package com.example.se_track_review.controller;

import com.example.se_track_review.model.Review;
import com.example.se_track_review.repository.ReviewRepository;
import com.example.se_track_review.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class ReviewControllerIntegrationTest {

    private static String idReview1;
    private static String idReview2;
    private static String idReview3;
    private static String idNewReview;
    private static Review reviewUnderTest1 = new Review(1, "test", 2, "It was okay");
    private static Review reviewUnderTest2 = new Review(2, "test", 4, "It was great");
    private static Review reviewUnderTest3 = new Review(3, "test", 5, "Great concert, great performer");
    private static List<Review> expected = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeAll
    void populateDatabase() {
        idReview1 = this.reviewRepository.save(reviewUnderTest1).getId();
        idReview2 = this.reviewRepository.save(reviewUnderTest2).getId();
        idReview3 = this.reviewRepository.save(reviewUnderTest3).getId();
        expected.add(reviewUnderTest1);
        expected.add(reviewUnderTest2);
        expected.add(reviewUnderTest3);
    }

    @Test
    @Order(1)
    void getAllReviews() {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(get("/review/all")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Review> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Review>>() {});
            assertEquals(actual, expected);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    void getReviewsWithNrOfStars() {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(get("/review/stars")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("stars", "2"))
                    .andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Review> expectedResult = new ArrayList<>();
        expectedResult.add(reviewUnderTest1);
        try {
            List<Review> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Review>>() {});
            assertEquals(actual, expectedResult);
            assertEquals(actual.size(), expectedResult.size());
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    void getReviewsWithMaxStars() {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(get("/review/max-stars")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("stars", "4"))
                    .andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Review> expectedResult = new ArrayList<>();
        expectedResult.add(reviewUnderTest1);
        expectedResult.add(reviewUnderTest2);
        try {
            List<Review> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Review>>() {});
            assertEquals(actual, expectedResult);
            assertEquals(actual.size(), expectedResult.size());
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    void getReviewsWithMinStars() {
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(get("/review/min-stars")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("stars", "4"))
                    .andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Review> expectedResult = new ArrayList<>();
        expectedResult.add(reviewUnderTest2);
        expectedResult.add(reviewUnderTest3);
        try {
            List<Review> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Review>>() {});
            assertEquals(actual, expectedResult);
            assertEquals(actual.size(), expectedResult.size());
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    void newReview() {
        NewReviewDTO addReview = new NewReviewDTO(2, "test", 3, "It was awesome");
        MvcResult mvcResult = null;
        try {
            mvcResult = mockMvc.perform(post("/review/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(addReview)))
                    .andExpect(status().isCreated()).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            idNewReview = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.response");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Review reviewInDb = this.reviewService.findById(idNewReview);
        assertEquals(addReview.getReviewText(), reviewInDb.getReviewText());
    }

    @Test
    @Order(6)
    void newReviewFailed() {
        NewReviewDTO reviewUnderTest4 = new NewReviewDTO(999, "test", 3, "What concert did I go to?");
        NewReviewDTO reviewUnderTest5 = new NewReviewDTO(1, "test", 6, "Better than possible!");
        try {
            mockMvc.perform(post("/review/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reviewUnderTest4)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockMvc.perform(post("/review/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reviewUnderTest5)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Order(7)
    void updateReview() {
        UpdateReviewDTO updateReviewDTO = new UpdateReviewDTO(idNewReview, 0, null, 4, "It was better");

        try {
            mockMvc.perform(put("/review/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateReviewDTO)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Review reviewInDb = this.reviewService.findById(idNewReview);
        assertEquals(updateReviewDTO.getReviewText(), reviewInDb.getReviewText());
    }

    @Test
    @Order(8)
    void deleteReview() {
        try {
            mockMvc.perform(delete("/review/delete")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("reviewId", idNewReview))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertThrows(NoSuchElementException.class, () -> this.reviewService.findById(idNewReview));
    }

    @AfterAll
    void removeData() {
        this.reviewRepository.deleteById(idReview1);
        this.reviewRepository.deleteById(idReview2);
        this.reviewRepository.deleteById(idReview3);
    }
}