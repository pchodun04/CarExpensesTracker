package model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private int carId;
    private String partName;
    private String brandName;
    private double price;
    private LocalDate changeDate;
    private int mileage;

    public Expense(int id, int carId, String partName, String brandName, double price, LocalDate changeDate, int mileage) {
        this.id = id;
        this.carId = carId;
        this.partName = partName;
        this.brandName = brandName;
        this.price = price;
        this.changeDate = changeDate;
        this.mileage = mileage;
    }

    public Expense(int carId, String partName, String brandName, double price, LocalDate changeDate, int mileage) {
        this.carId = carId;
        this.partName = partName;
        this.brandName = brandName;
        this.price = price;
        this.changeDate = changeDate;
        this.mileage = mileage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate

    getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDate

                                      changeDate) {
        this.changeDate = changeDate;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }


}
