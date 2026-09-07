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
    private JTextField priceField = new JTextField(10 );
    private JTextField changeDateField = new JTextField(12);
    private JTextField mileageField = new JTextField(22);

    private final Database db;
    private int carId;
    private int editId;
    private final Runnable onSave;

    public ExpenseForm(JFrame parent, Database db, int carId, Runnable onSave) {
        super(parent, "Dodaj wydatek", true);
        this.db = db;
        this.carId = carId;
        this.onSave = onSave;
        this.editId = -1;

        buildUI();
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    public ExpenseForm(JFrame parent, Database db, Expense expense, Runnable onSave) {
        super(parent, "Edytuj wydatek", true);
        this.editId = expense.getId();
        this.db = db;
        this.onSave = onSave;

        partNameField.setText(expense.getPartName());
        brandNameField.setText(expense.getBrandName());
        priceField.setText(String.valueOf(expense.getPrice()));
        changeDateField.setText(expense.getChangeDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        mileageField.setText(String.valueOf(expense.getMileage()));

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
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pricePanel.add(priceField);
        pricePanel.add(new JLabel(" zł"));
        add(pricePanel, c);

        c.gridx = 0;
        c.gridy = 3;
        add(new JLabel("Data wymiany"), c);
        c.gridx = 1;
        add(changeDateField, c);

        c.gridx = 0;
        c.gridy = 4;
        add(new JLabel("Przebieg"), c);
        c.gridx = 1;
        JPanel mileagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mileagePanel.add(mileageField);
        mileagePanel.add(new JLabel(" km"));
        add(mileagePanel, c);

        JButton saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> save());
        c.gridx = 1;
        c.gridy = 5;
        c.anchor = GridBagConstraints.EAST;
        add(saveButton, c);
        getRootPane().setDefaultButton(saveButton);
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
                JOptionPane.showMessageDialog(this, "Wprowadź nazwe części", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(editId == -1){
                db.addExpense(carId, partName, partBrand, partPrice, changeDate, mileage);
            }else{
                db.updateExpense(editId, partName, partBrand, partPrice, changeDate, mileage);
            }

            onSave.run();
            dispose();
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
