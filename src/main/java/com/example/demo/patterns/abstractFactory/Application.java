package com.example.demo.patterns.abstractFactory;

public class Application {

    private OrderCoffeeForm orderCoffeeForm;

    public void drawOrderCoffeeForm() {
        String osName = System.getProperty("os.name").toLowerCase();
        GUIFactory guiFactory;

        if (osName.startsWith("win")) {
            guiFactory = new WindowsGUIFactory();
        } else if (osName.startsWith("mac")) {
            guiFactory = new MacGUIFactory();
        } else {
            System.out.println("Unknown OS, can't draw form :(");
            return;
        }
        orderCoffeeForm = new OrderCoffeeForm(guiFactory);
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.drawOrderCoffeeForm();
    }

    public OrderCoffeeForm getOrderCoffeeForm() {
        return orderCoffeeForm;
    }

    public void setOrderCoffeeForm(OrderCoffeeForm orderCoffeeForm) {
        this.orderCoffeeForm = orderCoffeeForm;
    }
}
