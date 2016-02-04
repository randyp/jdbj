#!/bin/bash

echo "coverallsToken starts with ${coverallsToken:0:4}"
mvn -DrepoToken="${coverallsToken}" -B clean test jacoco:report coveralls:report
