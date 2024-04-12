package org.example.service;

import org.example.models.api.JokeRS;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JokeService {
    List<JokeRS> getJokes(Integer count);
}
