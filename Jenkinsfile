pipeline {
    agent any

    // 1. Pipeline Parameters
    parameters {
        booleanParam(
            name: 'IS_TEST_MODE',
            defaultValue: false,
            description: 'Check to build the application in Test Environment mode.'
        )
    }

    // 2. Global Environment Variables
    environment {
        KEYSTORE_FILE = "C:/Users/Dell/Documents/my_key.jks"
    }

    // 3. Execution Stages
    stages {
        stage('Checkout Code') {
            steps {
                echo '--------------------------------------------------'
                echo 'Fetching the latest source code from GitHub...'
                echo '--------------------------------------------------'
                checkout scm
            }
        }

        stage('Clean Project') {
            steps {
                echo '--------------------------------------------------'
                echo 'Cleaning up the old build directory...'
                echo '--------------------------------------------------'
                bat 'gradlew.bat clean'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo '--------------------------------------------------'
                echo 'Executing automated unit tests...'
                echo '--------------------------------------------------'
                bat 'gradlew.bat test'
            }
        }

        stage('Build & Sign APK') {
            steps {
                echo '--------------------------------------------------'
                echo "Compiling and packaging APK. IS_TEST_MODE = ${params.IS_TEST_MODE}"
                echo '--------------------------------------------------'
                bat "gradlew.bat assembleRelease -PIS_TEST_MODE=${params.IS_TEST_MODE}"
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo '--------------------------------------------------'
                echo 'Build successful! Archiving the generated APK files...'
                echo '--------------------------------------------------'
                archiveArtifacts artifacts: '**/build/outputs/apk/release/*.apk', fingerprint: true
            }
        }
    }

    // 4. Post-build Actions
    post {
        success {
            echo '=================================================='
            echo ' SUCCESS: The build pipeline completed successfully!'
            echo '=================================================='
        }
        failure {
            echo '=================================================='
            echo ' FAILURE: The build pipeline failed. Please check the logs!'
            echo '=================================================='
        }
    }
}