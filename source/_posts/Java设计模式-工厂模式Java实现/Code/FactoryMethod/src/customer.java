import Pizza.Pizza;
import Store.ChicagoPizzaStore;
import Store.NYPizzaStore;
import Store.PizzaStore;

/**
 * 模拟顾客购买pizza
 */
public class customer {
    public static void main(String[] args) {
        /**
         * 开两家店
         */
        PizzaStore nyStore = new NYPizzaStore();
        PizzaStore chicagoStore = new ChicagoPizzaStore();

        /**
         * 纽约式CheesePizza
         */
        Pizza pizza = nyStore.orderPizza("cheese");
        System.out.println("Ethan ordered a " + pizza.getName() + "\n");

        /**
         * 购买芝加哥式CheesePizza
         */
        pizza = chicagoStore.orderPizza("cheese");
        System.out.println("Joel ordered a " + pizza.getName() + "\n");

    }
}
