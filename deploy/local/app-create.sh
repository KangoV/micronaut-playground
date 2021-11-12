#!/bin/bash

DIR="app"
APP="app"

kubectl create -f ${DIR}/${APP}-configmap.yaml
kubectl create -f ${DIR}/${APP}-deployment.yaml
kubectl create -f ${DIR}/${APP}-service.yaml
