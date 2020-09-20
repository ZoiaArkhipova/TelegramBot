package app.repository;

import app.data.Subcategory;
import app.data.SubcategoryByPreferences;
import org.springframework.data.repository.CrudRepository;

public interface SubcategoryByPreferencesRepository extends CrudRepository<SubcategoryByPreferences, Long> {
}
