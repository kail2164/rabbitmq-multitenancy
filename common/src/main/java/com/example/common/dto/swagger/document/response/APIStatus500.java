package com.example.common.dto.swagger.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIStatus500 {
	@Schema(example = "500")
    private int code;
    @Schema(example = "Internal Server Error")
    private String status;
}
