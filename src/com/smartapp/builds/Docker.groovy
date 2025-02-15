package com.smartapp.builds

class Docker{
    def jenkins
    Docker(jenkins){
        this.jenkins =jenkins
    }

    // Test Addition method

    def add(fNum,sNum){
        
        return fNum + sNum
        
    }

    // Method for Application Build
    def buildApp(appName){
    return {
        echo "Building appName Application"
        // build using maven
        sh 'mvn clean package -DskipTests=true'
        archiveArtifacts artifacts: 'target/*.jar'
        echo "********"
    }
}

    // Docker build

    // Docker login




}