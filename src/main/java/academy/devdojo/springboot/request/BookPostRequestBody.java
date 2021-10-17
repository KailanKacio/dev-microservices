package academy.devdojo.springboot.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BookPostRequestBody {

    @NotEmpty(message = "The book cannot be empty")
    private String name;
}
