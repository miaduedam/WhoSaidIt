package app.dtos;

import app.entities.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDTO {
    private Integer id;
    private String author;

    public PersonDTO() {}

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.author = person.getName();
    }
}
