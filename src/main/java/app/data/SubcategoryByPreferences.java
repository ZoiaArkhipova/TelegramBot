package app.data;

import app.data.presentrecipient.Age;
import app.data.presentrecipient.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Data
@AllArgsConstructor
public class SubcategoryByPreferences {
    @Id
    private long subcategoryId;
    private Set<Gender> genders;
    private Set<Age> ages;
}
