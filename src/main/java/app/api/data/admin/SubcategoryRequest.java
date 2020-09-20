package app.api.data.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubcategoryRequest {
    private String name;
    private Long categoryId;
}
