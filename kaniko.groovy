import com.cloudbees.plugins.flow.*;
import org.jenkinsci.plugins.gitclient.Git;
import org.jenkinsci.plugins.gitclient.GitClient;
import  hudson.plugins.promoted_builds.*;
import org.jvnet.hudson.plugins.groovypostbuild.*;
import org.jenkinsci.plugins.scriptsecurity.scripts.*;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.*;
import hudson.model.RootAction;
import org.jvnet.hudson.plugins.groovypostbuild.*;

def label = "kaniko-${UUID.randomUUID().toString()}"

podTemplate(name: 'kaniko', label: label, yaml: """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: jenkins-docker-cfg
        mountPath: /root
  volumes:
  - name: jenkins-docker-cfg
    projected:
      sources:
      - secret:
          name: regcred
          items:
            - key: .dockerconfigjson
              path: .docker/config.json
"""
  ) {

  node(label) {
    stage('Build with Kaniko') {
      git 'https://github.com/ab1234567/hellonode.git'
      container(name: 'kaniko', shell: '/busybox/sh') {
        withEnv(['PATH+EXTRA=/busybox']) {
          sh '''#!/busybox/sh
          /kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure-skip-tls-verify --cache=true --destination=mydockerregistry:5000/myorg/myimage
          '''
        }
      }
    }
  }
}
