package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.MembershipOrder;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipOrderRepository extends JpaRepository<MembershipOrder, Long> {
    List<MembershipOrder> findByStatus(PaymentStatus status);
    Optional<MembershipOrder> findByTxnRef(String txnRef);

}
