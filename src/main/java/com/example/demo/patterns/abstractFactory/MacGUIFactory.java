package com.example.demo.patterns.abstractFactory;

public class MacGUIFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new MacButton();
    }
}
