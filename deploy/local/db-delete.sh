#!/bin/bash

APP="playground-db"

kubectl delete service ${APP}-service
kubectl delete deployment ${APP}
kubectl delete configmap ${APP}-config
kubectl delete persistentvolumeclaim ${APP}-pv-claim
kubectl delete persistentvolume ${APP}-pv-volume
