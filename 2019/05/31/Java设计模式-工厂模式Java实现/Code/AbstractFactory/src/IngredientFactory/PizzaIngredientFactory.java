package IngredientFactory;

import Ingredient.Cheese.Cheese;
import Dough;
import Ingredient.Dough.Dough;
import Ingredient.Sauce.Sauce;
import Ingredient.Veggies.Veggies;

public interface PizzaIngredientFactory {

    Dough createDough();
    Sauce createSauce();
    Cheese createCheese();
    Veggies[] createVeggies();
}