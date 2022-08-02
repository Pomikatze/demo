package com.example.demo.repositories;

import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findAllByOrderIdAndUserId(Long orderId, Long userId);

    List<OrderEntity> findAllByOrganizationAndUserId(OrganizationEntity organization, Long userId);

    Optional<OrderEntity> findByIdAndUserId(Long id, Long userId);

    Optional<List<OrderEntity>> findAllByUserId(Long userId);
}
