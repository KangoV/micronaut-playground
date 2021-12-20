# Micronaut Playground
---
[![Java CI](https://github.com/KangoV/micronaut-playground/actions/workflows/gradle.yml/badge.svg)](https://github.com/KangoV/micronaut-playground/actions/workflows/gradle.yml)

A Micronaut project to try stuff out to see what works and what breaks. Please feel free to create issues with recommendations etc.

Micronaut is a fantastic framework for creating all kinds of Java apps. This little java app has been created to test out new functions and libraries

The main libraries are:

* Micronaut (of course)
* Immutables
* Mapstruct
* Kubernetes
* Kind
* Helm

## Main Goals

* Keep the development experience simple
* Easily spin up locally
* Learn new technologies

## The future

* Spilt into multiple modules (services) -- Done
* Introduce messaging (Kafka) -- Done
* Full integration tests will always run locally

## Build/Run

The only Kubenetes implementationm I have used so far is Kind. To install, follow the instruction at https://kind.sigs.k8s.io/docs/user/quick-start/. Once installed follow the below instructions:

### Linux/MacOs

#### TL;DR

```shell
$ ./gradlew clean build jibDockerBuild
$ kind create cluster
$ deploy/loadimages.sh
$ helm install playground deploy/playground/
$ kubectl port-forward service/clients-app-service 8081:8081
$ curl localhost:8080/clients
```

#### Documented Build Process

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
BUILD SUCCESSFUL in 2m 15s
61 actionable tasks: 61 executed
```

We now have the following docker images:
```shell
$ docker images
REPOSITORY              TAG           IMAGE ID       CREATED         SIZE
confluentinc/cp-kafka   latest        7d9766481102   5 days ago      784MB
testcontainers/ryuk     0.3.3         64f4b02dc986   2 months ago    12MB
postgres                10.5-alpine   294f651dec48   3 years ago     71.6MB
kindest/node            <none>        32b8b755dee8   7 months ago    1.12GB
pg-sessions-backend     1.0.0         d97e14d62747   52 years ago    383MB
pg-client-backend       1.0.0         272fd90fbd0e   52 years ago    349MB
pg-events-backend       1.0.0         ad674d2665f2   52 years ago    390MB
pg-clients-backend      1.0.0         d2b5942511b0   52 years ago    397MB
```

During the build testcontainers, postgres and kafka images would have been pulled. You can also see the kind image. Although we have built the images for the application, they are only visible to docker and not Kind. We need to load the images into Kind's internal registry so that they can be found. Use the following command:

```shell
$ deploy/loadimages.sh
Image: "pg-clients-backend:1.0.0" with ID "sha256:d2b5942511b0cad05afc6cb2af17a60865d290ccc56602e7e8ef8f32b944bcbc" not yet present on node "kind-control-plane", loading...
Image: "pg-sessions-backend:1.0.0" with ID "sha256:d97e14d6274707aeba340a57ff59b3f69d8421e577aedd0bcf78fec050a8d737" not yet present on node "kind-control-plane", loading...
Image: "pg-events-backend:1.0.0" with ID "sha256:ad674d2665f200df16ba0a83cde0c31a62bae5616818a0a70f334fca38a4fd32" not yet present on node "kind-control-plane", loading...
```

Now that Kind has access to the mages that were just built, Helm can be used to deploy the services:

```shell
$ helm install playground deploy/playground/
NAME: playground
LAST DEPLOYED: Mon Dec 20 17:55:47 2021
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
```

Confirm the pods/services have been created:

```shell
$ kubectl get pods,svc
NAME                                READY   STATUS             RESTARTS   AGE
pod/clients-app-7794c67c5-k9dmf     1/1     Running            4          109s
pod/clients-db-0                    1/1     Running            0          109s
pod/events-app-789cc8945f-q9fgb     1/1     Running            0          109s
pod/kafka-app-64d7f596c-9jr7f       1/1     Running            0          109s
pod/sessions-app-86f4dd8496-z49hv   0/1     CrashLoopBackOff   3          109s
pod/sessions-db-0                   1/1     Running            0          109s
pod/zookeeper-85f6ffb789-qvmx6      1/1     Running            0          109s
NAME                           TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)                      AGE
service/clients-app-service    NodePort    10.96.57.205   <none>        8081:31450/TCP               109s
service/clients-db-service     NodePort    10.96.91.33    <none>        5432:30704/TCP               109s
service/events-app-service     NodePort    10.96.66.118   <none>        8081:30617/TCP               109s
service/kafka-service          ClusterIP   10.96.22.71    <none>        29092/TCP                    109s
service/kubernetes             ClusterIP   10.96.0.1      <none>        443/TCP                      5m29s
service/sessions-app-service   NodePort    10.96.234.57   <none>        8081:30657/TCP               109s
```

Note that `session-app-<hash>` is in `CrashLoopBackOff` status as there is an issue at this point. Also note that all services are using port 8081. This is a bug which is yet to be resolved.

In order to access the application we can use port forwarding. In a different shell, run the followuing:

```shell
$ kubectl port-forward service/clients-app-service 8081:8081
Forwarding from 127.0.0.1:8081 -> 8081
Forwarding from [::1]:8081 -> 8081
Handling connection for 8081
```

In your original shell run:
```shell
$ curl localhost:8081/clients
```
should result in a list of clients that were initially loaded through Flyway:
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

