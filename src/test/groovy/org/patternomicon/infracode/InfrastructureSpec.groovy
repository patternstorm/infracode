package org.patternomicon.infracode

import spock.lang.*

class InfrastructureSpec extends Specification {

    @Singleton
    class AnInfrastructure implements Infrastructure {

    }

    @Singleton
    class AnEnvironment implements Environment {}

    def "an infrastructure is created with no environments" () {
        given:
        Infrastructure infra = AnInfrastructure.instance

        when:
        def environments = infra.getEnvironments()

        then:
        environments.size() == 0
    }

    def "add a new environment to an infrastucture" () {
        given:
        Infrastructure infra = AnInfrastructure.instance
        Environment dev = AnEnvironment.instance

        when:
        infra.addEnvironment(dev)

        then:
        infra.getEnvironments().contains(dev)
    }

    def "an environment cannot be added twice to an infrastucture" () {
        given:
        Infrastructure infra = AnInfrastructure.instance
        Environment dev = AnEnvironment.instance
        infra.addEnvironment(dev)

        when:
        infra.addEnvironment(dev)

        then:
        infra.getEnvironments().contains(dev)
        infra.getEnvironments().size() == 1
    }
}
