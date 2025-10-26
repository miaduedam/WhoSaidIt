package app.controllers;

import app.daos.PersonDAO;
import app.daos.QuoteDAO;
import app.dtos.PersonDTO;
import app.dtos.QuoteDTO;
import app.entities.Person;
import app.entities.Quote;
import app.services.QuoteService;

import java.util.List;

public class QuoteAPIController {
    private final PersonDAO personDAO;
    private final QuoteDAO quoteDAO;
    private final QuoteService quoteService;


    public QuoteAPIController(PersonDAO personDAO, QuoteDAO quoteDAO, QuoteService quoteService, PersonDTO personDTO) {
        this.personDAO = personDAO;
        this.quoteDAO = quoteDAO;
        this.quoteService = quoteService;

    }

//    public void fetchAndSaveQuotes(int limit) throws Exception {
//        List<QuoteDTO> quotes = quoteService.fetchQuotes(limit);
//
//        for (QuoteDTO dto : quotes) {
//
//            PersonDTO existing = personDAO.getPersonByName(dto.getAuthor());
//
//
//            if (existing == null) {
//                PersonDTO personDTO = new PersonDTO(dto.getAuthor());
//                personDAO.createPerson(personDTO);
//            }
//
//            Quote quote = new Quote(dto.getQuote(), existing.getAuthor());
//            quoteDAO.insertQuote(quote);
//
//            System.out.println("Inserted quote: " + quote);
//        }
//    }
}
