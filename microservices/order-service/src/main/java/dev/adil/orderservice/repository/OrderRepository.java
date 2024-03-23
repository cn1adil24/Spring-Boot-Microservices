package dev.adil.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.adil.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
