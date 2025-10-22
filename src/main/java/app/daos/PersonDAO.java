package app.daos;

import app.entities.Person;

import java.sql.*;

public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    public int insertPerson(Person person) throws SQLException {
        String sql = "INSERT INTO persons (name) VALUES (?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                person.setId(id);
                return id;
            }
            throw new SQLException("Failed to insert person");
        }
    }

    public Person getPersonByName(String name) throws SQLException {
        String sql = "SELECT * FROM persons WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Person(rs.getInt("id"), rs.getString("name"));
            }
            return null;
        }
    }
}
