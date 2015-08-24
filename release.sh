#!/bin/bash

set -e

#do release
mvn clean release:clean release:prepare -B
mvn release:perform


PROJECT_VERSION=`ls target/jdbj*-sources.jar | cut -d'-' -f2`
cd ..

#copy to gh-pages branch
git clone git@github.com:randyp/jdbj.git jdbj-gh-pages || true
pushd jdbj-gh-pages
git reset --hard HEAD
git checkout gh-pages
git pull
mkdir -p apidocs
cp -R ../jdbj/target/apidocs "apidocs/${PROJECT_VERSION}"
git add "apidocs/${PROJECT_VERSION}" && git commit -am "${PROJECT_VERSION} docs" && git push
PROJECT_VERSIONS=`ls apidocs | sort -n -r`
popd

##update javadoc
git clone git@github.com:randyp/jdbj.wiki.git || true
pushd jdbj.wiki && git checkout master && git pull
git reset --hard HEAD
git checkout master
git pull

echo "Java Docs" >  _Sidebar.md
for PROJECT_VERSION in ${PROJECT_VERSIONS}; do
    echo "* [${PROJECT_VERSION}](http://randyp.github.io/jdbj/apidocs/${PROJECT_VERSION}/index.html)" >> _Sidebar.md 
done

git add _Sidebar.md && git commit -am "${PROJECT_VERSION} docs" && git push
popd
