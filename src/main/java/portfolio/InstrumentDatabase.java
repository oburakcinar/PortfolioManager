package portfolio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstrumentDatabase {
    private String databasePath;

    public InstrumentDatabase(String databasePath) {
        this.databasePath = databasePath;
    }

    public List<Instrument> getList() throws SQLException {
        List<Instrument> todos = new ArrayList<>();
        try (Connection connection = createConnectionAndEnsureDatabase();
             ResultSet results = connection.prepareStatement("SELECT * FROM Todo").executeQuery()) {
            while (results.next()) {
                todos.add(new Instrument(results.getString("symbol"), results.getString("name"),  //????? emin ol
                        results.getDouble("quantity"), results.getDouble("unitValue")));
            }
        }
        return todos;
    }

    public void add(String symbolToAdd, String nameToAdd, double qtyToAdd, double priceToAdd) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Todo (symbol, name, quantity, unitValue, totalValue) VALUES (?, ?, ?,?, ?)");
            stmt.setString(1, symbolToAdd);
            stmt.setString(2, nameToAdd);
            stmt.setDouble(3, qtyToAdd);
            stmt.setDouble(4, priceToAdd);
            stmt.setDouble(5, (qtyToAdd * priceToAdd));

            stmt.executeUpdate();
        }
    }

    public void updateQty(Instrument instrument, double newQty) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE Todo SET quantity = ? WHERE symbol = ?");
            stmt.setDouble(1, newQty);
            stmt.setString(2, instrument.getSymbol());
            stmt.executeUpdate();
        }
    }

    public void updateUnitValue(Instrument instrument, double newUnitValue) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE Todo SET unitValue = ? WHERE symbol = ?");
            stmt.setDouble(1, newUnitValue);
            stmt.setString(2, instrument.getSymbol());
            stmt.executeUpdate();
        }
    }

    public void remove(Instrument instrument) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Todo WHERE symbol = ?");
            stmt.setString(1, instrument.getSymbol());
            stmt.executeUpdate();
        }
    }

    private Connection createConnectionAndEnsureDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(this.databasePath, "sa", "");
        try {
            conn.prepareStatement("CREATE TABLE Todo (symbol varchar(255), name varchar(255), quantity double, unitValue double, totalValue double)").execute();
        } catch (SQLException t) {
        }

        return conn;
    }
}
