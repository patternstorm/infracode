package org.patternomicon.infracode.paas.docker

import spock.lang.*

class ClientSpec extends Specification {
    def url = "http://localhost:2376/"

    def "get the docker version"() {
        given:
        def docker = new Client(url)
        when:
        def version = docker.getVersion()
        then:
        version.Os == "linux"
    }
}
