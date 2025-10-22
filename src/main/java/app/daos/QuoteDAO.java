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

    public void insertQuote(Quote quote) throws SQLException {
        String sql = "INSERT INTO quotes (text, person_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quote.getText());
            stmt.setInt(2, quote.getPerson().getId());
            stmt.executeUpdate();
        }
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
}
