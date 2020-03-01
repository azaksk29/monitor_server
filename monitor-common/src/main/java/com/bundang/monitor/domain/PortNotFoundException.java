package com.bundang.monitor.domain;

public class PortNotFoundException extends RuntimeException {
    public PortNotFoundException(Long id) {
        super("Could not find port " + id);
    }
}
