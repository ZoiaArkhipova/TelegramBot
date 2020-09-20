package app.service;

import app.data.Subcategory;
import app.data.SubcategoryByPreferences;
import app.repository.SubcategoryByPreferencesRepository;
import app.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersistenceService {
    private final SubcategoryRepository subcategoryRepository;
    private final SubcategoryByPreferencesRepository subcategoryByPreferencesRepository;

    public Subcategory saveSubcategory(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    public List<Subcategory> getAllSubcategories() {
        List<Subcategory> result = new ArrayList<>();
        subcategoryRepository.findAll().forEach(result::add);
        return result;
    }

    public SubcategoryByPreferences saveSubcategoriesByPreferences(SubcategoryByPreferences subcategoryByPreferences) {
        return subcategoryByPreferencesRepository.save(subcategoryByPreferences);
    }

    public List<SubcategoryByPreferences> getAllSubcategoriesByPreferences() {
        List<SubcategoryByPreferences> result = new ArrayList<>();
        subcategoryByPreferencesRepository.findAll().forEach(result::add);
        return result;
    }
}
