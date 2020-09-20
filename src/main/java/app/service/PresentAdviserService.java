package app.service;

import app.data.MarketModel;
import app.data.MarketVendor;
import app.data.SubcategoryByPreferences;
import app.data.presentrecipient.Age;
import app.data.presentrecipient.Gender;
import app.api.data.market.MarketModelResponse;
import app.data.Subcategory;
import app.api.data.market.MarketVendorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresentAdviserService {
    private final PersistenceService persistenceService;
    private final MarketApiService marketApiService;

    public static final List<Subcategory> subCategories = new ArrayList<>();

    private final Random random = new Random();

    public MarketModel getAdvice(Gender gender, Age age) {
        List<Subcategory> suitableSubCategories = getSuitableSubcategories(gender, age);
        if (suitableSubCategories.isEmpty()) {
            return null;
        }
        Subcategory subCategory = suitableSubCategories.get(random.nextInt(suitableSubCategories.size()));

        List<MarketVendor> vendors = marketApiService.getVendors(subCategory);
        if (vendors.isEmpty()) {
            return null;
        }
        MarketVendor vendor = vendors.get(random.nextInt(vendors.size()));

        List<MarketModel> models = marketApiService.getModels(subCategory, vendor);
        if (models.isEmpty()) {
            return null;
        }
        return models.get(random.nextInt(models.size()));
    }

    private List<Subcategory> getSuitableSubcategories(Gender gender, Age age) {
        List<Subcategory> subcategories = persistenceService.getAllSubcategories();
        List<SubcategoryByPreferences> subcategoriesByPreferences = persistenceService.getAllSubcategoriesByPreferences();

        Set<Long> suitableSubcategoryIds = subcategoriesByPreferences.stream()
                .filter(subcategory -> gender == null || subcategory.getGenders().contains(gender))
                .filter(subcategory -> age == null || subcategory.getAges().contains(age))
                .map(SubcategoryByPreferences::getSubcategoryId)
                .collect(Collectors.toSet());

        return subcategories.stream()
                .filter(subcategory -> suitableSubcategoryIds.contains(subcategory.getId()))
                .collect(Collectors.toList());
    }
}
