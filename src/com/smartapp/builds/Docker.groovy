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

    def auth_login(){
        jenkins.sh """
        echo "Entering into K8 Auth/Login Method"
        gcloud compute instances list
        gcloud container clusters get-credentials cluster-2 --region us-central1 --project smart-k8
        """
    }

    // Docker build

    // Docker login




}