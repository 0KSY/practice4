package com.solo.bulletin_board.auth.utils;

import com.google.gson.Gson;
import com.solo.bulletin_board.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponder {

    private static void comment(HttpServletResponse response,
                                HttpStatus httpStatus,
                                ErrorResponse errorResponse) throws IOException{

        Gson gson = new Gson();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }

    public static void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus) throws IOException{

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusAndMessageFromHttpStatus(httpStatus);

        comment(response, httpStatus, errorResponse);
    }
}
