package app.controllers;

import app.daos.QuoteDAO;
import app.entities.Quote;
import java.sql.SQLException;
import java.util.List;

public class QuoteController {
    private final QuoteDAO quoteDAO;

    public QuoteController(QuoteDAO quoteDAO) {
        this.quoteDAO = quoteDAO;
    }

//det her er bare til CRUD
    public List<Quote> getAllQuotes() throws SQLException {
        return quoteDAO.getAllQuotes();
    }

    public Quote getQuoteById(int id) throws SQLException {
        return quoteDAO.getQuoteById(id);
    }

    public void addQuote(Quote quote) throws SQLException {
        quoteDAO.insertQuote(quote);
    }

    public void updateQuote(Quote quote) throws SQLException {
        quoteDAO.updateQuote(quote);
    }

    public void deleteQuote(int id) throws SQLException {
        quoteDAO.deleteQuote(id);
    }
}
