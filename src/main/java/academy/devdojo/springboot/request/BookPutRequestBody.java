package academy.devdojo.springboot.request;

import lombok.Data;

@Data
public class BookPutRequestBody {
    private Long id;
    private String name;
}
