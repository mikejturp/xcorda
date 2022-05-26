package com.xcorda.services.config;

public class Source {
    private final String name;
    private final String hostname;
    private final String port;
    private final String username;
    private final String password;

    public Source(String name, String hostname, String port, String username, String password) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
