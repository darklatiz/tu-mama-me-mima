pipeline {
    agent any

    options {
        ansiColor('xterm')
    }

    environment {
        SONAR_PROJECT_KEY = "TuMamaMeMima"
        GITHUB_TOKEN = credentials('github-token')
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

        stage('Build') {
            steps {
                echo "🛠️ Building the project with Maven..."
                sh 'mvn clean install -DskipTests'
                script {
                    publishChecks name: 'Building', summary: 'Building', text: 'The Text', title: 'Builder Building'
                }
            }
        }

        stage('Testing') {
            steps {
                echo "🧪 Running unit and integration tests..."
                sh 'mvn test'
                script {
                    publishChecks name: 'Testing', summary: 'Unit Testing', text: 'The Text', title: 'Unit testing'
                }
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
                echo "📊 Running SonarQube analysis..."
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.dependencyCheck.summarize=true \
                        -Dsonar.dependencyCheck.jsonReportPath=target/dependency-check-report.json \
                        -Dsonar.dependencyCheck.xmlReportPath=target/dependency-check-report.xml \
                        -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                error "🚨 SonarQube Quality Gate failed! Build stopped."
                            }
                        }
                    }
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
