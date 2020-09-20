package app.controller;

import app.data.presentrecipient.Age;
import app.data.presentrecipient.Gender;
import app.api.data.admin.SubcategoryByPreferencesRequest;
import app.api.data.admin.SubcategoryByPreferencesResponse;
import app.api.data.admin.SubcategoryRequest;
import app.api.data.admin.SubcategoryResponse;
import app.data.Subcategory;
import app.data.SubcategoryByPreferences;
import app.service.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class AdminController {
    private final PersistenceService persistenceService;

    @PutMapping(value = "/subcategory/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public SubcategoryResponse putSubcategory(@PathVariable long id, @RequestBody SubcategoryRequest request) {
         return toView(persistenceService.saveSubcategory(toDomain(id, request)));
    }

    @GetMapping(value = "/subcategory", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<SubcategoryResponse> getAllSubcategories() {
        return persistenceService.getAllSubcategories().stream()
                .map(AdminController::toView)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/subcategoryByPreferences/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE} )
    public SubcategoryByPreferencesResponse putSubcategoryByPreferences(@PathVariable long id, @RequestBody SubcategoryByPreferencesRequest request) {
        return toView(persistenceService.saveSubcategoriesByPreferences(toDomain(id, request)));
    }

    @GetMapping(value = "/subcategoryByPreferences", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public List<SubcategoryByPreferencesResponse> getAllSubcategoriesByPreferences() {
        return persistenceService.getAllSubcategoriesByPreferences().stream()
                .map(AdminController::toView)
                .collect(Collectors.toList());
    }

    private static Subcategory toDomain(long id, SubcategoryRequest subcategoryRequest) {
        return new Subcategory(id, subcategoryRequest.getName(), subcategoryRequest.getCategoryId());
    }

    private static SubcategoryResponse toView(Subcategory subcategory) {
        return new SubcategoryResponse(subcategory.getId(), subcategory.getName(), subcategory.getCategoryId());
    }

    private static SubcategoryByPreferences toDomain(long id, SubcategoryByPreferencesRequest subcategoryByPreferencesRequest) {
        return new SubcategoryByPreferences(
                id,
                subcategoryByPreferencesRequest.getGenders().stream()
                        .map(gender -> Gender.findByCode(gender))
                        .collect(Collectors.toSet()),
                subcategoryByPreferencesRequest.getAges().stream()
                        .map(age -> Age.findByCode(age))
                        .collect(Collectors.toSet())
        );
    }

    private static SubcategoryByPreferencesResponse toView(SubcategoryByPreferences subcategoryByPreferences) {
        return new SubcategoryByPreferencesResponse(
                subcategoryByPreferences.getSubcategoryId(),
                subcategoryByPreferences.getGenders().stream()
                        .map(Gender::getCode)
                        .collect(Collectors.toSet()),
                subcategoryByPreferences.getAges().stream()
                        .map(Age::getCode)
                        .collect(Collectors.toSet()));
    }
}
