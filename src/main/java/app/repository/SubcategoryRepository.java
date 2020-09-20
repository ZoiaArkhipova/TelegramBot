package app.repository;

import app.data.Subcategory;
import org.springframework.data.repository.CrudRepository;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {
}
