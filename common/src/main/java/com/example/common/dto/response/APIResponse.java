package com.example.common.dto.response;

import com.example.common.dto.APIStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema
@Getter
@NoArgsConstructor
public class APIResponse<S>{

	private APIStatus status;
    private S result;
    private String error;
  
	public void setError(String error) {
		this.error = error;
	}

	public APIResponse(S result) {
        this.status = APIStatus.OK;
        this.result = result;
    }

    public APIResponse(APIStatus status) {
        this.status = status;
    }

    public void setStatus(APIStatus status) {
        this.status = status;
    }

    public void setResult(S result) {
        this.result = result;
    }

    public boolean isSuccess() {
        if (status == null || status.getCode() == APIStatus.OK.getCode()) {
            return true;
        }
        return false;
    }
   

	@Override
	public String toString() {
		return "ServiceResponse [status=" + status + ", result=" + result + ", error=" + error + "]";
	}
}
