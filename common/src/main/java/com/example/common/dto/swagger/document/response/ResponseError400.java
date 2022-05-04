package com.example.common.dto.swagger.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError400 {
	@Schema
	private APIStatus400 status;
	@Schema(example = "Bad Request")
    private String error;
	@Schema(example = "false")
	private boolean success = false;
}