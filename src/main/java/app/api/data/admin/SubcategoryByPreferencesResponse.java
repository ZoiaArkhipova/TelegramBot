package app.api.data.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryByPreferencesResponse {
    private long subcategoryId;
    private Set<String> genders;
    private Set<String> ages;
}
