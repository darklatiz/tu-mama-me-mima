pipeline {
    agent any

    options {
        ansiColor('xterm')
    }

    environment {
        SONAR_PROJECT_KEY = "TuMamaMeMima"
        SONAR_HOST_URL = "http://20.42.91.75:9000"
        SONAR_TOKEN = credentials('sonar-token') // Use Jenkins credentials
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Check PR or Branch') {
            steps {
                script {
                    if (env.CHANGE_ID) {
                        echo "🔄 This is a Pull Request Build for PR #${env.CHANGE_ID}."
                    } else {
                        echo "🌿 This is a branch build for ${env.BRANCH_NAME}."
                    }
                }
            }
        }

        stage('Build & Test') {
            steps {
                echo "🛠️ Building the project with Maven..."
                sh 'mvn clean install'
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                echo "🔍 Running OWASP Dependency-Check..."
                sh '''
                    mvn org.owasp:dependency-check-maven:aggregate \
                        -DfailBuildOnCVSS=7 \
                        -Dformats=XML,JSON,HTML
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "📊 Running Super SonarQube analysis..."
                sh """
                    mvn -B sonar:sonar \
                        -Dsonar.dependencyCheck.summarize=true \
                        -Dsonar.dependencyCheck.jsonReportPath=target/dependency-check-report.json \
                        -Dsonar.dependencyCheck.xmlReportPath=target/dependency-check-report.xml \
                        -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_TOKEN}
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline executed successfully!"
        }
        failure {
            echo "❌ Build failed! Check the logs."
        }
    }
}
