import Factory.SimplePizzaFactory;
import Store.PizzaStore;
import pizza.Pizza;

/**
 * 模拟顾客购买pizza
 */
public class customer {
    public static void main(String[] args) {
        /**
         * 创建工厂并且创建一个PizzaStore对象
         */
        SimplePizzaFactory factory = new SimplePizzaFactory();
        PizzaStore store = new PizzaStore(factory);

        Pizza pizza = store.orderPizza("cheese");
        System.out.println("We ordered a " + pizza.getName() + "\n");
        System.out.println(pizza);

        pizza = store.orderPizza("clam");
        System.out.println("We ordered a " + pizza.getName() + "\n");
        System.out.println(pizza);
    }
}
