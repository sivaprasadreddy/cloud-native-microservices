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
