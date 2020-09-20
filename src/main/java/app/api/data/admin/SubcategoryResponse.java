package app.api.data.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryResponse {
    private long id;
    private String name;
    private long categoryId;
}
