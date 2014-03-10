package lambda;

/**
 * Just a car representation
 */
public class Car {

    String brand;
    String model;
    Double price;

    public Car(String brand, String model, Double price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }

    public String toString() {
        return model;
    }
}


