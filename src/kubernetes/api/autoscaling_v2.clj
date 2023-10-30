(ns kubernetes.api.autoscaling-v2
  (:require [kubernetes.api.openapiv2 :as openapiv2]
            [kubernetes.api.util :as util]))

(def make-context util/make-context)

(openapiv2/render-api-group "v1.23.17" "autoscaling" "v2")