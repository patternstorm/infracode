package org.patternomicon.infracode.clients

import spock.lang.*

class DockerClientSpec extends Specification {
    def url = "http://localhost:2376/"

    def "get the docker version"() {
        given:
        def docker = new DockerClient(url)
        when:
        def version = docker.getVersion()
        then:
        version.Os == "linux"
    }
}
