(ns kubernetes.api.v1-23-17.apps-v1-test
  (:require [clojure.core.async :refer [<!!] :as async]
            [clojure.test :refer [deftest is testing use-fixtures]]
            [kubernetes.api.v1-23-17.apps-v1 :as app-v1-23-17]
            [kubernetes.api.common :as common]
            [kubernetes.api.v1 :as v1]))

(def ctx (app-v1-23-17/make-context "http://localhost:8080"))
(def tns (common/random-name))
(def stateful-set-name (common/random-name))

(def nsopt {:namespace tns})
(def stateful-set {:apiVersion "apps/v1"
                   :kind       "StatefulSet"
                   :metadata   {:name stateful-set-name}
                   :spec       {:selector {:matchLabels {:service "service"}}
                                :template {:metadata {:labels {:service "service"}}
                                           :spec     {:containers [{:name  "kafka"
                                                                    :image "kafka"}]}}}})

(use-fixtures :once
              (fn [f]
                (<!! (v1/create-namespace ctx {:metadata {:name tns}}))
                (<!! (async/timeout 2000))
                (f)
                (<!! (v1/delete-namespace ctx {} {:name tns}))))

(deftest stateful-set-test
  (testing "creation of stateful-sets"
    (let [{:keys [kind metadata]} (<!! (app-v1-23-17/create-apps-v1namespaced-stateful-set ctx stateful-set nsopt))]
      (is (= kind "StatefulSet"))
      (is (= (:name metadata) stateful-set-name))))

  (testing "listing stateful-sets"
    (let [stateful-sets (<!! (app-v1-23-17/list-apps-v1namespaced-stateful-set ctx nsopt))]
      (is (= stateful-set-name (-> stateful-sets :items first :metadata :name)))
      (is (= "StatefulSetList" (:kind stateful-sets)))))

  (testing "reading single stateful-set"
    (let [{:keys [kind metadata]} (<!! (app-v1-23-17/read-apps-v1namespaced-stateful-set ctx (assoc nsopt :name stateful-set-name)))]
      (is (= kind "StatefulSet"))
      (is (= (:name metadata) stateful-set-name))))

  (testing "deleting stateful-set"
    (let [_ (<!! (app-v1-23-17/delete-apps-v1namespaced-stateful-set ctx {} (assoc nsopt :name stateful-set-name)))
          {:keys [reason]} (<!! (app-v1-23-17/read-apps-v1namespaced-stateful-set ctx (assoc nsopt :name stateful-set-name)))]
      (is (= "NotFound" reason)))))

(def storage-class-name "sc-name")

(def storage-class
  {:api-version         "v1"
   :kind                "StorageClass"
   :metadata            {:name      storage-class-name
                         :namespace tns}
   :provisioner         "kubernetes.io/no-provisioner"
   :volume-binding-mode "WaitForFirstConsumer"})

(deftest storage-class-test
  (testing "creation of storage-class"
    (let [{:keys [kind metadata] :as sc} (<!! (app-v1-23-17/create-storage-v1storage-class ctx storage-class))]
      (is (= kind "StorageClass"))
      (is (= (:name metadata) storage-class-name))))

  (testing "listing storage-classes"
    (let [storage-classes (<!! (app-v1-23-17/list-storage-v1storage-class ctx))]
      (is (= storage-class-name (-> storage-classes :items first :metadata :name)))
      (is (= "StorageClassList" (:kind storage-classes)))))

  (testing "reading single storage-class"
    (let [{:keys [kind metadata]} (<!! (app-v1-23-17/read-storage-v1storage-class ctx (assoc nsopt :name storage-class-name)))]
      (is (= kind "StorageClass"))
      (is (= (:name metadata) storage-class-name))))

  (testing "deleting storage-class"
    (let [_ (<!! (app-v1-23-17/delete-storage-v1storage-class ctx {} (assoc nsopt :name storage-class-name)))
          {:keys [reason]} (<!! (app-v1-23-17/read-storage-v1storage-class ctx (assoc nsopt :name storage-class-name)))]
      (is (= "NotFound" reason)))))
