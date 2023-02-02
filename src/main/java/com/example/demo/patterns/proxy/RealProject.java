package com.example.demo.patterns.proxy;

public class RealProject implements Project {

    private final String url;

    public RealProject(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        System.out.println("start " + url);
    }
}
