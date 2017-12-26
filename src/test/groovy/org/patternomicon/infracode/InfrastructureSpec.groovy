package org.patternomicon.infracode

import spock.lang.*

class InfrastructureSpec extends Specification {

    class AnyEnvironment implements Environment {}

    def "an infrastructure is created with no environments" () {
        given:
        Infrastructure infra = new Infrastructure()

        when:
        def environments = infra.getEnvironments()

        then:
        environments.size() == 0
    }

    def "an infrastucture can be extended with a new environment" () {
        given:
        Infrastructure infra = new Infrastructure()
        Environment dev = new AnyEnvironment()

        when:
        infra.addEnvironment(dev)

        then:
        infra.getEnvironments().contains(dev)
    }

    def "an infrastucture cannot be extended with an environment it already contains" () {
        given:
        Infrastructure infra = new Infrastructure()
        Environment dev = new AnyEnvironment()
        infra.addEnvironment(dev)

        when:
        infra.addEnvironment(dev)

        then:
        infra.getEnvironments().contains(dev)
        infra.getEnvironments().size() == 1
    }
}
