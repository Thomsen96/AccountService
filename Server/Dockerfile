# Build with: docker build --tag dtupay .  
# Test with: docker run -ti -p 8080:8080 dtupay



FROM maven:3.8.4-jdk-11-slim

## Get source code
COPY src /home/app/src
COPY pom.xml /home/app

## Build application
WORKDIR /home/app
RUN mvn clean package

## Run application
WORKDIR /home/app/target
EXPOSE 8080

# The -Xmx64m demanding from the java runtime to have atleast allocated 64m memory
# https://docs.oracle.com/cd/E13222_01/wls/docs61/perform/JVMTuning.html
CMD java -Xmx64m -jar quarkus-app/quarkus-run.jar


