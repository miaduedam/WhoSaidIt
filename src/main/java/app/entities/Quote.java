package app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    private int id;
    private String text;
    private Person person;

    public Quote(String text, Person person) {
        this.text = text;
        this.person = person;
    }
}
