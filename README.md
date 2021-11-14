# Micronaut Playground
---
[![Java CI](https://github.com/KangoV/micronaut-playground/actions/workflows/gradle.yml/badge.svg)](https://github.com/KangoV/micronaut-playground/actions/workflows/gradle.yml)

A Micronaut project to try stuff out to see what works and what breaks. Please feel free to create issues with recommendations etc.

Micronaut is a fantastic framework for creating all kinds of Java apps. This little java app has been created to test out new functions and libraries

The main libraries are:

* Micronaut (of course)
* Immutables
* Mapstruct

## Build/Run

The only Kubenetes implementationm I have used so far is Kind. To install, follow the instruction at https://kind.sigs.k8s.io/docs/user/quick-start/. Once installed follow the below instructions:

### Linux/MacOs

First Create the cluster:
```shell
$ kind create cluster
Creating cluster "kind" ...
 ✓ Ensuring node image (kindest/node:v1.21.1) 🖼
 ✓ Preparing nodes 📦  
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
Set kubectl context to "kind-kind"
You can now use your cluster with:

kubectl cluster-info --context kind-kind

Thanks for using kind! 😊

$ kubectl get nodes
NAME                 STATUS   ROLES                  AGE    VERSION
kind-control-plane   Ready    control-plane,master   112s   v1.21.1
```

Now build:
```shell
$ ./gradlew clean build jibDockerBuild
.
.
BUILD SUCCESSFUL in 21s
17 actionable tasks: 17 executed
```

We now have a docker image:
```shell
$ docker images
REPOSITORY             TAG           IMAGE ID       CREATED         SIZE
kindest/node           <none>        32b8b755dee8   5 months ago    1.12GB
testcontainers/ryuk    0.3.1         ee7515743e6f   10 months ago   12MB
postgres               10.5-alpine   294f651dec48   3 years ago     71.6MB
playground-micronaut   1.0.0         c260e0a9a967   51 years ago    349MB
```

During the build testcontainers and postgres images would have been pulled. You can also see the kind image. Although we have build the image for the application, it is only visible to docker and not Kind. We need to load the image into Kind's internal registry so that the it can be found. Use the following command:

```shell
$ kind load docker-image playground-micronaut:1.0.0
Image: "playground-micronaut:1.0.0" with ID "sha256:c260e0a9a967ee7ef8c8892e1d36d0a954f2f0d75a57a647b4925fc47c27ac81" not yet present on node "kind-control-plane", loading...
```

```shell
$ cd deploy/local
$ ./db-create.sh
configmap/playground-db-config created
persistentvolume/playground-db-pv-volume created
persistentvolumeclaim/playground-db-pv-claim created
deployment.apps/playground-db created
service/playground-db-service created
$ ./app-create.sh
configmap/playground-app-config created
deployment.apps/playground-app created
service/playground-app-service created
```

Confirm the pods/services have been created:

```shell
$ kubectl get pods,svc
NAME                             READY   STATUS         RESTARTS   AGE
playground-app-9d878554b-22fxt   1/1     Running        0          69s
playground-db-564c569f8f-8jvng   1/1     Running        0          2m32s
NAME                             TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
service/kubernetes               ClusterIP   10.96.0.1       <none>        443/TCP          24m
service/playground-app-service   NodePort    10.96.146.37    <none>        8080:31083/TCP   7m54s
service/playground-db-service    NodePort    10.96.196.251   <none>        5432:30463/TCP   9m16s
```

In order to access the application we can use port forwarding. In a different shell, run the followuing:
```shell
$ kubectl port-forward service/playground-app-service 8080:8080
Forwarding from 127.0.0.1:8080 -> 8080
Forwarding from [::1]:8080 -> 8080
Handling connection for 8080
```

In your original shell run:
```shell
$ curl localhost:8080/clients
```
should reault in a list of clients that were initially loaded through Flyway:
```json
[
  {
    "id": "d967da01-9d66-4623-9762-68506151006c",
    "version": 0,
    "created": "2021-11-14T12:02:52.059473",
    "forename": "David",
    "surname": "Ball",
    "phones": [
      {
        "name": "HOME",
        "number": "+1234123456"
      },
      {
        "name": "WORK",
        "number": "+1234123456"
      }
    ],
    "email": "david.ball@nodomain",
    "telephone": "+441234123456",
    "sex": "MALE",
    "status": "ACTIVE"
  },
  {
    "id": "d967da01-9d66-4623-9762-68506151006d",
    "version": 0,
    "created": "2021-11-14T12:02:52.059473",
    "forename": "Sarah",
    "surname": "Ball",
    "email": "sarah.ball@nodomain",
    "telephone": "+441234123456",
    "sex": "FEMALE",
    "status": "SUSPENDED"
  }
]
```

## Project layout

Most projects that I have come across have a layout reflects the type of class within the project and not what they represent. For example, you'll often see the following packages:

```
* configuration
* controller
* jpa
* model
* security
* service
```
This in my view masks the intent of the application. Uncle Bob agrees with me :)
This project uses the following layout

```
 + api
 + client
   + api
       PhoneType
       MaritalStatus
   + infra
       ClientDAO
       ClientE
   + web
       ClientController
       ClientT
     ClientService
     ClientStore
 + domain
     DomainEntity
   Application
```
The `client` is the resource being managed by this application. 

The domain classes go into the root of `client`. The service is used by the web layer. the store is the domain facing persistence layer which has the dao (Micronaut Repository) injected.
Within the this `/client` directory there are:

* api - This is any classes that apply across all the 3 layers (web,domain,infrastructure). The intent is for these to be packaged into a `-client.jar` file which can be pulled in as a dependency. This is still work in progress though.
* infra - This holds the data access layer. The `CliebntDao` is a Micronaut repository that manages a `ClientE` entity.
* web - This package holds the controllers and the model to be exposed to clients/users of the api. 

The reasoning behind this layout is that domain (business) code and rules do not rely on anything the from web layer and also has no idea how domain objects are persisted.

One thing that I've not decided on is to merge the infra, web and domain packages into one as the classes are named after their function.

## Object Naming

Data and domain objects are named according to their use

* Client - The domain object for a client. This is where the business rules should ideally reside (according to Domain Driven Design).
* ClientT - The web facing version of a client (maybe should be `ClientW`?). I chose `T` for transport.
* ClientE - This is the persistence facing version of the client which is never passed out of the store. The store translates to and from the domain version of the client.

## Immutables Generation

Immutables.io is used to create the web and domain objects. These interfaces have a `*Spec` suffix. The immutable instance is generated with the suffix removed (ClientTSpec -> ClientT).
There is a style (`ImmutableStyle`) which tells the immutables annotation processor how to do this.

## Data Transformation

The translation of data between the 3 layers is managed via MapStruct mappers. This removes a huge amount of hand crafted code. There are still a few issues to be resilved with Mapstruct though:

* Converting an `Optional<>` to another `Optional<>` is not handled well, hence all the mappers listed in `BaseMapper`. But, even with this, the code reduction is considerable. With more managed resources the saving will be quite large.
* Converting to an optional from a nested property does not work.

One of the tasks needed is to create a branches for each problem and then create issues in the MapStruct Github project.

This is the first stab at the documentation. It will improve, honest ;)

