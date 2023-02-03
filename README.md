# Encrypted ID Demo

A [SpringBoot](https://spring.io/) 3.x application to demonstrate how to use [GitHub - fillumina/id-encryptor: Encrypts long values (64 bit) and UUIDs (128 bit)](https://github.com/fillumina/id-encryptor/). The application can be compiled in an executable using [GraalVM](https://www.graalvm.org/).

It is divided into two packages that simulates two different applications talking through a REST API:

- **shop** that simulates an e-commerce

- **accounting** that registers invoices

## Encryption applied in the REST controller

The encryption is applied at the JSON serialization level in the REST controller by using Jackson compatible annotations on the exported-imported classes (DTOs or entities). Parameters should be converted manually. The application service layer should not know anything about encryption or external identifiers.

## Encryption benefits

Encrypting identifiers can be beneficial for the following reasons:

- An **UUID** might contains information embedded that should not be disclosed (node id, time of creation, algorithm used, next value)

- In a default **sequential long ID** the next value predictability can be used to gain unauthorized access to data

- An application might want to use a sequential long ID internally but **expose an UUID externally** to be able to interact with other peers in a distributed environment

- Many different clients might want to send data to a **centralized application** (i.e. logging facility or a data backup).



![](./class%20diagram.png)
