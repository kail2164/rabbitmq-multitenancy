package com.example.common.dto.swagger.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIStatus400 {
	@Schema(example = "400")
    private int code;
    @Schema(example = "Bad Request")
    private String status;
}
