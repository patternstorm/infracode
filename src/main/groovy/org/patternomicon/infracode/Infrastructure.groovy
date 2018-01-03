package org.patternomicon.infracode

class Infrastructure {
    Set environments = []
    def addEnvironment(Environment environment) {environments << environment}
    def getEnvironments() {environments}
}
