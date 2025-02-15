package com.smartapp.builds.k8s

class k8s{
    def jenkins
    k8s(jenkins)
    {
        this.jenkins = jenkins
    }

    // Authentication to Cluster
    def auth_login(){
        jenkins.sh """
        echo "Entering into K8 Auth/Login Method"
        gcloud auth activate-service-account jenkins@smart-k8.iam.gserviceaccount.com --key-file=${gke_svc_key}
        gcloud compute instance list
        """
    }

}