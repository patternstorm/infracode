package org.patternomicon.infracode.paas.docker

import org.patternomicon.infracode.Component
import spock.lang.*

class ClientSpec extends Specification {
    def url = "http://localhost:2376/"

    def "get the docker version"() {
        given:
        def docker = new Client(url)
        when:
        Version version = docker.getVersion()
        then:
        version.Os == "linux"
    }

    def "build a component's docker image"() {
        given:
        def docker = new Client(url)
        def dockerFile = "https://raw.githubusercontent.com/patternstorm/infracode/master/src/test/resources/Dockerfile"
        Component nginx = new Component(name: "nginx", source: dockerFile)
        when:
        docker.createImage(nginx)
        Image image = docker.getImage(nginx)
        then:
        image.RepoTags[0] == nginx.getTag()
    }
}
