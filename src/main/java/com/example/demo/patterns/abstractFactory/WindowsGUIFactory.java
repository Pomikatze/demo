package com.example.demo.patterns.abstractFactory;

public class WindowsGUIFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }
}
