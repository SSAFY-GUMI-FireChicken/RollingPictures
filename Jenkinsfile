pipeline {
    agent any
    stages {
        // 2. gradle을 docker agent로 실행해 소스를 build합니다.
        stage('Git clonning and Build') {
            steps {
                echo 'Git Clonning...'
                git url: 'http://lab.ssafy.com/seung7642/jenkins-test',
                    credentialsId: 'jenkins-credentials-id'

                sh 'ls -al'
                dir('RollingPictures-Backend/') {
                    sh './gradlew cleanQuerydslSourcesDir'
                    sh './gradlew bootJar'
                }
            }
            post {
                success {
                    echo 'Successfully Cloned Repository And Spring Boot App Build'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        // 3. 도커 이미지를 빌드합니다. (Dockerfile 필요)
        stage('Docker build') {
            steps {
                echo 'Docker building...'
                sh 'docker build -t rolling-pictures:latest RollingPictures-Backend/'
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        // 4. 기존 컨테이너와 이미지를 지우고 새로운 컨테이너를 실행합니다.
        stage('Docker run') {
            steps {
                echo 'Docker running...'
                sh 'docker ps -f name=rolling-pictures -q | xargs --no-run-if-empty docker container stop'
                sh 'docker container ls -a --filter ancestor=rolling-pictures --filter status=exited -q | xargs -r docker container rm'
                sh 'docker images --no-trunc -a -q --filter="dangling=true" | xargs --no-run-if-empty docker rmi'
                sh 'docker run -d -p 8185:8185 --name rolling-pictures rolling-pictures:latest'
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
    }
}
