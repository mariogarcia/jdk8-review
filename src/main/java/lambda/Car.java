package lambda;

import java.util.Optional;

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

    public Optional<String> getModel() {
        return Optional.ofNullable(this.model);
    }

    public String toString() {
        return model;
    }
}


