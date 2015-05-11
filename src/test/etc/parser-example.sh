#!/bin/bash

DIR=$(dirname $0)
java -jar $DIR/../../../target/playground-1.0-SNAPSHOT.jar parse $1
