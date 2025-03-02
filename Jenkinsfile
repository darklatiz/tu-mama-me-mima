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
        genericTrigger(
            causeString: 'Triggered by PR',
            genericVariables: [
                [key: 'ref', value: '$ref'],
                [key: 'event_name', value: '$event_name']
            ],
            token: 'my-secret-token',
            printContributedVariables: true,
            printPostContent: true
        )
    }

    stages {
        stage('Check PR') {
            when {
                expression { env.GIT_BRANCH ==~ /origin\/PR-.*/ }
            }
            steps {
                echo "🔄 This is a Pull Request Build..."
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
