package cc.neuanfang.basic_economy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

public class EconomyAPI {
    public static final DecimalFormat df = new DecimalFormat("#.00");

    // Kontostand eines Spielers abrufen
    public static double getBalance(UUID uuid) {
        String query = "SELECT balance FROM players WHERE uuid = ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                // Spieler existiert nicht, initialisiere mit 0
                setBalance(uuid, 0.0);
                return 0.0;
            }
        } catch (SQLException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
            return 0.0;
        }
    }

    // Kontostand eines Spielers setzen
    public static void setBalance(UUID uuid, double amount) {
        String query = "REPLACE INTO players (uuid, balance) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(query)) {
            ps.setString(1, uuid.toString());
            ps.setDouble(2, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

    // Betrag zum Kontostand eines Spielers hinzufügen
    public static void addBalance(UUID uuid, double amount) {
        double currentBalance = getBalance(uuid);
        setBalance(uuid, currentBalance + amount);
    }

    // Betrag vom Kontostand eines Spielers abziehen
    public static void subtractBalance(UUID uuid, double amount) {
        double currentBalance = getBalance(uuid);
        setBalance(uuid, currentBalance - amount);
    }

    // Transaktion zwischen zwei Spielern durchführen
    public static boolean transfer(UUID senderUuid, UUID receiverUuid, double amount) {
        if (getBalance(senderUuid) >= amount) {
            subtractBalance(senderUuid, amount);
            addBalance(receiverUuid, amount);
            saveTransaction(senderUuid, receiverUuid, amount);
            cleanUpTransactions();
            return true;
        } else {
            return false;
        }
    }

    // Transaktion in der Datenbank speichern
    private static void saveTransaction(UUID senderUuid, UUID receiverUuid, double amount) {
        String query = "INSERT INTO transactions (sender_uuid, receiver_uuid, amount) VALUES (?, ?, ?)";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(query)) {
            ps.setString(1, senderUuid.toString());
            ps.setString(2, receiverUuid.toString());
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (SQLException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

    // Nur die letzten 1000 Transaktionen behalten
    private static void cleanUpTransactions() {
        String query = "DELETE FROM transactions WHERE id NOT IN (SELECT id FROM transactions ORDER BY id DESC LIMIT 1000)";
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            BasicEconomy.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }
}

