
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
        stage("Build the app"){
            steps{
                script{
                    echo "--------------------- Executing Build Stage ----------------------"
                    println k.auth_login(5,9)
                }
            }
        }
    }

    }

}
