package org.patternomicon.infracode.paas.docker

import org.patternomicon.infracode.Component
import org.patternomicon.infracode.ComponentInstance
import org.patternomicon.infracode.paas.docker.model.Container
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import spock.lang.*

@Singleton
class NginxComponent implements Component {
    UUID uuid = UUID.randomUUID()
    String source = "https://raw.githubusercontent.com/patternstorm/infracode/master/src/test/resources/Dockerfile"
    String name = "nginx"
}

@Singleton
class ComponentWithNoDockerfile implements Component {
    UUID uuid = UUID.randomUUID()
    String name = "noimage"
    String source = ""
}


class ServiceSpec extends Specification {
    def url = "http://localhost:2376/"


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
        Component noimage = ComponentWithNoDockerfile.instance
        when:
        docker.createImage(noimage)
        then:
        def error = thrown(Error)
        error.message == "Cannot locate specified Dockerfile: Dockerfile"
    }

    def "successful request to build a component's docker image"() {
        given:
        def docker = new Service(url)
        Component nginx = NginxComponent.instance
        when:
        docker.createImage(nginx)
        Image image = docker.getImage(nginx)
        then:
        image.RepoTags[0] == nginx.getTag()
    }

    def "succesful request to create a component's instance"() {
        given:
        def docker = new Service(url)
        Component nginx = NginxComponent.instance
        when:
        ComponentInstance nginxproc = docker.createContainer(nginx)
        Container container = docker.getContainer(nginxproc)
        then:
        container.Config.Image == nginxproc.component.getTag()
        container.Id == nginxproc.id
    }
}
