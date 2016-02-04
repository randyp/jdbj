#!/bin/bash

mvn -B clean test jacoco:report coveralls:report
