package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.WishlistItemResponse;

public interface WishlistService {
    List<WishlistItemResponse> getMyWishlist();

    WishlistItemResponse addToWishlist(String courseId);

    void removeFromWishlist(String courseId);

    boolean isWishlisted(String courseId);
}
