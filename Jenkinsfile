pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = credentials('DOCKER_REGISTRY')
//         GITHUB_USERNAME = credentials('GITHUB_USERNAME')
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build & Unit Tests') {
            parallel {
                stage('Account Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :account-service:clean :account-service:build'
                        }
                    }
                }
                stage('Blocker Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :blocker-service:clean :blocker-service:build'
                        }
                    }
                }
                stage('Cash Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :cash-service:clean :cash-service:build'
                        }
                    }
                }
                stage('Exchange-generator Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :exchange-generator-service:clean :exchange-generator-service:build'
                        }
                    }
                }
                stage('Exchange Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :exchange-service:clean :exchange-service:build'
                        }
                    }
                }
                stage('Front Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :front-service:clean :front-service:build'
                        }
                    }
                }
                stage('Notification Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :notification-service:clean :notification-service:build'
                        }
                    }
                }
                stage('Transfer Service') {
                    steps {
                        dir('.') {
                            sh 'gradle :transfer-service:clean :transfer-service:build'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh """
                docker build -t ${DOCKER_REGISTRY}/front-service:${IMAGE_TAG} -f ./front-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/account-service:${IMAGE_TAG} -f ./account-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/blocker-service:${IMAGE_TAG} -f ./blocker-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/exchange-service:${IMAGE_TAG} -f ./exchange-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/notification-service:${IMAGE_TAG} -f ./notification-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/transfer-service:${IMAGE_TAG} -f ./transfer-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/cash-service:${IMAGE_TAG} -f ./cash-service/Dockerfile .
                docker build -t ${DOCKER_REGISTRY}/exchange-generator-service:${IMAGE_TAG} -f ./exchange-generator-service/Dockerfile .
                """
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([string(credentialsId: 'GHCR_TOKEN', variable: 'GHCR_TOKEN')]) {
                    sh """
                    echo \$GHCR_TOKEN | docker login ghcr.io -u ${GITHUB_USERNAME} --password-stdin
                    docker push ${DOCKER_REGISTRY}/front-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/account-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/blocker-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/exchange-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/notification-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/transfer-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/cash-service:${IMAGE_TAG}
                    docker push ${DOCKER_REGISTRY}/exchange-generator-service:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('Helm Deploy to TEST') {
            steps {
                sh """
                helm upgrade --install bankapp bankapp-chart \\
                  --namespace test --create-namespace \\
                  --set account-service.image.repository=${DOCKER_REGISTRY}/account-service \\
                  --set account-service.image.tag=${IMAGE_TAG} \\
                  --set blocker-service.image.repository=${DOCKER_REGISTRY}/blocker-service \\
                  --set blocker-service.image.tag=${IMAGE_TAG} \\
                  --set cash-service.image.repository=${DOCKER_REGISTRY}/cash-service \\
                  --set cash-service.image.tag=${IMAGE_TAG} \\
                  --set exchange-generator-service.image.repository=${DOCKER_REGISTRY}/exchange-generator-service \\
                  --set exchange-generator-service.image.tag=${IMAGE_TAG} \\
                  --set exchange-service.image.repository=${DOCKER_REGISTRY}/exchange-service \\
                  --set exchange-service.image.tag=${IMAGE_TAG} \\
                  --set notification-service.image.repository=${DOCKER_REGISTRY}/notification-service \\
                  --set notification-service.image.tag=${IMAGE_TAG} \\
                  --set transfer-service.image.repository=${DOCKER_REGISTRY}/transfer-service \\
                  --set transfer-service.image.tag=${IMAGE_TAG} \\
                  --set front-service.image.repository=${DOCKER_REGISTRY}/front-service \\
                  --set front-service.image.tag=${IMAGE_TAG} \\
                """
            }
        }

        stage('Manual Approval for PROD') {
            steps {
                input message: 'Deploy to PROD environment?', ok: 'Yes, deploy'
            }
        }

        stage('Helm Deploy to PROD') {
            steps {
                sh """
                helm upgrade --install bankapp bankapp-chart \\
                  --namespace prod --create-namespace \\
                  --set account-service.image.repository=${DOCKER_REGISTRY}/account-service \\
                  --set account-service.image.tag=${IMAGE_TAG} \\
                  --set blocker-service.image.repository=${DOCKER_REGISTRY}/blocker-service \\
                  --set blocker-service.image.tag=${IMAGE_TAG} \\
                  --set cash-service.image.repository=${DOCKER_REGISTRY}/cash-service \\
                  --set cash-service.image.tag=${IMAGE_TAG} \\
                  --set exchange-generator-service.image.repository=${DOCKER_REGISTRY}/exchange-generator-service \\
                  --set exchange-generator-service.image.tag=${IMAGE_TAG} \\
                  --set exchange-service.image.repository=${DOCKER_REGISTRY}/exchange-service \\
                  --set exchange-service.image.tag=${IMAGE_TAG} \\
                  --set notification-service.image.repository=${DOCKER_REGISTRY}/notification-service \\
                  --set notification-service.image.tag=${IMAGE_TAG} \\
                  --set transfer-service.image.repository=${DOCKER_REGISTRY}/transfer-service \\
                  --set transfer-service.image.tag=${IMAGE_TAG} \\
                  --set front-service.image.repository=${DOCKER_REGISTRY}/front-service \\
                  --set front-service.image.tag=${IMAGE_TAG} \\
                """
            }
        }
    }
}