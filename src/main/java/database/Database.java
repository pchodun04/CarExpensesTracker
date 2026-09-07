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
        String url = "jdbc:h2:~/wydatki;AUTO_SERVER=TRUE";
        String user = "carexpensetracker";
        String password = "";
        conn = DriverManager.getConnection(url, user, password);
    }

    public Database(String url, String user, String password) throws Exception {
        conn = DriverManager.getConnection(url, user, password);
    }

    public void createTable() throws Exception {
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS car (id INT AUTO_INCREMENT PRIMARY KEY, make VARCHAR(50) NOT NULL, model VARCHAR(50) NOT NULL, generation VARCHAR(5) NOT NULL, production_year INT NOT NULL, engine VARCHAR(50) NOT NULL )");
        statement.execute("CREATE TABLE IF NOT EXISTS expense (id INT AUTO_INCREMENT PRIMARY KEY, carId INT NOT NULL, part_name VARCHAR(50) NOT NULL, brand_name VARCHAR(50) NOT NULL, price DOUBLE NOT NULL, change_date DATE, mileage INT)");
    }

    public void addCar(String make, String model, String generation, int year, String engine) throws Exception {
        String sql = "INSERT INTO car (make, model, generation, production_year, engine) VALUES (?, ?, ?, ?, ?)";
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
                    rs.getInt("production_year"),
                    rs.getString("engine")));
        }
        return cars;
    }

    public void addExpense(int carId, String partName, String brandName, double price, LocalDate changeDate, int mileage) throws Exception {
        String sql = "INSERT INTO expense (carId, part_name, brand_name, price, change_date, mileage) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt   (1, carId);
        ps.setString(2, partName);
        ps.setString(3, brandName);
        ps.setDouble(4, price);
        ps.setDate  (5, java.sql.Date.valueOf(changeDate));
        ps.setInt   (6, mileage);
        ps.executeUpdate();
    }

    public void updateExpense(int id, String partName, String brandName, double price, LocalDate changeDate, int mileage) throws Exception {
        String sql = "UPDATE expense set part_name = ?, brand_name = ?, price = ?, change_date = ?, mileage = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, partName);
        ps.setString(2, brandName);
        ps.setDouble(3, price);
        ps.setDate  (4, java.sql.Date.valueOf(changeDate));
        ps.setInt   (5, mileage);
        ps.setInt   (6, id);
        ps.executeUpdate();
    }

    public void deleteExpense(int id) throws Exception {
        String sql = "DELETE FROM expense WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<Expense> getAllExpensesByCarId(int carId) throws Exception {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expense WHERE carId = ? ORDER BY change_date ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, carId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            expenses.add(new Expense(rs.getInt("id"), rs.getInt("carId"), rs.getString("part_name"),
                rs.getString("brand_name"),
                rs.getDouble("price"),
                rs.getDate("change_date").toLocalDate(),
                rs.getInt("mileage")));
        }
        return expenses;
    }

    public Expense getExpenseById(int id) throws Exception {
        String sql = "SELECT * FROM expense WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Expense(
                    rs.getInt("id"), rs.getInt("carId"),
                    rs.getString("part_name"), rs.getString("brand_name"),
                    rs.getDouble("price"),
                    rs.getDate("change_date").toLocalDate(),
                    rs.getInt("mileage")
            );
        }
        return null;
    }
    public void closeConnection() throws Exception {
        conn.close();
    }
}
