#!/bin/bash

echo "coverallsToken starts with ${coverallsToken:0:4}"
mvn -DrepoToken="${coverallsToken}" -B jacoco:report coveralls:report
