package org.patternomicon.infracode.paas.docker.model


class ContainerDefinition {
    String Image
}

class ContainerHandle {
    String Id
}

class Container {

    class Config {
        String Image
    }

    Config Config
    String Name
    String Id
}
