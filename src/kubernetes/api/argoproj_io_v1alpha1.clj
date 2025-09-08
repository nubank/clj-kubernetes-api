(ns kubernetes.api.argoproj-io-v1alpha1
  (:require [kubernetes.api.swagger :as swagger]
            [kubernetes.api.util :as util]))

(def make-context util/make-context)

(swagger/render-full-api "argoproj_io_v1alpha1")
