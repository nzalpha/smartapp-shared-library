
import com.smartapp.builds.Docker

def call(Map pipelineParams){
    Docker d = new Docker(this)

    pipeline{
    agent{
        label 'workernode-1'
    }

    parameters{
        choice (name: 'buildOnly',
                choices: 'no\nyes',
                description: "Build the Application only")
        choice (name: 'dockerformat',
                choices: 'no\nyes',
                description: "format docker")
        choice (name: 'dockerPush',
                choices: 'no\nyes',
                description: "this will push to registry")
        choice (name: 'K8deployToDev',
                choices: 'no\nyes',
                description: "Deploy app to Dev only ")
        choice (name: 'K8deployToStg',
                choices: 'no\nyes',
                description: "Deploy app to Stage only ")
        choice (name: 'K8deployToProd',
                choices: 'no\nyes',
                description: "Deploy app to Prod only ")
    }

    tools{
        maven 'mvn-3.8.8'
        jdk 'jdk-17'
    }

    environment{
        Pom_Version = readMavenPom().getVersion()
        Pom_Packaging = readMavenPom().getPackaging()
        Docker_Hub = "docker.io/aadil08"
        Docker_Creds = credentials('docker_creds')
        Application_Name = "${pipelineParams.appName}"
        GKE_Dev_Cluster_Name = "cluster-2"
        GKE_Dev_Region= "us-central1"
        K8S_Dev_File = "k8s_dev.yaml"
        DEV_Namespace = "dev-aadil-ns"
    }

    stages{

        stage ('Build'){
            when {
                anyOf{
                    expression {
                        params.buildOnly == "yes"
                    }
                }
            }

            // This will takee care of building the application
            steps{
                script{
                    buildApp().call()
                }
            }
        }

        stage ('Docker Build & Push') {
             when {
                anyOf{
                    expression {
                        params.dockerPush == "yes"
                    }
                }
            }
            steps{
                script{
               dockerBuildandPush().call()
                }
            }
        }

        stage("K8 Auth Login Stage"){
            steps{
                script{
                    echo "--------------------- Executing Login  Stage ----------------------"
                     d.buildApp("${env.Application_Name}")
                     d.auth_login("${env.GKE_Dev_Cluster_Name}","${env.GKE_Dev_Region}")
                }
            }
        }

        stage ('Deploy to Dev'){
           when {
                anyOf{
                    expression {
                        params.deployToDev == "yes"
                    }
                }
            }
            steps {
                script{
                     echo "--------------------- Executing Pre Deploy  Stage ----------------------"
                    imageValidation().call()
                     echo "--------------------- Executing Deploy to dev  Stage ----------------------"
                    d.auth_login("${env.GKE_Dev_Cluster_Name}","${env.GKE_Dev_Region}")
                    d.deployinK8("${env.K8S_Dev_File}","${env.DEV_Namespace}")
                }
            }
        }




    }

    }
}


    def buildApp(){
    return {
        echo "Building ${env.Application_Name} Application"
        // build using maven
        sh 'mvn clean package -DskipTests=true'
        archiveArtifacts artifacts: 'target/*.jar'
    }
}

def dockerBuildandPush(){
    return{
        echo "Starting Docker Build "
        echo "Copy the jar to the folder where Docker file is present"
        sh "cp ${WORKSPACE}/target/i27-${env.Application_Name}-${env.Pom_Version}.${env.Pom_Packaging} ./.cicd/"
        echo "********************* Building Docker Image ********************"
        sh "docker build --force-rm  --no-cache --build-arg JAR_SRC=i27-${env.Application_Name}-${env.Pom_Version}.${env.Pom_Packaging}  -t ${env.Docker_Hub}/${env.Application_Name}:${GIT_COMMIT} ./.cicd"
        echo "********************* Login to Docker Repo ********************"
        sh "docker login -u ${Docker_Creds_USR} -p ${Docker_Creds_PSW}"
        echo "********************* Docker Push ********************"
        sh "docker push ${env.Docker_Hub}/${env.Application_Name}:${GIT_COMMIT}"
    }
}

def imageValidation(){
    return{
        println ("Pulling the docker image")
        try{
            sh "docker pull  ${env.Docker_Hub}/${env.Application_Name}:${GIT_COMMIT}"
        }
        catch (Exception e){
            println("Docker image with this tag doesnt exist, so creating the image")
            buildApp().call()
            dockerBuildandPush().call()
        }
    }
}


