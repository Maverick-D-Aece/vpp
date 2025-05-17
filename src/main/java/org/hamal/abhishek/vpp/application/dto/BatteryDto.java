package org.hamal.abhishek.vpp.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record BatteryDto(
        @NotBlank(message = "Say my name!")
        String name,

        @Pattern(regexp = "^\\d+$", message = "Testing 1, 2, 3... INVALID POSTCODE")
        @JsonProperty("postcode")
        String postCode,

        @Pattern(regexp = "^[1-9]\\d*$", message = "I need some positivity")
        @JsonProperty("capacity")
        int wattCapacity
) {
}
