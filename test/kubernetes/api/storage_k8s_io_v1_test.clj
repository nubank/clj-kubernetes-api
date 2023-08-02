(ns kubernetes.api.storage-k8s-io-v1-test
  (:require [clojure.core.async :refer [<!!] :as async]
            [clojure.test :refer [deftest is testing use-fixtures]]
            [kubernetes.api.storage-k8s-io-v1 :as storage-api]
            [kubernetes.api.common :as common]
            [kubernetes.api.v1 :as v1]))

(def ctx (storage-api/make-context "http://localhost:8080"))
(def tns (common/random-name))

(def nsopt {:namespace tns})

(use-fixtures :once
              (fn [f]
                (<!! (v1/create-namespace ctx {:metadata {:name tns}}))
                (<!! (async/timeout 2000))
                (f)
                (<!! (v1/delete-namespace ctx {} {:name tns}))))

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
    (let [{:keys [kind metadata] :as sc} (<!! (storage-api/create-storage-v1storage-class ctx storage-class))]
      (is (= kind "StorageClass"))
      (is (= (:name metadata) storage-class-name))))

  (testing "listing storage-classes"
    (let [storage-classes (<!! (storage-api/list-storage-v1storage-class ctx))]
      (is (= storage-class-name (-> storage-classes :items first :metadata :name)))
      (is (= "StorageClassList" (:kind storage-classes)))))

  (testing "reading single storage-class"
    (let [{:keys [kind metadata]} (<!! (storage-api/read-storage-v1storage-class ctx (assoc nsopt :name storage-class-name)))]
      (is (= kind "StorageClass"))
      (is (= (:name metadata) storage-class-name))))

  (testing "deleting storage-class"
    (let [_ (<!! (storage-api/delete-storage-v1storage-class ctx {} (assoc nsopt :name storage-class-name)))
          {:keys [reason]} (<!! (storage-api/read-storage-v1storage-class ctx (assoc nsopt :name storage-class-name)))]
      (is (= "NotFound" reason)))))
