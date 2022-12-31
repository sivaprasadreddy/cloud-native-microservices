#!/bin/bash

declare project_dir=$(dirname "$0")
declare dc_manifests_dir=${project_dir}/docker
declare dc_infra=${dc_manifests_dir}/infra.yml
declare dc_apps=${dc_manifests_dir}/apps.yml

function build() {

    cd product-service-quarkus || exit
    ./mvnw clean spotless:apply verify
    cd ..

    cd product-service-boot || exit
    ./mvnw clean spotless:apply verify
    cd ..

    cd order-service-boot || exit
    ./mvnw clean spotless:apply verify
    cd ..

    cd delivery-service-micronaut || exit
    ./mvnw clean spotless:apply verify
    cd ..

    cd bookstore-ui || exit
    npm install
    npm run build
    cd ..
}

function build_ui_image() {
    cd bookstore-ui || exit
    docker build -f Dockerfile -t sivaprasadreddy/bookstore-ui .
    cd ..
}

function build_images() {
    cd product-service-quarkus || exit
    #docker build -f src/main/docker/Dockerfile.jvm -t sivaprasadreddy/product-service-quarkus .
    ./mvnw clean package -Dquarkus.container-image.build=true
    cd ..

    cd product-service-boot || exit
    #./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/product-service-boot
    ./mvnw -DskipTests jib:dockerBuild -Dimage=sivaprasadreddy/product-service-boot
    cd ..

    cd order-service-boot || exit
    #./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sivaprasadreddy/order-service-boot
    ./mvnw -DskipTests jib:dockerBuild -Dimage=sivaprasadreddy/order-service-boot
    cd ..

    cd delivery-service-micronaut || exit
    #docker build -f Dockerfile -t sivaprasadreddy/delivery-service-micronaut .
    ./mvnw package -Dpackaging=docker
    cd ..

    build_ui_image
}

function build_images_native() {
    cd product-service-quarkus || exit
    ./mvnw clean package -Pnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
    docker build -f src/main/docker/Dockerfile.native -t sivaprasadreddy/product-service-quarkus-native .
    cd ..

#    cd product-service-boot || exit
#    ./mvnw spring-boot:build-image -Pnative -Dspring-boot.build-image.imageName=sivaprasadreddy/product-service-boot-native
#    cd ..

    cd order-service-boot || exit
    ./mvnw spring-boot:build-image -Pnative -Dspring-boot.build-image.imageName=sivaprasadreddy/order-service-boot-native
    cd ..

    cd delivery-service-micronaut || exit
    # This is not native image
    ./mvnw package -Dpackaging=docker -Ddockerhub.imageName=sivaprasadreddy/delivery-service-micronaut-native
    # Need to figure out how to build a native image without installing GraalVM locally
    #./mvnw package -Ddockerhub.imageName=sivaprasadreddy/delivery-service-micronaut-native -Dpackaging=docker-native
    cd ..

    build_ui_image
}

function start_infra() {
    docker-compose -f "${dc_infra}" up -d
}

function stop_infra() {
    docker-compose -f "${dc_infra}" stop
    docker-compose -f "${dc_infra}" rm -f
}

function restart_infra() {
    stop_infra
    sleep 5
    start_infra
}

function start() {
    #build_images
    docker-compose --env-file "${dc_manifests_dir}/.env" -f "${dc_infra}" -f "${dc_apps}" up --force-recreate -d
}

function start_native() {
    #build_images
    docker-compose --env-file "${dc_manifests_dir}/.env.native" -f "${dc_infra}" -f "${dc_apps}" up --force-recreate -d
}

function stop() {
    docker-compose -f "${dc_infra}" -f "${dc_apps}" stop
    docker-compose -f "${dc_infra}" -f "${dc_apps}" rm -f
}

function restart() {
    stop
    sleep 10
    start
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$*
fi

eval "${action}"
