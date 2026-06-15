// Java 25+
// Feature shown: compact source files and instance main methods, final in Java 25+
// Feature shown: Java Platform Module System — listing loaded modules via
//   ModuleLayer, final in Java 9+
// Place this class in a named module on the module path.
// Run from the command line: java --list-modules
//
// Selected output (Java 21):
//   java.base@21       java.net.http@21
//   java.desktop@21    java.sql@21
//   java.logging@21    java.xml@21
//   jdk.compiler@21    jdk.jlink@21
//
// Every platform module has explicit dependencies:
//   java.sql     requires java.logging, java.xml
//   java.desktop requires java.datatransfer, java.prefs
package com.domain.example;

import java.util.logging.Logger;

public class ListPlatformModules {

    private static final Logger log =
            Logger.getLogger(ListPlatformModules.class.getName());

    void main() {
        ModuleLayer.boot()
            .modules()
            .stream()
            .map(Module::getName)
            .sorted()
            .forEach(name -> log.info("Module: " + name));
    }
}
// Output: INFO: Module: java.base
//         INFO: Module: java.desktop
//         INFO: Module: java.logging
//         ...
