package app.controllers;

import app.config.HibernateConfig;
import app.daos.QuoteDAO;
import app.entities.Person;
import app.entities.Quote;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class QuoteController {

    private final QuoteDAO quoteDAO;

    public QuoteController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.quoteDAO = QuoteDAO.getInstance(emf);
    }

    // CREATE
    public void addQuote(Quote quote) {
        quoteDAO.insertQuote(quote);
    }

    // READ
    public List<Quote> getAllQuotes() {
        return quoteDAO.getAllQuotes();
    }

    public Quote getQuoteById(int id) {
        return quoteDAO.getQuoteById(id);
    }

    public List<Quote> getQuotesByPerson(Person person) {
        return quoteDAO.getQuotesByPerson(person);
    }

    // UPDATE
    public void updateQuote(Quote quote) {
        quoteDAO.updateQuote(quote);
    }

    // DELETE
    public void deleteQuote(int id) {
        quoteDAO.deleteQuote(id);
    }
}
