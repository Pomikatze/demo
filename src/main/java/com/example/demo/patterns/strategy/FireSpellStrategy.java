package com.example.demo.patterns.strategy;

public class FireSpellStrategy implements SpellStrategy {

    @Override
    public void doSpell() {
        System.out.println("Огненный шар!");
    }
}
