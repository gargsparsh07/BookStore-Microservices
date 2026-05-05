package com.bookstore.order.service;

import com.bookstore.order.dto.*;
import com.bookstore.order.entity.Order;
import com.bookstore.order.entity.OrderItem;
import com.bookstore.order.entity.OrderStatus;
import com.bookstore.order.exception.ResourceNotFoundException;
import com.bookstore.order.kafka.OrderEventPublisher;
import com.bookstore.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional
    public OrderResponse placeOrder(Long userId, OrderRequest request) {
        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        List<OrderItem> items = request.getItems().stream().map(i -> {
            OrderItem item = new OrderItem();
            item.setProductId(i.getProductId());
            item.setProductTitle(i.getProductTitle());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());
        order.setItems(items);
        order.setTotalAmount(items.stream()
                .map(i -> i.getUnitPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderPlaced(saved);
        return mapToResponse(saved);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        return mapToResponse(findOrder(id));
    }

    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = findOrder(id);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderStatusChanged(saved);
        return mapToResponse(saved);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = findOrder(id);
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderStatusChanged(saved);
        return mapToResponse(saved);
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id));
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemRequest> items = order.getItems().stream().map(i -> {
            OrderItemRequest item = new OrderItemRequest();
            item.setProductId(i.getProductId());
            item.setProductTitle(i.getProductTitle());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            return item;
        }).collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getUserId(),
                order.getStatus(), order.getTotalAmount(),
                order.getShippingAddress(), order.getCreatedAt(), items);
    }
}