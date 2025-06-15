package com.example.jobSeaching.mapper;

import com.example.jobSeaching.dto.response.MembershipOrderDTO;
import com.example.jobSeaching.entity.MembershipOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface MembershipOrderMapper {
    MembershipOrderDTO toDTO(MembershipOrder order);
}
