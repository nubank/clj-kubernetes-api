#!/bin/bash

set -e # exit if any failures

###
###  This script will install a new clj namespace file from an installed openapi v3 kubernetes CRD
###
###  NOTE: To help find kubernetes control plane host, execute 'kubectl cluster-info'
###

#################################
####  Script Arguments   ########
#################################

KUBE_CTL_PLANE_HOST=$1
KUBE_CRD=$2
KUBE_CRD_VERSION=$3


#################################
#########   Logic   #############
#################################

if [ $# != 3 ]; then
  echo
  echo Usage: ./add_kubernetes_crd.sh '$KUBE_CTL_PLANE_HOST' '$KUBE_CRD $KUBE_CRD_VERSION'
  echo
  echo Example: ./add_kubernetes_crd.sh localhost:8080 gateway.networking.k8s.io v1
  echo
  exit 2
fi

echo
echo Provided arguments:
echo KUBE_CTL_PLANE_HOST = $KUBE_CTL_PLANE_HOST
echo KUBE_CRD = $KUBE_CRD
echo KUBE_CRD_VERSION = $KUBE_CRD_VERSION
echo

PROJECT_ROOT=$(dirname $0)/..

OPENAPI_V3_URL=${KUBE_CTL_PLANE_HOST}/openapi/v3/apis/${KUBE_CRD}/${KUBE_CRD_VERSION}
KUBE_CRD_JSON=${PROJECT_ROOT}/resources/openapiv3/${KUBE_CRD}_${KUBE_CRD_VERSION}.json
# echo Downloading CRD OpenAPI v3 json to $KUBE_CRD_JSON
wget -O $KUBE_CRD_JSON $OPENAPI_V3_URL

echo Pretty printing the json file
cp $KUBE_CRD_JSON ${KUBE_CRD_JSON}.tmp
jq . ${KUBE_CRD_JSON}.tmp > $KUBE_CRD_JSON
rm ${KUBE_CRD_JSON}.tmp
echo

CLJ_KUBE_CRD_FILE=${PROJECT_ROOT}/src/kubernetes/api/$(echo ${KUBE_CRD} | sed -r 's/\./_/g')_${KUBE_CRD_VERSION}.clj
CLJ_NS=kubernetes.api.$(echo ${KUBE_CRD} | sed -r 's/\./-/g')-${KUBE_CRD_VERSION}
echo Generating the clj kubernetes namespace file: ${CLJ_KUBE_CRD_FILE}
echo "\
(ns ${CLJ_NS}
  (:require [kubernetes.api.openapiv3 :as openapiv3]
            [kubernetes.api.util :as util]))

(def make-context util/make-context)

(openapiv3/render-full-api \"${KUBE_CRD}_${KUBE_CRD_VERSION}\")" > $CLJ_KUBE_CRD_FILE

echo
echo Finished
echo
