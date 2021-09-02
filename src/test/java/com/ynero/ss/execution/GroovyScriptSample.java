package com.ynero.ss.execution;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class GroovyScriptSample {

    @Test
    void runGroovyScriptFromJavaCode() {
        var shell = new GroovyShell();
        var binding = new Binding();
        var params = new HashMap<String, Object>();
        params.put("temperature", 39.9);
        params.put("threshold", 39.5);
        params.put("alert-message", "call ambulance!!!");
        binding.setVariable("params", params);

        var scriptStr = "" +
                "if (params['temperature'] > params['threshold']) {" +
                "  return ['result': params['alert-message']]" +
                "} else {" +
                "  return ['result': '']" +
                "}";

        var script = shell.parse(scriptStr);
        script.setBinding(binding);
        var result = (Map<String, Object>) script.run();

        System.out.println("result map = " + result);
    }
}
