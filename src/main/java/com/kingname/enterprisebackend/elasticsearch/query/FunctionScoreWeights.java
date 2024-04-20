package com.kingname.enterprisebackend.elasticsearch.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionScoreWeights {
    private Double companyNameWeight = 1000D;
    private Double synCompanyNameWeight = 1000D;
}
