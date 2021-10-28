# Micronaut Playground
---
[![Java CI](https://github.com/KangoV/micronaut-playground/actions/workflows/gradle.yml/badge.svg)](https://github.com/KangoV/micronaut-playground/actions/workflows/gradle.yml)

A Micronaut project to try stuff out to see what works and what breaks. Please feel free to create issues with recommendations etc.

Micronaut is a fantastic framework for creating all kinds of Java apps. This little java app has been created to test out new functions and libraries

The main libraries are:

* Micronaut (of course)
* Immutables
* Mapstruct

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

