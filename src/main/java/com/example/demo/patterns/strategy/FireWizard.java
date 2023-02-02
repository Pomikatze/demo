package com.example.demo.patterns.strategy;

public class FireWizard extends Wizard {

    public FireWizard() {
        this.spellStrategy = new FireSpellStrategy();
    }

    @Override
    public void attack() {
        System.out.println("Удар посохом!");
    }
}
