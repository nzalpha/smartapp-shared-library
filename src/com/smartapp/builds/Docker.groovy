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

    def auth_login(gke_cluster_name,region){
        jenkins.sh """
        echo "Entering into K8 Auth/Login Method"
        gcloud compute instances list
        gcloud container clusters get-credentials $gke_cluster_name --region $gke_region --project smart-k8
       // gcloud container clusters get-credentials cluster-2 --region us-central1 --project smart-k8
        Kubectl get nodes
        """
    }

    // Docker build

    // Docker login




}