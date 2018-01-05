package org.patternomicon.infracode

trait Infrastructure {
    private Set environments = []
    def addEnvironment(Environment environment) {environments << environment}
    def getEnvironments() {environments}
}
