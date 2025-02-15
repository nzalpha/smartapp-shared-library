
import com.smartapp.builds.Docker

def call(Map pipelineParams){
    Docker d = new Docker(this)

    pipeline{
    agent{
        label 'workernode-1'
    }

    environment{
        Application_Name = "${pipelineParams.appName}"
    }

    stages{
        stage("K8 Auth Login Stage"){
            steps{
                script{
                    echo "--------------------- Executing Login  Stage ----------------------"
                     d.buildApp("${env.Application_Name}")
                     d.auth_login()
                }
            }
        }
    }

    }

}
