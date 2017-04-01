node {
  // Mark the code checkout 'stage'....
  stage 'Checkout'

  checkout scm

  stage 'Stage Build'

  //branch name from Jenkins environment variables
  echo "My branch is: ${env.BRANCH_NAME}"

  sh "./gradlew clean -PBUILD_NUMBER=${env.BUILD_NUMBER} build"

  stage 'Stage Archive'
  //tell Jenkins to archive the apks
  archiveArtifacts artifacts: 'build/libs/*-SNAPSHOT.jar', fingerprint: true

  stage 'Deploy to dev env'
  sh "./gradlew deploy -PBUILD_NUMBER=${env.BUILD_NUMBER} -PENV=dev"
}
