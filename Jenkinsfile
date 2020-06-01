pipeline {
    agent any

    stages {
        stage ('build') {
            script {
                try {
                    sh '.gradlew build --no-daemon'
                } finally {

                }
            }
        }
    }

}