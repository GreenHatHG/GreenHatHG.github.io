package Store;

import Pizza.ChicagoStyle.*;
import Pizza.Pizza;

/**
 * 芝加哥Pizza店
 */
public class ChicagoPizzaStore extends PizzaStore {
    @Override
    public Pizza createPizza(String item) {

        switch (item) {
            case "cheese":
                return new ChicagoStyleCheesePizza();
            case "clam":
                return new ChicagoStyleClamPizza();
            default:
                return null;
        }

    }
}