package ui;

import database.Database;
import model.Car;
import model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame{
    private Database db;
    private JComboBox<Car> carComboBox;
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    public MainFrame(Database db) {
        this.db = db;
        setTitle("Wydatki samochodowe");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        buildTopPanel();
        buildTable();
        buildBottomPanel();

        refreshCars();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildTopPanel() {
        JPanel topPanel = new JPanel();

        carComboBox = new JComboBox<>();
        carComboBox.setPreferredSize(new Dimension(250, 28));
        carComboBox.addActionListener(e -> refreshExpenses());

        JButton addCarButton = new JButton("Dodaj samochód");
        addCarButton.addActionListener(e -> openCarForm());
        JButton deleteCarButton = new JButton("Usuń");
        deleteCarButton.addActionListener(e -> deleteCar());

        topPanel.add(new JLabel("Samochód"));
        topPanel.add(carComboBox);
        topPanel.add(addCarButton);
        topPanel.add(deleteCarButton);

        add(topPanel, BorderLayout.NORTH);
    }

    private void buildTable() {
        String[] columns = {"ID", "Część", "Marka", "Cena", "Data wymiany", "Przebieg"};

        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.setRowHeight(24);

        expenseTable.getColumnModel().getColumn(0).setMaxWidth(0);
        expenseTable.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel();
    }

    private void refreshCars() {
        Car selectedCar = (Car) carComboBox.getSelectedItem();

        carComboBox.removeAllItems();

        try {
            for(Car car : db.getAllCars()){
                carComboBox.addItem(car);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(selectedCar != null){
            carComboBox.setSelectedItem(selectedCar);
        }
    }

    private void refreshExpenses() {
        tableModel.setRowCount(0);
        Car selectedCar = (Car) carComboBox.getSelectedItem();
        if(selectedCar == null){
            return;
        }

        try {
            for(Expense expense : db.getAllExpenses()){
                tableModel.addRow(new Object[]{
                        expense.getId(),
                        expense.getPartName(),
                        expense.getBrandName(),
                        expense.getPrice(),
                        expense.getChangeDate(),
                        expense.getMileage()
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
