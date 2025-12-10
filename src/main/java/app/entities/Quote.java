package app.entities;

import app.dtos.QuoteDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quotes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    @ToString.Exclude
    private Person person;

    // Keep your existing convenience constructor
    public Quote(String text, Person person) {
        this.text = text;
        this.person = person;
    }

    public Quote(QuoteDTO quoteDTO) {
        this.text = quoteDTO.getQuote();
    }

}
