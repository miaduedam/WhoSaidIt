package app.controllers;

import app.daos.PersonDAO;
import app.daos.QuoteDAO;
import app.dtos.QuoteDTO;
import app.entities.Person;
import app.entities.Quote;
import app.services.QuoteService;

import java.util.List;

public class QuoteController {
    private final PersonDAO personDAO;
    private final QuoteDAO quoteDAO;
    private final QuoteService quoteService;

    public QuoteController(PersonDAO personDAO, QuoteDAO quoteDAO, QuoteService quoteService) {
        this.personDAO = personDAO;
        this.quoteDAO = quoteDAO;
        this.quoteService = quoteService;
    }

    public void fetchAndSaveQuotes(int limit) throws Exception {
        List<QuoteDTO> quotes = quoteService.fetchQuotes(limit);

        for (QuoteDTO dto : quotes) {
            Person person = personDAO.getPersonByName(dto.getAuthor());
            if (person == null) {
                person = new Person(dto.getAuthor());
                personDAO.insertPerson(person);
            }

            Quote quote = new Quote(dto.getQuote(), person);
            quoteDAO.insertQuote(quote);

            System.out.println("Inserted quote: " + quote);
        }
    }
}
