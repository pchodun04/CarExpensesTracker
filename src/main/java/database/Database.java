package database;

import model.Car;
import model.Expense;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection conn;

    public Database() throws Exception {
        String url = "jdbc:h2./wydatki;AUTO_SERVER=TRUE";
        String user = "carexpensetracker";
        String password = "";
        conn = DriverManager.getConnection(url, user, password);
    }

    public void createTable() throws Exception {
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS car (id INT AUTO_INCREMENT PRIMARY KEY, make VARCHAR(50) NOT NULL, model VARCHAR(50) NOT NULL, generation VARCHAR(5) NOT NULL, year INT NOT NULL, engine VARCHAR(50) NOT NULL )");
        statement.execute("CREATE TABLE IF NOT EXISTS expense (id INT AUTO_INCREMENT PRIMARY KEY, carId INT NOT NULL, part_name VARCHAR(50) NOT NULL, brand_name VARCHAR(50) NOT NULL, price DOUBLE NOT NULL, change_date DATE, mileage INT");
    }

    public void addCar(String make, String model, String generation, int year, String engine) throws Exception {
        String sql = "INSERT INTO car (make, model, generation, year, engine) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, make);
        preparedStatement.setString(2, model);
        preparedStatement.setString(3, generation);
        preparedStatement.setInt(4, year);
        preparedStatement.setString(5, engine);
        preparedStatement.executeUpdate();
    }

    public void deleteCar(int id) throws Exception {
        String sql = "DELETE FROM car WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Car> getAllCars() throws Exception {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM car ORDER BY make, model ASC";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            cars.add(new Car(rs.getInt("id"),
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getString("generation"),
                    rs.getInt("year"),
                    rs.getString("engine")));
        }
        return cars;
    }

    public void addExpense(String partName, String brandName, double price, Date changeDate, int mileage) throws Exception {
        String sql = "INSERT INTO expense (partName, brandName, price, changeDate, mileage) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, partName);
        preparedStatement.setString(2, brandName);
        preparedStatement.setDouble(3, price);
        preparedStatement.setDate(4, changeDate);
        preparedStatement.setInt(5, mileage);
    }

    public void deleteExpense(int id) throws Exception {
        String sql = "DELETE FROM expense WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Expense> getAllExpenses() throws Exception {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expense ORDER BY changeDate ASC";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        while (rs.next()) {
            rs.getString("part_name");
            rs.getString("brand_name");
            rs.getString("price");
            rs.getString("change_date");
            rs.getString("mileage");
        }
        return expenses;
    }
}
