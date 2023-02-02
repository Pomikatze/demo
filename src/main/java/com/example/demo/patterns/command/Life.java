package com.example.demo.patterns.command;

public class Life {

    public static void main(String[] args) {
        Worker worker = new Worker();

        Command commandWork = new CommandWork(worker);
        Command commandSleep = new CommandSleep(worker);

        Chief chief = new Chief(commandSleep, commandWork);

        chief.executeCommandSleep();
        chief.executeCommandWork();
    }
}
