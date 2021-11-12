#!/bin/bash

APP="playground-app"

kubectl delete service ${APP}-service
kubectl delete deployment ${APP}
kubectl delete configmap ${APP}-config

