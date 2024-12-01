package com.cs597.project.splitwise.advices;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
}
