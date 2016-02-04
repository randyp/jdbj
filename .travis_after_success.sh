#!/bin/bash

echo "coverallsToken starts with ${coverallsToken:0:4}"
mvn -B jacoco:report #coveralls:report
