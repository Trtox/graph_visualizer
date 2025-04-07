#!/bin/bash

mvn clean dokka:dokka

open target/dokka/index.html