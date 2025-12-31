package com.carmanagement.servlet;

import com.carmanagement.dto.response.ErrorResponse;
import com.carmanagement.exception.BadRequestException;
import com.carmanagement.exception.NotFoundException;
import com.carmanagement.exception.ValidationException;
import com.carmanagement.util.JsonUtil;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {
    
    protected static final String CONTENT_TYPE_JSON = "application/json";
    protected static final String CHARSET_UTF8 = "UTF-8";
    private static final int MAX_REQUEST_BODY_SIZE = 1024 * 1024;

    protected <T> T parseRequestBody(HttpServletRequest req, Class<T> clazz) {
        try {
            String contentLengthHeader = req.getHeader("Content-Length");
            if (contentLengthHeader != null) {
                try {
                    long contentLength = Long.parseLong(contentLengthHeader);
                    if (contentLength > MAX_REQUEST_BODY_SIZE) {
                        throw new BadRequestException("Request body exceeds maximum size of " + 
                                (MAX_REQUEST_BODY_SIZE / 1024) + " KB");
                    }
                } catch (NumberFormatException e) {
                }
            }
            
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            int totalSizeBytes = 0;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                int lineBytes = line.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
                totalSizeBytes += lineBytes;
                
                if (!firstLine || reader.ready()) {
                    totalSizeBytes += 1;
                }
                
                if (totalSizeBytes > MAX_REQUEST_BODY_SIZE) {
                    throw new BadRequestException("Request body exceeds maximum size of " + 
                            (MAX_REQUEST_BODY_SIZE / 1024) + " KB");
                }
                
                if (!firstLine) {
                    sb.append("\n");
                }
                sb.append(line);
                firstLine = false;
            }
            
            String json = sb.toString();
            if (json.trim().isEmpty()) {
                throw new BadRequestException("Request body is empty");
            }
            
            T result = JsonUtil.fromJson(json, clazz);
            if (result == null) {
                throw new BadRequestException("Failed to parse request body");
            }
            return result;
        } catch (JsonSyntaxException e) {
            throw new BadRequestException("Invalid JSON format");
        } catch (IOException e) {
            throw new BadRequestException("Error reading request body");
        }
    }

    protected void sendJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType(CONTENT_TYPE_JSON);
        resp.setCharacterEncoding(CHARSET_UTF8);
        
        PrintWriter writer = resp.getWriter();
        writer.print(JsonUtil.toJson(body));
        writer.flush();
    }

    // Send an error response based on the exception type.
    protected void sendError(HttpServletResponse resp, Exception e) throws IOException {
        ErrorResponse errorResponse;
        int status;
        
        if (e instanceof NotFoundException) {
            status = HttpServletResponse.SC_NOT_FOUND;
            errorResponse = new ErrorResponse(status, "Not Found", e.getMessage());
        } else if (e instanceof ValidationException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            ValidationException ve = (ValidationException) e;
            if (ve.hasFieldErrors()) {
                errorResponse = new ErrorResponse(status, "Validation Error", e.getMessage(), ve.getFieldErrors());
            } else {
                errorResponse = new ErrorResponse(status, "Validation Error", e.getMessage());
            }
        } else if (e instanceof BadRequestException) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            // Sanitize error message to prevent information disclosure
            String message = sanitizeErrorMessage(e.getMessage());
            errorResponse = new ErrorResponse(status, "Bad Request", message);
        } else {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            // Never expose internal error details to clients
            // Log the actual error server-side for debugging
            logError("Internal server error", e);
            errorResponse = new ErrorResponse(status, "Internal Server Error", 
                    "An unexpected error occurred. Please try again later.");
        }
        
        sendJson(resp, status, errorResponse);
    }

    private String sanitizeErrorMessage(String message) {
        if (message == null) {
            return "An error occurred";
        }
        
        message = message.replaceAll("([A-Za-z]:)?[\\\\/][^\\s]+", "[path]");
        message = message.replaceAll("at [^\\n]+", "");
        message = message.replaceAll("Caused by:.*", "");
        
        if (message.length() > 200) {
            message = message.substring(0, 197) + "...";
        }
        
        return message;
    }

    private void logError(String message, Exception exception) {
        System.err.println("ERROR: " + message);
        if (exception != null) {
            System.err.println("Exception: " + exception.getClass().getName());
            System.err.println("Message: " + exception.getMessage());
            exception.printStackTrace(System.err);
        }
    }
}

