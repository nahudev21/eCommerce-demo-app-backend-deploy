package com.nahudev.electronic_shop.service.order;

import com.nahudev.electronic_shop.dto.OrderDTO;
import com.nahudev.electronic_shop.model.Order;

import java.util.List;

public interface IOrderService {

    public Order placeOrder(Long userId);

    public OrderDTO getOrder(Long orderId);

    public List<OrderDTO> getUserOrders(Long userId);

    public OrderDTO convertToDto(Order order);
}
