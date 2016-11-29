.PHONY: all build release

IMAGE=glaizier/powerlifters
VERSION=$(shell cat VERSION)

all: build

build:
	@cd ../; \
	  ./mvnw clean package -P docker; \
	  mkdir -p docker/app; \
	  cp target/powerliftersdb.war docker/app;
	@docker build -t ${IMAGE} .

run: build
	@docker-compose up

release: build
	@docker build -t ${IMAGE}:${VERSION} .