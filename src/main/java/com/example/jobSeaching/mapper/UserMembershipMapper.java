package com.example.jobSeaching.mapper;

import com.example.jobSeaching.dto.response.UserMembershipDTO;
import com.example.jobSeaching.entity.UserMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMembershipMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "membershipPlan.postLimit", target = "allowedPosts")
    @Mapping(source = "membershipPlan.type", target = "membershipType")
    UserMembershipDTO toDTO(UserMembership membership);
}
