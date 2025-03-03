pipeline {
    agent any

    options {
        ansiColor('xterm')
    }

    environment {
        SONAR_PROJECT_KEY = "TuMamaMeMima"
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
                echo "📊 Running NEW Super SonarQube analysis..."
                withSonarQubeEnv('SonarQube') {
                    sonarqubeScanner(
                        installationName: 'SonarQube',
                        task: [
                            additionalProperties: [
                                'sonar.projectKey': SONAR_PROJECT_KEY,
                                'sonar.dependencyCheck.summarize': true,
                                'sonar.dependencyCheck.jsonReportPath': 'target/dependency-check-report.json',
                                'sonar.dependencyCheck.xmlReportPath': 'target/dependency-check-report.xml',
                                'sonar.dependencyCheck.htmlReportPath': 'target/dependency-check-report.html',
                                'sonar.branch.name': "PR-${env.CHANGE_ID}"
                            ]
                        ]
                    )
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
