package Store;

import IngredientFactory.ChicagoPizzaIngredientFactory;
import IngredientFactory.PizzaIngredientFactory;
import Pizza.*;

/**
 * 芝加哥Pizza店
 */
public class ChicagoPizzaStore extends PizzaStore {

    @Override
    protected Pizza createPizza(String item) {
        Pizza pizza = null;
        PizzaIngredientFactory ingredientFactory = new ChicagoPizzaIngredientFactory();

        /**
         * 制作item类型的Pizza
         */
        switch (item){
            case "cheese":
                pizza = new CheesePizza(ingredientFactory);
                pizza.setName("Chicago Style Cheese Pizza");
                break;
            case "clam":
                pizza = new ClamPizza(ingredientFactory);
                pizza.setName("Chicago Style Clam Pizza");
                break;
        }

        return pizza;
    }
}
