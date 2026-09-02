import database.Database;
import ui.MainFrame;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try{
                Database db = new Database();
                db.createTable();
                new MainFrame(db);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
