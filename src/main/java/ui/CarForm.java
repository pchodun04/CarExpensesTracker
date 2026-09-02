package ui;

import database.Database;

import javax.swing.*;
import java.awt.*;

public class CarForm extends JDialog{
    private JTextField makeField = new JTextField(22);
    private JTextField modelField = new JTextField(22);
    private JTextField generationField = new JTextField(10);
    private JTextField yearField = new JTextField(12);
    private JTextField engineField = new JTextField(22);

    private final Database db;
    private final Runnable onSave;

    public CarForm(JFrame parent, Database db, Runnable onSave) {
        super(parent, "Dodaj auto", true);
        this.db = db;
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
        add(new JLabel("Marka"), c);
        c.gridx = 1;
        add(makeField, c);

        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Model"), c);
        c.gridx = 1;
        add(modelField, c);

        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("Generacja"), c);
        c.gridx = 1;
        add(generationField, c);

        c.gridx = 0;
        c.gridy = 3;
        add(new JLabel("Rok"), c);
        c.gridx = 1;
        add(yearField, c);

        c.gridx = 0;
        c.gridy = 4;
        add(new JLabel("Silnik"), c);
        c.gridx = 1;
        add(engineField, c);

        JButton saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> save());
        c.gridx = 1;
        c.gridy = 5;
        c.anchor = GridBagConstraints.EAST;
        add(saveButton, c);
    }

    private void save() {
        try{
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();
            String generation = generationField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            String engine = engineField.getText().trim();

            if(make.isBlank()){
                JOptionPane.showMessageDialog(this, "Wprowadź marke", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            db.addCar(make, model, generation, year, engine);
            onSave.run();
            dispose();
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
