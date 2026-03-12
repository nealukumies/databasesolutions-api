package fi.metropolia.neal.demo.repository;

import fi.metropolia.neal.demo.entity.PaymentDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailRepo extends JpaRepository<PaymentDetail, Integer> {
    List<PaymentDetail> findByOrderCustomerId(Integer customerId);
}