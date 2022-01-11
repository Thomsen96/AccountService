pipeline {
    agent any

    stages {
        stage('Setup server') {
            steps {
                sh 'docker rm -f dtu_server_cont'
                sh 'docker rm -f dtu_client_cont'
            }
        }
        stage('Build server') {
            steps {
                sh 'docker build --tag dtupay ./Server'
            }
        }
        stage('Build client') {
            steps {
                sh 'docker build --tag dtupay_client ./Client'
            }
        }
        stage('Start Server') {
            steps {
                echo 'starting up dtupay docker'
                sh'docker run --name dtu_server_cont --network=deploy_app_network -d dtupay:latest'
                //sleep 2
            }
        }
        stage('Test') {
            steps {
                echo 'starting up test'
                sh 'docker run --name dtu_client_cont --network=deploy_app_network --entrypoint /home/app/run.sh dtupay_client:latest'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
        stage('Clean up') {
            steps {
                sh 'docker rm -f dtu_server_cont'
                sh 'docker rm -f dtu_client_cont'
            }
        }
    }
}