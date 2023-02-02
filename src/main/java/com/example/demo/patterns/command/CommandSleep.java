package com.example.demo.patterns.command;

public class CommandSleep implements Command {

    private final Worker worker;

    public CommandSleep(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void doCommand() {
        worker.doSleep();
    }
}
