package com.example.demo.patterns.proxy;

public class ProxyProject implements Project {

    private final String url;
    private RealProject realProject;

    public ProxyProject(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        if (realProject == null) {
            realProject = new RealProject(url);
        }

        load();
        realProject.run();
    }

    private void load() {
        System.out.println("load " + url);
    }
}
