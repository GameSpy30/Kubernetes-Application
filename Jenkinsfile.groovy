#Jenkins Pipeline #(CI + GitOps trigger)

pipeline {
  agent any

  environment {
    ECR = "<account-id>.dkr.ecr.ap-south-1.amazonaws.com/my-app"
  }

  stages {

    stage('Build Docker Image') {
      steps {
        sh 'docker build -t my-app .'
      }
    }

    stage('Push to ECR') {
      steps {
        sh '''
        aws ecr get-login-password | docker login --username AWS --password-stdin $ECR
        docker tag my-app:latest $ECR:latest
        docker push $ECR:latest
        '''
      }
    }

    stage('Update Helm Values') {
      steps {
        sh '''
        sed -i "s/tag:.*/tag: latest/" helm/my-app/values.yaml
        git commit -am "Update image tag"
        git push origin main
        '''
      }
    }
  }
}