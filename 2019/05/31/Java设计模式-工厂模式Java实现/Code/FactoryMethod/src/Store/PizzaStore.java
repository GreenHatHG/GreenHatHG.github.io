package Store;

import Pizza.Pizza;

public abstract class PizzaStore {
    /**
     * 制作item类型的Pizza
     * @param item 需要item类的Pizza
     * @return Pizza
     */
    public abstract Pizza createPizza(String item);

    public Pizza orderPizza(String type){
        Pizza pizza = createPizza(type);
        System.out.println("--- Making a " + pizza.getName() + " ---");
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }
}
