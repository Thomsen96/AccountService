## run the docker conatiner with the command bwlow. where the entry point especialy is important.
#docker run --name dtu_client_cont --network=deploy_app_network --entrypoint /home/app/run.sh dtupay_client:latest

FROM maven:3.8.4-jdk-11-slim
ENV IN_DOCKER_ENV=True
## Get source code into the container
COPY features /home/app/features
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app/

## Create a run file on the container that can start the test, in "quiet mode"
RUN echo "#!/bin/bash \n mvn --no-transfer-progress -f /home/app/pom.xml test" > /home/app/run.sh

## Make scripte executeable
RUN chmod +x /home/app/run.sh


