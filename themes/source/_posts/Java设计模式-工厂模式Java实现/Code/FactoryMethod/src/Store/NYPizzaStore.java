package Store;

import Pizza.NYStyle.*;
import Pizza.Pizza;

/**
 * 纽约Pizza店
 */
public class NYPizzaStore extends PizzaStore {
    @Override
    public Pizza createPizza(String item) {

        switch (item) {
            case "cheese":
                return new NYStyleCheesePizza();
            case "clam":
                return new NYStyleClamPizza();
            default:
                return null;
        }
    }
}
