package ui;

import database.Database;
import model.Expense;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpenseForm extends JDialog {
    private JTextField partNameField = new JTextField(22);
    private JTextField brandNameField = new JTextField(22);
    private JTextField priceField = new JTextField(10);
    private JTextField changeDateField = new JTextField(12);
    private JTextField mileageField = new JTextField(22);

    private final Database db;
    private final int carId;
    private final Runnable onSave;

    public ExpenseForm(JFrame parent, Database db, int carId, Runnable onSave) {
        super(parent, "Dodaj wydatek", true);
        this.db = db;
        this.carId = carId;
        this.onSave = onSave;

        buildUI();
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
    private void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Nazwa części"), c);
        c.gridx = 1;
        add(partNameField, c);

        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Nazwa marki"), c);
        c.gridx = 1;
        add(brandNameField, c);

        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("Cena"), c);
        c.gridx = 1;
        add(priceField, c);

        c.gridx = 0;
        c.gridy = 3;
        add(new JLabel("Data wymiany"), c);
        c.gridx = 1;
        add(changeDateField, c);

        c.gridx = 0;
        c.gridy = 4;
        add(new JLabel("Przebieg"), c);
        c.gridx = 1;
        add(mileageField, c);

        JButton saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> save());
        c.gridx = 1;
        c.gridy = 5;
        c.anchor = GridBagConstraints.EAST;
        add(saveButton, c);
    }

    private void save() {
        try{
            String partName = partNameField.getText().trim();
            String partBrand = brandNameField.getText().trim();
            Double partPrice = Double.parseDouble(priceField.getText().trim());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate changeDate = LocalDate.parse(changeDateField.getText().trim(), fmt );
            int mileage = Integer.parseInt(mileageField.getText().trim());

            if(partName.isBlank()){
                JOptionPane.showMessageDialog(this, "Please enter a part name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            db.addExpense(carId, partName, partBrand, partPrice, changeDate, mileage);
            onSave.run();
            dispose();
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
