package com.example.demo.patterns.abstractFactory;

public class OrderCoffeeForm {

    private final Button orderButton;

    public OrderCoffeeForm(GUIFactory factory) {
        orderButton = factory.createButton();
    }

    public Button getOrderButton() {
        return orderButton;
    }
}
