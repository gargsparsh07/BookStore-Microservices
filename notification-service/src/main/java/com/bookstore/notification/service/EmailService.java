package com.bookstore.notification.service;

import com.bookstore.notification.event.OrderEvent;
import com.bookstore.notification.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    public void sendOrderConfirmation(OrderEvent event) {
        log.info("Sending order confirmation email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendShippingUpdate(OrderEvent event) {
        log.info("Sending shipping update email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendDeliveryConfirmation(OrderEvent event) {
        log.info("Sending delivery confirmation email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }

    public void sendWelcomeEmail(UserEvent event) {
        log.info("Sending welcome email to: {} name: {}",
                event.getEmail(), event.getName());
    }

    public void sendOrderCancelledEmail(OrderEvent event) {
        log.info("Sending order cancelled email for orderId: {} to userId: {}",
                event.getOrderId(), event.getUserId());
    }
}