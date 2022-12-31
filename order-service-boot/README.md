# Order Service SpringBoot

## How to run tests?
```shell
$ ./mvnw verify
```

## How to run application?
```shell
$ ../run.sh start_infra
$ ./mvnw spring-boot:run
```

## How to build docker image?
```shell
# using Buildpacks
$ ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/order-service-boot

# using Jib
$ ./mvnw -DskipTests jib:dockerBuild -Dimage=sivaprasadreddy/order-service-boot
```

## How to build GraalVM Native Image
```shell
$ sdk install java 22.3.r17-nik
$ sdk use java 22.3.r17-nik
# to create a native binary using Native Tools
$ ./mvnw -Pnative native:compile
# to create docker image with native binary using buildpacks
$ ./mvnw -Pnative spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/order-service-boot-native
```
