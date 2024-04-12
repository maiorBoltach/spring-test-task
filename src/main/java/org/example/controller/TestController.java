package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.models.api.ApiErrorRS;
import org.example.models.api.JokeRS;
import org.example.service.JokeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final JokeService jokeService;

    @GetMapping(value = "/jokes", produces = APPLICATION_JSON_VALUE)
    public List<JokeRS> getJokes(@Validated @RequestParam(name = "count", defaultValue = "5", required = false) Integer count) {
        return jokeService.getJokes(count);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorRS> handleException(Exception e) {
        return ResponseEntity.status(400)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiErrorRS(e.getMessage()));
    }
}
