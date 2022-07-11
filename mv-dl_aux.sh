#!/bin/bash
mvn package
mvn exec:java -D exec.mainClass=launcher.Launcher
