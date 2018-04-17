# lmvc
Model-View-Controller Framework

Prerequisites
-------------
* Java JDK 1.9 or greater
http://www.oracle.com/technetwork/java/javase/downloads/index.html

Build and Install
------
Export environment variables

    export JAVA_HOME=/path/to/jdk
    export M2_HOME=/path/to/maven
    export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH

In the directory that contains the pom.xml run

        mvn clean package install
