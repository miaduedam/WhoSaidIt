package app;

import app.daos.QuoteDAO;
import app.entities.Person;

import java.util.List;

public class PersonPopulator {

    public static List<Person> populatePersons(QuoteDAO quoteDAO) {

        // Create first person
        Person p1 = Person.builder()
                .name("Alice Anderson")
                .build();

        Person p2 = Person.builder()
                .name("Esben")
                .build();

        Person p3 = Person.builder()
                .name("Mia")
                .build();

        return List.of(p1, p2, p3);

    }

    //persist using dao


}
