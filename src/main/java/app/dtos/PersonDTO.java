package app.dtos;

import app.entities.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PersonDTO {
    private String author;


public PersonDTO(Person person){
    this.author = person.getName();
}

}
