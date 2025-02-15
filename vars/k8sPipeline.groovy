
import com.smartapp.builds.k8s.k8s

def call(Map pipelineParams){
    k8s k = new k8s(this)

    pipeline{
    agent{
        label 'workernode-1'
    }

    environment{
        Application_Name = "${pipelineParams.appName}"
    }

    stages{
        stage("Auth"){
            steps{
                script{
                    echo "--------------------- Executing auth Stage ----------------------"
                    k.auth_login()
                }
            }
        }
    }

    }

}
