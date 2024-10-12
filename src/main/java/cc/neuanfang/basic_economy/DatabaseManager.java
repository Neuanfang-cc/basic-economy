package cc.neuanfang.basic_economy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        try {
            String dbPath = "./config/" + BasicEconomy.MOD_ID + "/economy.db";
            Path path = Paths.get(dbPath);

            // Ensure the directory exists
            Path directory = path.getParent();
            if (directory != null && !Files.exists(directory)) {
                Files.createDirectories(directory);
                BasicEconomy.LOGGER.info("Created config Directory");
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTables();
        } catch (SQLException | IOException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private static void createTables() {
        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY, balance REAL);";
        String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, sender_uuid TEXT, receiver_uuid TEXT, amount REAL, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP);";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPlayersTable);
            stmt.execute(createTransactionsTable);
        } catch (SQLException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }
}
