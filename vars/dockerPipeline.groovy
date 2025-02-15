
import com.smartapp.builds.Docker

def call(Map pipelineParams){
    Docker d = new Docker(this)

    pipeline{
    agent{
        label 'workernode-1'
    }

    environment{
        Application_Name = "${pipelineParams.appName}"
        GKE_Dev_Cluster_Name = "cluster-2"
        GKE_Dev_Region= "us-central1"
    }

    stages{
        stage("K8 Auth Login Stage"){
            steps{
                script{
                    echo "--------------------- Executing Login  Stage ----------------------"
                     d.buildApp("${env.Application_Name}")
                     d.auth_login("${env.GKE_Dev_Cluster_Name}","${env.GKE_Dev_Region}")
                }
            }
        }
    }

    }

}
