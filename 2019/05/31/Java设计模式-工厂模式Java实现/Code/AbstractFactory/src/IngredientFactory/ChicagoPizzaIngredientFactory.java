package IngredientFactory;

import Ingredient.Cheese.*;
import Ingredient.Dough.*;
import Ingredient.Sauce.*;
import Ingredient.Veggies.*;

/**
 * 芝加哥原料工厂
 */
public class ChicagoPizzaIngredientFactory implements PizzaIngredientFactory {
    @Override
    public Dough createDough() {
        return new ThickCrustDough();
    }

    @Override
    public Sauce createSauce() {
        return new PlumTomatoSauce();
    }

    @Override
    public Cheese createCheese() {
        return new MozzarellaCheese();
    }

    @Override
    public Veggies[] createVeggies() {
        Veggies[] veggies = {
                new BlackOlives(),
                new Spinach(),
                new Eggplant()
        };
        return veggies;
    }

}
