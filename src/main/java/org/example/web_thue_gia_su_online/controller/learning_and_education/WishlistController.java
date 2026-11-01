package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.WishlistItemResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.WishlistService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {

    WishlistService wishlistService;

    @GetMapping
    public ApiResponse<List<WishlistItemResponse>> getMyWishlist() {
        return ApiResponse.<List<WishlistItemResponse>>builder()
                .result(wishlistService.getMyWishlist())
                .build();
    }

    @PostMapping("/{courseId}")
    public ApiResponse<WishlistItemResponse> addToWishlist(@PathVariable String courseId) {
        return ApiResponse.<WishlistItemResponse>builder()
                .result(wishlistService.addToWishlist(courseId))
                .build();
    }

    @DeleteMapping("/{courseId}")
    public ApiResponse<Void> removeFromWishlist(@PathVariable String courseId) {
        wishlistService.removeFromWishlist(courseId);
        return ApiResponse.<Void>builder()
                .message("Item removed from wishlist successfully")
                .build();
    }

    @GetMapping("/check/{courseId}")
    public ApiResponse<Boolean> isWishlisted(@PathVariable String courseId) {
        return ApiResponse.<Boolean>builder()
                .result(wishlistService.isWishlisted(courseId))
                .build();
    }
}
