pipeline {
    agent any

    stages {
        stage ('build') {
            sh '.gradlew build --no-daemon'
        }
    }

}