package app.api.data.market;

import app.api.data.market.MarketModelResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MarketModelsPageResponse {
    private List<MarketModelResponse> results;
}
