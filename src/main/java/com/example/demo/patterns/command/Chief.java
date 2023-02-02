package com.example.demo.patterns.command;

public class Chief {

    private final Command commandSleep;
    private final Command commandWork;

    public Chief(Command commandSleep, Command commandWork) {
        this.commandSleep = commandSleep;
        this.commandWork = commandWork;
    }

    public void executeCommandSleep() {
        commandSleep.doCommand();
    }

    public void executeCommandWork() {
        commandWork.doCommand();
    }
}
