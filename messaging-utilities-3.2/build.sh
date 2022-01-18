#!/bin/bash
set -e
git submodule init
git submodule update
mvn clean install

