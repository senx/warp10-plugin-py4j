#!/bin/sh

if [ $# != 3 ]
then
  echo "Usage: $0 plugin-name version \"Description\""
  exit 1
fi

NAME=$1
VERSION=$2
DESC=$3

sed -e "s/@PLUGIN_NAME@/${NAME}/g" settings.gradle > settings.gradle.new
mv -f settings.gradle.new settings.gradle

sed -e "s/@PLUGIN_VERSION@/${VERSION}/g" -e "s/@PLUGIN_DESCRIPTION@/${DESC}/g" build.gradle > build.gradle.new
mv -f build.gradle.new build.gradle
