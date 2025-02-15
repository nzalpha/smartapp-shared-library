
import com.smartapp.builds.k8s.k8s

def call(Map pipelineParams){
    k8s d = new k8s(this)

    pipeline{
    agent{
        label 'workernode-1'
    }

    environment{
        Application_Name = "${pipelineParams.appName}"
    }

    stages{
        stage("Build the app"){
            steps{
                script{
                    echo "--------------------- Executing Build Stage ----------------------"
                    d.auth_login()
                }
            }
        }
    }

    }

}
