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
                    sh /kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure-skip-tls-verify --cache=true        
                }
            }
        }
    }
}
    )
}
}

public void PushtoACR(body) {
  podTemplate(label: label,
        containers: [containerTemplate(name: 'maven', image: 'maven', command: 'cat', ttyEnabled: true)],
        volumes: [secretVolume(secretName: 'maven-settings', mountPath: '/root/.m2'),
                  persistentVolumeClaim(claimName: 'maven-local-repo', mountPath: '/root/.m2nrepo')]) {
    body()
}
}

return this
