#!/bin/bash

set -e

postgres -U postgres < src/test/java/com/github/randyp/jdbj/db/postgres_9_4/setup.sql
mvn -B clean test jacoco:report coveralls:report