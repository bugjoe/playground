#!/bin/bash

DIR=$(dirname $0)
java -jar $DIR/../../../target/playground-1.0-SNAPSHOT.jar scan 8 135b4a0d 135b4a8d
