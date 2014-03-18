package lambda;

/**
 * Just a car representation
 */
public class Car {

    public String brand;
    public String model;
    public Double price;

    public Car(String brand, String model, Double price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }

    public String toString() {
        return model;
    }
}


