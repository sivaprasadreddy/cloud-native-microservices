# order-service
OrderService using Java + SpringBoot

## How to run tests?
```shell
$ ./mvnw verify
```

## How to run application?
```shell
$ docker-compose up -d
$ ./mvnw spring-boot:run
```

## How to build docker image?
```shell
$ ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/order-service
```

## How to build GraalVM Native Image
```shell
$ sdk install java 22.3.r17-nik
$ sdk use java 22.3.r17-nik
$ ./mvnw -Pnative native:compile
$ ./mvnw -Pnative spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/catalog-service-native
```
