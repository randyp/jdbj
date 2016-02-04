#!/bin/bash

set -e

psql -U postgres < src/test/java/io/codemonastery/jdbj/db/postgres_9_4/setup.sql
mysql -uroot < src/test/java/io/codemonastery/jdbj/db/mysql_5_1/setup.sql
echo "coveralls ${coverallstoken:0:3}"
mvn -B clean test jacoco:report coveralls:report
