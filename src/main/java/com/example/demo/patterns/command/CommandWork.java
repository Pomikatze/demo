package com.example.demo.patterns.command;

public class CommandWork implements Command {

    private final Worker worker;

    public CommandWork(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void doCommand() {
        worker.doCommand();
    }
}
