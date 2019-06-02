package pizza;

import java.util.ArrayList;

/**
 * 抽象Pizza类，其他种类都继承这个类
 */
public abstract class Pizza {
    /**
     * Pizza名字
     */
    String name;
    /**
     * 面团
     */
    String dough;
    /**
     * 调味汁
     */
    String sauce;
    /**
     * 配料
     */
    ArrayList<String> toppings = new ArrayList<String>();

    public String getName() {
        return name;
    }

    /**
     * 准备
     */
    public void prepare() {
        System.out.println("Preparing " + name);
    }

    /**
     * 烘焙
     */
    public void bake() {
        System.out.println("Baking " + name);
    }

    /**
     * 切片
     */
    public void cut() {
        System.out.println("Cutting " + name);
    }

    /**
     * 包装
     */
    public void box(){
        System.out.println("Cutting " + name);
    }

    @Override
    public String toString() {
        StringBuffer display = new StringBuffer();
        display.append("---- " + name + " ----\n");
        display.append("dough:" + dough + "\n");
        display.append("sauce:" + sauce + "\n");
        display.append("toppings:" + "\n");
        for (String topping : toppings){
            display.append(topping + "\n");
        }
        return display.toString();
    }
}
