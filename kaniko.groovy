def label = "mypod-${UUID.randomUUID().toString()}"
podTemplate(label: label, containers: [
    containerTemplate(name: 'Kaniko', image: 'gcr.io/kaniko-project/executor:debug', ttyEnabled: true, command: '/busybox/cat'),
    containerTemplate(name: 'Build', image: 'golang:1.8.0', ttyEnabled: true, command: 'cat')
  ]) {

    node(label) {
        stage('Get a Maven project') {
            git 'https://github.com/ab1234567/hellonode.git'
            container('Kaniko') {
                stage('Build a project') {
                    sh """
                    /kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure-skip-tls-verify --cache=true
                    """
                }
            }
        }

    }
}
