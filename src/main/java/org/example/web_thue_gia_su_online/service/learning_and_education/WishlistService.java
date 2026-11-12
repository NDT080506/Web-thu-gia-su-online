package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.WishlistItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishlistService {
    Page<WishlistItemResponse> getMyWishlist(Pageable pageable);

    WishlistItemResponse addToWishlist(String courseId);

    void removeFromWishlist(String courseId);

    boolean isWishlisted(String courseId);
}
