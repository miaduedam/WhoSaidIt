package app.daos;

import app.entities.Quote;
import app.entities.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuoteDAO {
    private final Connection conn;

    public QuoteDAO(Connection conn) {
        this.conn = conn;
    }

    //CREATE
    public void insertQuote(Quote quote) throws SQLException {
        String sql = "INSERT INTO quotes (text, person_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quote.getText());
            stmt.setInt(2, quote.getPerson().getId());
            stmt.executeUpdate();
        }
    }

    public List<Quote> getAllQuotes() throws SQLException {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT q.id, q.text, p.id AS person_id, p.name " +
                "FROM quotes q JOIN people p ON q.person_id = p.id";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Person p = new Person(rs.getInt("person_id"), rs.getString("name"));
                Quote quote = new Quote(rs.getInt("id"), rs.getString("text"), p);
                quotes.add(quote);
            }
        }
        return quotes;
    }


    public List<Quote> getQuotesByPerson(Person person) throws SQLException {
        List<Quote> quotes = new ArrayList<>();
        String sql = "SELECT q.id, q.text, p.id AS person_id, p.name " +
                "FROM quotes q JOIN people p ON q.person_id = p.id " +
                "WHERE p.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, person.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Person p = new Person(rs.getInt("person_id"), rs.getString("name"));
                Quote quote = new Quote(rs.getInt("id"), rs.getString("text"), p);
                quotes.add(quote);
            }
        }
        return quotes;
    }

    // ✅ READ (by ID)
    public Quote getQuoteById(int id) throws SQLException {
        String sql = "SELECT q.id, q.text, p.id AS person_id, p.name " +
                "FROM quotes q JOIN people p ON q.person_id = p.id WHERE q.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Person p = new Person(rs.getInt("person_id"), rs.getString("name"));
                return new Quote(rs.getInt("id"), rs.getString("text"), p);
            }
        }
        return null;
    }

    // ✅ UPDATE
    public void updateQuote(Quote quote) throws SQLException {
        String sql = "UPDATE quotes SET text = ?, person_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quote.getText());
            stmt.setInt(2, quote.getPerson().getId());
            stmt.setInt(3, quote.getId());
            stmt.executeUpdate();
        }
    }

    // ✅ DELETE
    public void deleteQuote(int id) throws SQLException {
        String sql = "DELETE FROM quotes WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
