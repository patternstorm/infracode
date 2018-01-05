package org.patternomicon.infracode.paas.docker

import org.patternomicon.infracode.Component
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import spock.lang.*

class ServiceSpec extends Specification {
    def url = "http://localhost:2376/"

    @Singleton
    class Nginx implements Component {
        UUID uuid = UUID.randomUUID()
        String source = "https://raw.githubusercontent.com/patternstorm/infracode/master/src/test/resources/Dockerfile"
        String name = "nginx"
    }

    @Singleton
    class NoImage implements Component {
        UUID uuid = UUID.randomUUID()
        String name = "noimage"
        String source = ""
    }

    def "successful request to get the docker version"() {
        given:
        def docker = new Service(url)
        when:
        Version version = docker.getVersion()
        then:
        version.Os == "linux"
    }

    def "failed request to build a component's docker image due to Dockerfile not found"() {
        given:
        def docker = new Service(url)
        Component noimage = NoImage.instance
        when:
        docker.createImage(noimage)
        then:
        def error = thrown(Error)
        error.message == "Cannot locate specified Dockerfile: Dockerfile"
    }

    def "successful request to build a component's docker image"() {
        given:
        def docker = new Service(url)
        Component nginx = Nginx.instance
        when:
        docker.createImage(nginx)
        Image image = docker.getImage(nginx)
        then:
        image.RepoTags[0] == nginx.getTag()
    }
}
