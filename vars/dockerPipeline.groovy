
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
                    d.add(5,6)
                }
            }
        }
    }

    }

}
