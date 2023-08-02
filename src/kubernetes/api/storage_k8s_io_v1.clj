(ns kubernetes.api.storage-k8s-io-v1
  (:require [kubernetes.api.openapiv2 :as openapiv2]
            [kubernetes.api.util :as util]))

(def make-context util/make-context)

(openapiv2/render-api-group "v1.23.17" "storage.k8s.io" "v1")

; expose dynamically generated vars for static analysis
(declare
  create-storage-v1csi-driver
  create-storage-v1csi-node
  create-storage-v1storage-class
  create-storage-v1volume-attachment
  delete-storage-v1collection-csi-driver
  delete-storage-v1collection-csi-node
  delete-storage-v1collection-storage-class
  delete-storage-v1collection-volume-attachment
  delete-storage-v1csi-driver
  delete-storage-v1csi-node
  delete-storage-v1storage-class
  delete-storage-v1volume-attachment
  list-storage-v1csi-driver
  list-storage-v1csi-node
  list-storage-v1storage-class
  list-storage-v1volume-attachment
  patch-storage-v1csi-driver
  patch-storage-v1csi-node
  patch-storage-v1storage-class
  patch-storage-v1volume-attachment
  patch-storage-v1volume-attachment-status
  read-storage-v1csi-driver
  read-storage-v1csi-node
  read-storage-v1storage-class
  read-storage-v1volume-attachment
  read-storage-v1volume-attachment-status
  replace-storage-v1csi-driver
  replace-storage-v1csi-node
  replace-storage-v1storage-class
  replace-storage-v1volume-attachment
  replace-storage-v1volume-attachment-status)
