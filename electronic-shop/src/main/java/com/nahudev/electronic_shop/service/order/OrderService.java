package com.nahudev.electronic_shop.service.order;

import com.nahudev.electronic_shop.dto.OrderDTO;
import com.nahudev.electronic_shop.enums.OrderStatus;
import com.nahudev.electronic_shop.exceptions.ResourceNotFoundException;
import com.nahudev.electronic_shop.model.Cart;
import com.nahudev.electronic_shop.model.Order;
import com.nahudev.electronic_shop.model.OrderItem;
import com.nahudev.electronic_shop.model.Product;
import com.nahudev.electronic_shop.repository.IOrderRepository;
import com.nahudev.electronic_shop.repository.IProductRepository;
import com.nahudev.electronic_shop.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final IOrderRepository orderRepository;

    private final IProductRepository productRepository;

    private final CartService cartService;

    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {

        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createItemOrder(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    public Order createOrder(Cart cart) {
        Order order = new Order();

        // Setter the user
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDIENTE);
        order.setOrderDate(LocalDate.now());

        return order;
    }

    public List<OrderItem> createItemOrder(Order order, Cart cart) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);

                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
                    );
                }).toList();
    }

    public BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(item -> item.getPrice()
                .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public OrderDTO convertToDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

}
