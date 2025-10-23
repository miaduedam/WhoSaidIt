package app.daos;

import app.entities.Person;
import app.entities.Quote;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuoteDAOTest {

    // Starter en midlertidig Postgres database i Docker
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16.2")
                    .withDatabaseName("test_db")
                    .withUsername("test")
                    .withPassword("test");

    private Connection conn;
    private QuoteDAO quoteDAO;
    private Person p1, p2;

    @BeforeAll
    void setupContainer() throws Exception {
        postgres.start();
        conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        quoteDAO = new QuoteDAO(conn);

        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE people (id SERIAL PRIMARY KEY, name VARCHAR(100));");
            st.execute("CREATE TABLE quotes (id SERIAL PRIMARY KEY, text TEXT, person_id INT REFERENCES people(id));");
        }
    }

    @BeforeEach
    void seedData() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("TRUNCATE quotes, people RESTART IDENTITY CASCADE;");
            st.execute("INSERT INTO people (name) VALUES ('Mia'), ('Esben');");
            st.execute("""
                INSERT INTO quotes (text, person_id) VALUES
                ('Første citat', 1),
                ('Andet citat', 1),
                ('Tredje citat', 2);
            """);
        }

        p1 = new Person(1, "Mia");
        p2 = new Person(2, "Esben");
    }


    // CREATE

    @Test
    void insertQuote_shouldInsertNewQuote() throws Exception {
        Quote newQuote = new Quote("Nyt testcitat", p1);
        quoteDAO.insertQuote(newQuote);

        List<Quote> all = quoteDAO.getAllQuotes();
        assertEquals(4, all.size());
        assertTrue(all.stream().anyMatch(q -> q.getText().equals("Nyt testcitat")));
    }


    // READ - getAllQuotes

    @Test
    void getAllQuotes_shouldReturnAllQuotes() throws Exception {
        List<Quote> all = quoteDAO.getAllQuotes();
        assertEquals(3, all.size());
        assertEquals("Første citat", all.get(0).getText());
    }


    // READ - getQuoteById

    @Test
    void getQuoteById_shouldReturnCorrectQuote() throws Exception {
        Quote quote = quoteDAO.getQuoteById(1);
        assertNotNull(quote);
        assertEquals(1, quote.getId());
        assertEquals("Første citat", quote.getText());
        assertEquals("Mia", quote.getPerson().getName());
    }


    // READ - getQuotesByPerson

    @Test
    void getQuotesByPerson_shouldReturnOnlyPersonsQuotes() throws Exception {
        List<Quote> miaQuotes = quoteDAO.getQuotesByPerson(p1);
        List<Quote> esbenQuotes = quoteDAO.getQuotesByPerson(p2);

        assertEquals(2, miaQuotes.size());
        assertEquals(1, esbenQuotes.size());
        assertTrue(miaQuotes.stream().allMatch(q -> q.getPerson().getName().equals("Mia")));
        assertTrue(esbenQuotes.stream().allMatch(q -> q.getPerson().getName().equals("Esben")));
    }


    // UPDATE

    @Test
    void updateQuote_shouldModifyExistingQuote() throws Exception {
        Quote existing = quoteDAO.getQuoteById(1);
        existing.setText("Opdateret citat");
        quoteDAO.updateQuote(existing);

        Quote updated = quoteDAO.getQuoteById(1);
        assertEquals("Opdateret citat", updated.getText());
        assertEquals(existing.getPerson().getId(), updated.getPerson().getId());
    }


    // DELETE

    @Test
    void deleteQuote_shouldRemoveQuoteFromDB() throws Exception {
        quoteDAO.deleteQuote(1);
        List<Quote> all = quoteDAO.getAllQuotes();
        assertEquals(2, all.size());
        assertNull(quoteDAO.getQuoteById(1));
    }

    @AfterAll
    void tearDown() throws Exception {
        conn.close();
        postgres.stop();
    }
}
