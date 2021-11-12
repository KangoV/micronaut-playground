#!/bin/bash

DIR="postgres"
APP="db"

kubectl create -f ${DIR}/${APP}-configmap.yaml
kubectl create -f ${DIR}/${APP}-storage.yaml
kubectl create -f ${DIR}/${APP}-deployment.yaml
kubectl create -f ${DIR}/${APP}-service.yaml
