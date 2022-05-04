package com.example.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class APIStatus {
	
	public static final APIStatus OK = new APIStatus(200, "OK");
    public static final APIStatus INVALID_TOKEN = new APIStatus(201, "Invalid token, please login again");
    public static final APIStatus ACCEPTED = new APIStatus(202, "Accepted");
    public static final APIStatus NO_CONTENT = new APIStatus(204, "No content");    
    public static final APIStatus NOT_MODIFIED = new APIStatus(304, "Not Modified");    
    public static final APIStatus BAD_REQUEST = new APIStatus(400, "Bad request");
    public static final APIStatus UNAUTHORIZED = new APIStatus(401, "Unauthorized");
    public static final APIStatus FORBIDDEN = new APIStatus(403, "Forbidden");
    public static final APIStatus NOT_FOUND = new APIStatus(404, "Not found");
    public static final APIStatus METHOD_NOT_ALLOWED = new APIStatus(405, "Method Not Allowed");
    public static final APIStatus NOT_ACCEPTABLE = new APIStatus(406, "Not Acceptable");
    public static final APIStatus REQUEST_TIME_OUT = new APIStatus(408, "Request Time Out");    
    public static final APIStatus SERVICE_ERROR = new APIStatus(500, "Internal server error");
    public static final APIStatus RUNNING_TIME_ERROR = new APIStatus(501, "Running time error");

    @Schema(example = "200")
    private int code;
    @Schema(example = "OK")
    private String status;

    public APIStatus() {
        this.code = OK.code;
        this.status = OK.status;
    }

    public APIStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }
    
    public APIStatus(APIStatus serviceStatus) {
    	this.code = serviceStatus.getCode();
    	this.status = serviceStatus.getStatus();
    }

    public APIStatus(APIStatus serviceStatus, String message) {
        this.code = serviceStatus.getCode();
        this.status = serviceStatus.getStatus();
        if (message != null && !message.trim().isEmpty()) {
            this.status = message;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

}
