(ns kubernetes.api.networking-istio-io-v1
  (:require [kubernetes.api.openapiv3 :as openapiv3]
            [kubernetes.api.util :as util]))

(def make-context util/make-context)

(openapiv3/render-full-api "networking.istio.io_v1")
