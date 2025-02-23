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

    def auth_login(gke_cluster_name,gke_region){
        jenkins.sh """
        echo "Entering into K8 Auth/Login Method"
        gcloud compute instances list
        gcloud container clusters get-credentials $gke_cluster_name --region $gke_region --project smart-k8
        kubectl get nodes
        """
    }

    // K8 Deployment

    def deployinK8(fileName,namespace,docker_image){
        jenkins.sh """
        echo "Entering into K8 Deploy Method"
        sed -i "s|DIT|${docker_image}|g" ./.cicd/$fileName 
        kubectl apply -f ./.cicd/$fileName -n $namespace
        """
    }


    // Helm Deployment

    def k8sHelmChartDeploy(appName, env, helmChartPath, imageTag){
        jenkins.sh """
        echo "Entering Helm Groovy Method"
        echo "Verifying if helm chart already exists"
        if helm list | grep -q ${appName}-${env}-chart; then
            echo "this chart exists, now we will upgrade the chart"
            helm upgrade ${appName}-${env}-chart -f  ./.cicd/helm/values_${env}.yaml --set image.tag=${imageTag}  ${helmChartPath} 
        else
            echo "Intalling the Chart "
            helm install ${appName}-${env}-chart -f  ./.cicd/helm/values_${env}.yaml --set image.tag=${imageTag}  ${helmChartPath} 
        """
    }

    def gitClone(){
        jenkins.sh """
        echo "Executing Git Clone for getting the Chart from Shared Library"
        git clone -b main https://github.com/nzalpha/smartapp-shared-library.git 
        echo "listint the files after clone"
        """
    }




}