package com.example.demo.patterns.adapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Adapter implements Enemy {

    private final SpecialEnemy specialEnemy;

    @Override
    public String attack() {
        return specialEnemy.spell();
    }
}
