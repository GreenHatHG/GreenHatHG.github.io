package Factory;

import pizza.CheesePizza;
import pizza.ClamPizza;
import pizza.Pizza;

/**
 * 简单工厂用于制作pizza
 */
public class SimplePizzaFactory {
    public Pizza createPizza(String type) {
        Pizza pizza = null;

        if (type.equals("cheese")) {
            pizza = new CheesePizza();
        } else if (type.equals("clam")) {
            pizza = new ClamPizza();
        }

        return pizza;
    }
}