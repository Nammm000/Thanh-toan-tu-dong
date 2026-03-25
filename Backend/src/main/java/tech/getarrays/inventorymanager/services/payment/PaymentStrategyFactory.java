package tech.getarrays.inventorymanager.services.payment;

import org.springframework.stereotype.Service;
import tech.getarrays.inventorymanager.dto.PaymentRequestDTO.PaymentMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentStrategyFactory {

    private final Map<PaymentMethod, PaymentStrategy> strategyMap;

    public PaymentStrategyFactory(
            List<PaymentStrategy> strategies
    ) {

        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getMethod,
                        s -> s
                ));
    }

    public PaymentStrategy getStrategy(
            PaymentMethod method
    ) {

        PaymentStrategy strategy =
                strategyMap.get(method);

        if (strategy == null) {
            throw new RuntimeException(
                    "Unsupported payment method"
            );
        }

        return strategy;
    }
}
