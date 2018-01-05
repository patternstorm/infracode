package org.patternomicon.infracode

trait Component {
    abstract UUID getUuid()
    abstract String getName()
    abstract String getSource()
    String getTag() {getName() +":"+ getUuid()}
}
