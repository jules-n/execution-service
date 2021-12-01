pipeline {
    agent any
    parameters {
        booleanParam(name: 'incMinor', defaultValue: false, description: '')
        booleanParam(name: 'incMajor', defaultValue: false, description: '')
        booleanParam(name: 'incPatch', defaultValue: true, description: '')
        choice(name: 'helmValues', choices: ['prod', 'dev'], description: '')
    }
    environment {
        SERVICE_NAME = 'execution-service'
        GCP_PROJECT_ID = 'single-system-dev'
        SS_DEV_ARTIFACTORY_USERNAME = credentials("jenkins-artifactory-username")
        SS_DEV_ARTIFACTORY_PASSWORD = credentials("jenkins-artifactory-password")
        SERVICE_ACCOUNT = credentials("jenkins_gcp_service_account")
        GCP_CLUSTER = credentials("gcp_cluster")
        HELM_DIRECTORY = 'executionservice'
    }
    stages {
        stage('init') {
            steps {
                script {
                    sshagent(credentials: ['jenkins-dev-event-receiver-id-rsa']) {
                        sh(script: "git fetch --tags")
                        def CURRENT_GIT_TAG = sh script: './gradlew -q printVersion', returnStdout: true
                        env.SERVICE_VERSION = incrementVersion(CURRENT_GIT_TAG, params.incMajor, params.incMinor, params.incPatch)
                        sh(script: "git tag -a ${env.SERVICE_VERSION} -m ${env.SERVICE_VERSION}")
                        sh(script: "git push origin --tags")
                    }
                    echo "version: ${SERVICE_VERSION}"
                }
            }
        }
        stage('build') {
            steps {
                sh './gradlew build -x test -x integrationTest'
            }
        }
        stage('unit test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('integration test') {
            steps {
                sh './gradlew integrationTest'
            }
        }
        stage('image publish') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'jenkins-service-account-key-file', variable: 'GC_KEY')]) {
                        sh "docker login -u _json_key --password-stdin https://gcr.io < $GC_KEY"
                        sh "docker build -t gcr.io/${env.GCP_PROJECT_ID}/${env.SERVICE_NAME}:${env.SERVICE_VERSION} . "
                        sh "docker push gcr.io/${env.GCP_PROJECT_ID}/${env.SERVICE_NAME}:${env.SERVICE_VERSION}"
                    }
                }
            }
        }
        stage('deploy') {
            environment {
                GCP_ZONE = 'europe-central2-c'
            }
            steps {
                script {
                    withCredentials([file(credentialsId: 'jenkins-service-account-key-file', variable: 'GC_KEY')]) {
                        sh "gcloud auth activate-service-account ${env.SERVICE_ACCOUNT}" +
                                " --key-file=${GC_KEY} --project ${env.GCP_PROJECT_ID}"
                        sh "gcloud container clusters get-credentials ${env.GCP_CLUSTER} --zone ${GCP_ZONE} --project ${env.GCP_PROJECT_ID}"
                        sh "helm upgrade --install --set app.version=${env.SERVICE_VERSION} -f ./helm/${env.HELM_DIRECTORY}/values-${params.helmValues}.yaml ${env.HELM_DIRECTORY} ./helm/${env.HELM_DIRECTORY}"
                    }
                }
            }
        }
    }
    post {
        failure {
            sshagent(credentials: ['jenkins-dev-event-receiver-id-rsa']) {
                sh(script: "git tag -d ${env.SERVICE_VERSION} || true")
                sh(script: "git push --delete origin ${env.SERVICE_VERSION} || true")
            }
        }
    }
}

def incrementVersion(String version, boolean incMajor = false, boolean incMinor = false, boolean incPatch = true) {
    def parts = version.split('\\.')
    def major = parts[0] as int
    def minor = parts[1] as int
    def patch = parts[2] as int
    incMajor ? "${major + 1}.0.0"
            : incMinor ? "${major}.${minor + 1}.0"
            : "${major}.${minor}.${patch + 1}"
}