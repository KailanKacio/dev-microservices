package academy.devdojo.springboot.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookPutRequestBody {
    private Long id;
    private String name;
}
