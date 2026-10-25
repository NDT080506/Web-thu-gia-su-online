package org.example.web_thue_gia_su_online.mapper.learning_and_education;


import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "reviewerId", source = "reviewer.id")
    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "enrollmentId", source = "enrollment.id")
    ReviewResponse toResponse(Review review);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toReview(ReviewCreationRequest request);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateReview(
            ReviewUpdateRequest request,
            @MappingTarget Review review
    );
}
