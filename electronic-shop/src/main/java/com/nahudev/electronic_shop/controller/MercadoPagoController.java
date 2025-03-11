package com.nahudev.electronic_shop.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.nahudev.electronic_shop.enums.OrderStatus;
import com.nahudev.electronic_shop.model.Order;
import com.nahudev.electronic_shop.model.Pay;
import com.nahudev.electronic_shop.repository.IOrderRepository;
import com.nahudev.electronic_shop.repository.IPayRepository;
import com.nahudev.electronic_shop.request.MercadoPagoPaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/mercado-pago")
public class MercadoPagoController {
    @Value("${mercadopago.access.token}")
    private String mercadoPagoToken;

    private final IOrderRepository orderRepository;

    private final IPayRepository payRepository;

    @PostMapping("/payment")
    public String getList(@RequestBody List<MercadoPagoPaymentRequest> paymentRequests) {

        if(paymentRequests == null || paymentRequests.isEmpty()) {
            return "error json :/";  // Si la lista está vacía o es null, devuelve un error.
        }

        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoToken);

            List<PreferenceItemRequest> items = new ArrayList<>();

            // Iterar sobre la lista de objetos de pago
            for (MercadoPagoPaymentRequest paymentRequest : paymentRequests) {

                // Crear el objeto de ítem de preferencia para cada pago
                PreferenceItemRequest itemRequest = PreferenceItemRequest
                        .builder()
                        .id(paymentRequest.getId())
                        .title(paymentRequest.getTitle())
                        .description(paymentRequest.getDescription())
                        .categoryId(paymentRequest.getCategoryId())
                        .quantity(paymentRequest.getQuantity())
                        .unitPrice(paymentRequest.getUnitPrice())
                        .currencyId("ARS")
                        .build();

                // Agregar el ítem a la lista de ítems
                items.add(itemRequest);
                System.out.println("Selling Price: " + paymentRequest.getUnitPrice());
            }

            // Crear la URL de redirección
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest
                    .builder()
                    .success("https://youtube.com")
                    .pending("https://youtube.com")
                    .failure("https://youtube.com")
                    .build();

            // Ensamblar la preferencia final
            PreferenceRequest preferenceRequest = PreferenceRequest
                    .builder()
                    .items(items)
                    .backUrls(backUrls)
                    .build();

            // Crear la preferencia a través del cliente de MercadoPago
            PreferenceClient preferenceClient = new PreferenceClient();
            Preference preference = preferenceClient.create(preferenceRequest);

            // Retornar el ID de la preferencia generada
            return preference.getId();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook (@RequestBody String json) {
        try {
            // Parsear la notificación de Mercado Pago
            JSONObject jsonObject = new JSONObject(json);
            String statusPay = jsonObject.getString("status"); // Estado del pago
            String mercadoPagoId = jsonObject.getString("id"); // ID del pago
            Long orderId = jsonObject.getLong("order_id"); // ID de la orden asociada

            // Buscar la orden
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

            Pay pay = new Pay();
              pay.setMercadoPagoId(Long.valueOf(mercadoPagoId));
              pay.setStatus(statusPay);
              pay.setTotalPrice(BigDecimal.valueOf(jsonObject.getDouble("transaction_amount")));
              pay.setPayDate(LocalDate.now());
              pay.setOrder(order);

            // Guardar el pago en la base de datos
              payRepository.save(pay);

            // Si el pago fue aprobado, actualizar la orden
            if ("approved".equals(statusPay)) {
                order.setOrderStatus(OrderStatus.PAGADO);
                orderRepository.save(order);
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al procesar el webhook");
        }
    }
}
