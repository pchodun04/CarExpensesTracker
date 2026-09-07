import database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Tests{

    private Database db;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database("jdbc:h2:mem:testdb", "sa", "");
        db.createTable();
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.closeConnection();
    }

    @Test
    void testCarAdd() throws Exception {
        db.addCar("Honda", "Civic", "VIII", 2009, "1.8");
        assertEquals(1, db.getAllCars().size());
        assertEquals("Honda", db.getAllCars().getFirst().getMake());
    }

    @Test
    void testCarDelete() throws Exception {
        db.addCar("Honda", "Civic", "VIII", 2009, "1.8");
        db.deleteCar(1);
        assertEquals(0, db.getAllCars().size());
    }

    @Test
    void testExpenseAdd() throws Exception {
        db.addCar("Honda", "Civic", "VIII", 2009, "1.8");
        db.addExpense(1, "Olej", "Motul", 160.00, LocalDate.now(), 160000);
        assertEquals(160.00, db.getExpenseById(1).getPrice());
    }

    @Test
    void testExpenseUpdate() throws Exception {
        db.addCar("Honda", "Civic", "VIII", 2009, "1.8");
        db.addExpense(1, "Olej", "Motul", 160.00, LocalDate.now(), 160000);
        assertNotEquals("Shell", db.getExpenseById(1).getBrandName());
        db.updateExpense(1, "Olej", "Shell", 160.00, LocalDate.now(), 160000);
        assertEquals("Shell", db.getExpenseById(1).getBrandName());
    }

    @Test
    void testExpenseDelete() throws Exception {
        db.addCar("Honda", "Civic", "VIII", 2009, "1.8");
        db.addExpense(1, "Olej", "Motul", 160.00, LocalDate.now(), 160000);
        db.deleteExpense(1);
        assertEquals(0, db.getAllExpensesByCarId(1).size());
        assertNull(db.getExpenseById(1));
    }
}
