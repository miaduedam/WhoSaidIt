package app.entities;

import app.dtos.PersonDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    // One person can have many quotes
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Quote> quotes = new HashSet<>();

    // Keep your convenience constructor
    public Person(String name) {
        this.name = name;
    }

    public void addQuote(Quote quote) {
        quotes.add(quote);
        quote.setPerson(this);
    }

    public Person(PersonDTO personDTO) {
        this.name = personDTO.getAuthor();
    }

}
