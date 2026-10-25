package org.example.web_thue_gia_su_online.controller.learning_and_education;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;


    @PostMapping
    public ApiResponse<ReviewResponse> createReview(@RequestBody @Valid ReviewCreationRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(request))
                .build();
    }

    @GetMapping("/tutor")
    public ApiResponse<List<ReviewResponse>> getTutorReviews(){
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewService.getTutorReviews())
                .build();
    }

    @PatchMapping
    public ApiResponse<ReviewResponse> updateReview(@RequestBody @Valid ReviewUpdateRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.updateReview(request))
                .build();
    }


    @DeleteMapping
    public ApiResponse<Void> deleteReview(){
        return ApiResponse.<Void>builder()
                .message("The review has been deleted")
                .build();
    }
}
