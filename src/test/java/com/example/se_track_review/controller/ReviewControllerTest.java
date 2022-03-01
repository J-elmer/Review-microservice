package com.example.se_track_review.controller;

import com.example.se_track_review.model.Review;
import com.example.se_track_review.repository.ReviewRepository;
import com.example.se_track_review.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewService reviewService;

    @Test
    void getAllReviews() {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/review/all")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Review> expected = new ArrayList<>();
        List<Review> actual;
        try {
            actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Review>>() {});
            assertEquals(expected, actual);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Test
    void testGetAllReviews() {
    }

    @Test
    void getReviewsWithMaxStars() {
    }

    @Test
    void getReviewsWithMinStars() {
    }

    @Test
    void newReview() {
    }

    @Test
    void updateReview() {
    }

    @Test
    void deleteReview() {
    }
}