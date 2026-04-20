(ns kubernetes.api.openapiv3
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.walk :as walk]
            [kubernetes.api.openapiv2 :as openapiv2]
            [kubernetes.api.swagger :as swagger]))

(defn operation-parameters [path-level-parameters parameters]
  (mapv
   (fn [param]
     {:type          (get-in param [:schema :type])
      :paramType     (:in param)
      :name          (:name param)
      :description   (:description param)
      :required      (:required param)
      :allowMultiple false})
   (concat path-level-parameters parameters)))

(defn- stringify [kw]
  (str (namespace kw) "/" (name kw)))

(defn paths->apis [paths]
  (mapv (fn [[path {:keys [parameters] :as operations}]]
          {:path (subs (str path) 1)
           :operations (mapv (fn [[method operation]]
                               (let [swagger-compatible-params
                                     (concat (openapiv2/operation-parameters parameters (:parameters operation))
                                             (when (contains? #{:post :put :patch} method)
                                               (let [schema-ref (-> operation
                                                                    (get-in [:requestBody :content])
                                                                    first second
                                                                    (get-in [:schema :$ref]))]
                                                 [{:type (subs schema-ref (inc (str/last-index-of schema-ref "/")))
                                                   :paramType "body"
                                                   :name "body"
                                                   :description ""
                                                   :required true
                                                   :allowMultiple false}])))]
                                 {:type ""
                                  :consumes (-> operation (get-in [:requestBody :content]) keys (#(mapv stringify %)))
                                  :produces (-> (filter #(str/starts-with? (name (first %)) "2") (:responses operation))
                                                first
                                                second
                                                :content
                                                keys
                                                (#(mapv stringify %)))
                                  :summary (:description operation)
                                  :nickname (:operationId operation)
                                  :method (str/upper-case (name method))
                                  :x-kubernetes-group-version-kind (:x-kubernetes-group-version-kind operation)
                                  :parameters swagger-compatible-params}))
                             (select-keys operations [:get :patch :delete :put :post :head :options]))})
        paths))

(defn openapiv3->swagger [openapiv3]
  {:swaggerVersion "1.2"
   :apis (paths->apis (:paths openapiv3))})

(defmacro render-full-api [k8s-version]
  (-> (str "openapiv3/" k8s-version ".json")
      io/resource
      slurp
      (json/read-str :key-fn keyword)
      openapiv3->swagger
      swagger/render-swagger))
