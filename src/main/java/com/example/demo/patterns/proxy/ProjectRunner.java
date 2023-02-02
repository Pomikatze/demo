package com.example.demo.patterns.proxy;

public class ProjectRunner {

    public static void main(String[] args) {
        Project project = new ProxyProject("http://test.ru");
        project.run();
    }
}
