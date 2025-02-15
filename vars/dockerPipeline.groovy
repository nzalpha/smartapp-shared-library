
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
        stage("Adding"){
            steps{
                script{
                    echo "--------------------- Executing Add Method ----------------------"
                    printlin d.add(4,5)
                }
            }
        }
    }

    }

}
