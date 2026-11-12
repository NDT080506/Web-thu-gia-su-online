package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    public ReviewResponse createReview(ReviewCreationRequest request);

    public Page<ReviewResponse> getTutorReviews(Pageable pageable);

    public ReviewResponse updateReview(ReviewUpdateRequest request);

    public void deleteMyReview();
}
