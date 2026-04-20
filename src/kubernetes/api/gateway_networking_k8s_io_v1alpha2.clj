(ns kubernetes.api.gateway-networking-k8s-io-v1alpha2
  (:require [kubernetes.api.openapiv3 :as openapiv3]
            [kubernetes.api.util :as util]))

(def make-context util/make-context)

(openapiv3/render-full-api "gateway.networking.k8s.io_v1alpha2")
