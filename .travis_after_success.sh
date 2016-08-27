#!/bin/bash

mvn -B jacoco:report coveralls:report

version_match=`perl -e "print '$TRAVIS_TAG' =~ /^jdbj-\d+\.\d+\.\d+\.\d+$/"`
if [[ "$version_match" == "1" ]]; then
    mvn deploy --settings .travis/release-settings.xml -DskipTests=true -B
    curl -X POST --data-urlencode "payload={ \"text\": \"${TRAVIS_TAG} has been released\"}" "${slackrelease}"
fi
