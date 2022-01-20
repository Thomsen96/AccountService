#!/bin/bash
set -e

# Build the services
pushd AccountService
./build.sh
popd
