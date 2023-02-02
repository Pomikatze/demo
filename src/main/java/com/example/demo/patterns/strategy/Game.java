package com.example.demo.patterns.strategy;

public class Game {

    public static void main(String[] args) {
        Wizard wizard = new FireWizard();
        wizard.attack();
        wizard.spell();
    }
}
