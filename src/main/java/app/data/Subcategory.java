package app.data;

import lombok.Data;

@Data
public class Subcategory {
    private final long id;
    private final String name;
    private final long categoryId;
}
