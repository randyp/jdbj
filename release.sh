#!/bin/bash

set -e

mvn release:clean release:prepare -B
mvn release:perform
