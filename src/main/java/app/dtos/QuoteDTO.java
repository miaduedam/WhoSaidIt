package app.dtos;

import app.entities.Quote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter

public class QuoteDTO {
    private String quote;   // text from API
    private String author;  // author from API

public QuoteDTO(Quote quote){
    this.quote = quote.getText();
    this.author = String.valueOf(quote.getPerson());

}

public QuoteDTO(){}

}

