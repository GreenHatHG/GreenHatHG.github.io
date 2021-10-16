package Store;

import Factory.SimplePizzaFactory;
import pizza.Pizza;

/**
 * Pizza店，用于订购Pizza
 */
public class PizzaStore {
    SimplePizzaFactory factory;

    public PizzaStore(SimplePizzaFactory factory) {
        this.factory = factory;
    }

    public Pizza orderPizza(String type) {
        Pizza pizza;

        /**
         * 工厂制作处type类型的Pizza
         */
        pizza = factory.createPizza(type);

        /**
         * 烘焙，切片，打包等
         */
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();

        return pizza;
    }

}