package com.example.jobSeaching.mapper;

import com.example.jobSeaching.dto.response.JobDTO;
import com.example.jobSeaching.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "employerName", expression = "java(job.getEmployer() != null ? job.getEmployer().getFullName() : null)")
    @Mapping(target = "status", expression = "java(job.getStatus().name())")
    JobDTO toDTO(Job job);
}


