pipeline {
  agent any

  options {
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
  }

  environment {
    COMPOSE_PROJECT_NAME = 'seleniumcucumberbddframework'
    BROWSER = 'chrome'
    PARALLEL_THREADS = '3'
    EXPECTED_GRID_NODES = '1'
    HEADLESS = 'true'
    MAVEN_PROFILE = 'testng-runner'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Run Grid + TestNG') {
      steps {
        sh '''
          set -euxo pipefail
          docker-compose --profile tests up --build --abort-on-container-exit --exit-code-from test-runner test-runner
        '''
      }
    }
  }

  post {
    always {
      sh '''
        set +e
        docker-compose --profile tests down --remove-orphans
      '''

      archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true

      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml,target/cucumber-results/junit/test-report.xml,target/cucumber-results/junit/*.xml'
    }
  }
}
