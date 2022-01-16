#!/bin/bash
mvn package
mvn exec:java -D exec.mainClass=com.mvdl.launcher.Launcher
