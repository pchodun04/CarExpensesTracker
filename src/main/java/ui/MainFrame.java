package ui;

import database.Database;
import model.Car;
import model.Expense;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame{
    private Database db;
    private JComboBox<Car> carComboBox;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JButton sumButton = new JButton("Suma: 0.00 zł");

    public MainFrame(Database db) {
        this.db = db;
        setTitle("Wydatki samochodowe");
        java.net.URL iconURL = getClass().getResource("/car_expense_icon.jpg");
        assert iconURL != null;
        setIconImage(new ImageIcon(iconURL).getImage());
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new JPanel(new BorderLayout()) {
            Image image = new ImageIcon(getClass().getResource("/car_expense_icon.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
                    g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                }
            }
        });

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
        JLabel samochodText = new JLabel("Samochód");

        samochodText.setForeground(Color.BLACK);
        //samochodText.setOpaque(true);
        topPanel.add(samochodText);
        carComboBox.setForeground(Color.BLACK);
        carComboBox.setOpaque(true);
        carComboBox.setBackground(Color.lightGray);
        topPanel.add(carComboBox);
        addCarButton.setForeground(Color.BLACK);
        addCarButton.setOpaque(true);
        addCarButton.setBackground(Color.lightGray);
        topPanel.add(addCarButton);
        deleteCarButton.setForeground(Color.BLACK);
        deleteCarButton.setOpaque(true);
        deleteCarButton.setBackground(Color.lightGray);
        sumButton.setOpaque(true);
        topPanel.add(deleteCarButton);
        sumButton.setBackground(new Color(235, 100, 100));
        sumButton.setForeground(Color.BLACK);
        sumButton.setFocusPainted(false);
        sumButton.setBorderPainted(false);
        sumButton.setUI(new BasicButtonUI());
        topPanel.add(sumButton);

        topPanel.setOpaque(false);
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

        expenseTable.getColumnModel().getColumn(0).setMinWidth(0);
        expenseTable.getColumnModel().getColumn(0).setMaxWidth(0);

        expenseTable.setBackground(new Color(220, 220, 220));
        expenseTable.getTableHeader().setBackground(Color.lightGray);
        expenseTable.getTableHeader().setForeground(Color.black);
        expenseTable.getTableHeader().setOpaque(true);
        expenseTable.setForeground(Color.black);
        expenseTable.setOpaque(true);
        JScrollPane scrollPane = new JScrollPane(expenseTable);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel();
        JButton addExpenseButton = new JButton("Dodaj wydatek");
        JButton deleteExpenseButton = new JButton("Usuń wydatek");
        JButton editExpenseButton = new JButton("Edytuj wydatek");

        addExpenseButton.addActionListener(e -> openExpenseForm());
        deleteExpenseButton.addActionListener(e -> deleteExpense());
        editExpenseButton.addActionListener(e -> openEditExpenseForm());

        addExpenseButton.setBackground(Color.lightGray);
        addExpenseButton.setForeground(Color.BLACK);
        addExpenseButton.setOpaque(true);
        bottomPanel.add(addExpenseButton);
        deleteExpenseButton.setBackground(Color.lightGray);
        deleteExpenseButton.setForeground(Color.BLACK);
        deleteExpenseButton.setOpaque(true);
        bottomPanel.add(deleteExpenseButton);
        editExpenseButton.setBackground(Color.lightGray);
        editExpenseButton.setForeground(Color.BLACK);
        editExpenseButton.setOpaque(true);
        bottomPanel.add(editExpenseButton);

        bottomPanel.setOpaque(false);
        add(bottomPanel, BorderLayout.SOUTH);
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
        double total = 0;
        Car selectedCar = (Car) carComboBox.getSelectedItem();
        if(selectedCar == null){
            return;
        }

        try {
            for(Expense expense : db.getAllExpensesByCarId(selectedCar.getId())){
                tableModel.addRow(new Object[]{
                        expense.getId(),
                        expense.getPartName(),
                        expense.getBrandName(),
                        String.format("%.2f zł", expense.getPrice()),
                        expense.getChangeDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        expense.getMileage() + " km"
                });
                total += expense.getPrice();
            }
            sumButton.setText(String.format("Suma: %.2f zł", total));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getSelectedExpenseId(){
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz wydatek");
            return -1;
        }
        return (int) tableModel.getValueAt(row, 0);
    }

    private void openExpenseForm() {
        Car selectedCar = (Car) carComboBox.getSelectedItem();
        assert selectedCar != null;
        new ExpenseForm(this, db, selectedCar.getId(), this::refreshExpenses);
    }

    private void openEditExpenseForm() {
        int id = getSelectedExpenseId();
        if(id == -1){
            return;
        }
        try{
            Expense expense = db.getExpenseById(id);
            new ExpenseForm(this, db, expense, this::refreshExpenses);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void openCarForm() {
        new CarForm(this, db, this::refreshCars);
    }

    private void deleteCar() {
        Car selectedCar = (Car) carComboBox.getSelectedItem();
        if(selectedCar == null){
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Usunąć " + selectedCar + "?", "Potwierdź", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            try{
                db.deleteCar(selectedCar.getId());
                refreshCars();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteExpense() {
        int id = getSelectedExpenseId();
        if(id == -1){
            return;
        }
        try {
            db.deleteExpense(id);
            refreshExpenses();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
