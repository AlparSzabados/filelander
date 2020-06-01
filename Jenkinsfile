pipeline {
    agent any

    stages {
        steps {
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

}