package com.example.demo.patterns.strategy;

public abstract class Wizard {

    SpellStrategy spellStrategy;

    public abstract void attack();

    void spell() {
        spellStrategy.doSpell();
    }
}
