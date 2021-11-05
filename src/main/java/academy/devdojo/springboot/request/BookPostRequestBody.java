package academy.devdojo.springboot.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class BookPostRequestBody {

    @NotEmpty(message = "The book cannot be empty")
    private String name;
}
