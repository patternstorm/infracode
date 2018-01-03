package org.patternomicon.infracode

class Component {
    UUID uuid = UUID.randomUUID()
    String name
    String source
    String getTag() { name +":"+ uuid}
}
