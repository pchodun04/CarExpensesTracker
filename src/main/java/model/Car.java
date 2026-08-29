package model;

public class Car {
    private int id;
    private String make;
    private String model;
    private String generation;
    private int year;
    private String engine;

    public Car(int id, String make, String model, String generation, int year, String engine) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.generation = generation;
        this.year = year;
        this.engine = engine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", generation='" + generation + '\'' +
                ", year='" + year + '\'' +
                ", engine='" + engine + '\'' +
                '}';
    }
}
