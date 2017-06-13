package com.andrianova.url.shortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by natal on 13-Jun-17.
 */
public abstract class AbstractControllerTest {
    protected MockMvc mockMvc;

    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
