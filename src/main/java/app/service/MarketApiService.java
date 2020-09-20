package app.service;

import app.api.data.market.MarketModelResponse;
import app.api.data.market.MarketModelsPageResponse;
import app.data.MarketModel;
import app.data.Subcategory;
import app.api.data.market.MarketVendorResponse;
import app.data.MarketVendor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarketApiService {
    private final ObjectMapper objectMapper;

    private final static String MARKET_TOKEN = "Token 21214bc6f085e8261ef61b4342606071134af15b";
    private final static String VENDORS_URL_TEMPLATE = "https://apimarket.parserdata.ru/spec/%d/%d";
    private final static String MODELS_URL_TEMPLATE = "https://apimarket.parserdata.ru/spec/%d/%d/%d";

    public List<MarketVendor> getVendors(Subcategory subCategory) {
        String url = String.format(VENDORS_URL_TEMPLATE, subCategory.getCategoryId(), subCategory.getId());
        ResponseEntity<String> response = makeRequest(url);
        List<MarketVendorResponse> vendors;
        try {
            vendors = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            vendors = emptyList();
            log.error("An error occurred while deserializing the response from the market", e);
        }
        return vendors.stream()
                .map(MarketApiService::toDomain)
                .collect(Collectors.toList());
    }

    public List<MarketModel> getModels(Subcategory subCategory, MarketVendor vendor) {
        String url = String.format(MODELS_URL_TEMPLATE, subCategory.getCategoryId(), subCategory.getId(), vendor.getId());
        ResponseEntity<String> response = makeRequest(url);
        List<MarketModelResponse> models;
        try {
            MarketModelsPageResponse modelsPage;
            modelsPage = objectMapper.readValue(response.getBody(), MarketModelsPageResponse.class);
            models = modelsPage.getResults();
        } catch (JsonProcessingException e) {
            models = emptyList();
            log.error("An error occurred while deserializing the response from the market", e);
        }
        return models.stream()
                .map(MarketApiService::toDomain)
                .collect(Collectors.toList());
    }

    private ResponseEntity<String> makeRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", MARKET_TOKEN);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    private static MarketVendor toDomain(MarketVendorResponse vendorResponse) {
        return new MarketVendor(vendorResponse.getId(), vendorResponse.getName());
    }

    private static MarketModel toDomain(MarketModelResponse modelResponse) {
        return new MarketModel(modelResponse.getLink());
    }
}
