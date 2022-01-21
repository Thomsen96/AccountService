# Account service

The service is a part of the total DTU Pay microservice system where where the **responsibility** of the service is to handle accounts. With regards to generation and verification of our own internal accounts. On creation of an DTUpay account, a check in the bank is done to verify that the bankaccount is valid in the FastMoney Bank.

## Build process

The build process is described with a series of scripts.

To do a complete build and deployment and test call the `./build_and_run.sh` script.

Each of the steps in this file is also described in the Jenkins file. Where the steps is called in the order of and these scripts will go down in each project respectively compile/build/package/create docker images then deploy and test the service

- `./build.sh`
- `./deploy.sh`
