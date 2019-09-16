package IngredientFactory;

import Ingredient.Cheese.*;
import Ingredient.Dough.*;
import Ingredient.Sauce.*;
import Ingredient.Veggies.*;

public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
    public Dough createDough() {
        return new ThinCrustDough();
    }

    public Sauce createSauce() {
        return new MarinaraSauce();
    }

    public Cheese createCheese() {
        return new ReggianoCheese();
    }

    public Veggies[] createVeggies() {
        Veggies veggies[] = {new Spinach(), new Eggplant()};
        return veggies;
    }
}
