package com.foo.utils

public void Kaniko(body) {
  podTemplate(label: label,
        containers: [containerTemplate(name: 'Kaniko', image: 'gcr.io/kaniko-project/executor:debug', ttyEnabled: true, command: '/busybox/cat')],
        volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]) {
    body(
         {

    node(label) {
        stage('Kaniko') {
            git 'https://github.com/ab1234567/hellonode.git'
            container('Kaniko') {
                stage('Build a project') {
                    sh /kaniko/executor -f ./Dockerfile        
                }
            }
        }
    }
}
    )
}
}
return this
