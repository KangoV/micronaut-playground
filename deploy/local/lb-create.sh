#!/bin/bash

# please see : https://kind.sigs.k8s.io/docs/user/loadbalancer/

kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/master/manifests/namespace.yaml
kubectl create secret generic -n metallb-system memberlist --from-literal=secretkey="$(openssl rand -base64 128)"
kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/master/manifests/metallb.yaml

#kubectl get pods -n metallb-system --watch

# To complete layer2 configuration, we need to provide metallb a range of IP addresses it controls.
# We want this range to be on the docker kind network.

