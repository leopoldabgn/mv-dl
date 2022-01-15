#!/bin/bash
mvn package
java -cp ./target/*mv-dl*.jar com.mvdl.launcher.Launcher
