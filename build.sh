#!/bin/bash
set -e

# Build the services
pushd AccountService
./build.sh
popd

pushd messaging-utilities-3.2
./build.sh
popd