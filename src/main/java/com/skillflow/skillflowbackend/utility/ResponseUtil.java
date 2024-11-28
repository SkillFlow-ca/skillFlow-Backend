package com.skillflow.skillflowbackend.utility;

import com.skillflow.skillflowbackend.dto.ResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
    public ResponseDto createResponse(String response, String status, String comment) {
        return ResponseDto.builder()
                .response(response)
                .status(status)
                .comment(comment)
                .build();
    }
}
