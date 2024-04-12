package org.example.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JokeRS {
    @JsonProperty(value = "count", required = true)
    private String type;

    @JsonProperty(value = "setup", required = true)
    private String setup;

    @JsonProperty(value = "punchline", required = true)
    private String punchline;
}
