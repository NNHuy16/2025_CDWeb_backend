package com.example.jobSeaching;

import com.example.jobSeaching.entity.MembershipPlan;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.repository.MembershipPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling
@SpringBootApplication
public class JobSeachingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobSeachingApplication.class, args);
	}



@Bean
public CommandLineRunner seedMembershipPlans(MembershipPlanRepository planRepository) {
	return args -> {
		if (planRepository.count() == 0) {
			List<MembershipPlan> plans = List.of(
					new MembershipPlan(MembershipType.BASIC, "Basic", 0, 0, 0),
					new MembershipPlan(MembershipType.SILVER, "Silver", 3, 30, 747500),
					new MembershipPlan(MembershipType.GOLD, "Gold", 10, 90, 1997500),
					new MembershipPlan(MembershipType.PLATINUM, "Platinum", 30, 180, 3747500),
					new MembershipPlan(MembershipType.DIAMOND, "Diamond", 80, 365, 6997500)
			);
			planRepository.saveAll(plans);
			System.out.println("Đã seed MembershipPlan thành công.");
		}
	};
	}
}

