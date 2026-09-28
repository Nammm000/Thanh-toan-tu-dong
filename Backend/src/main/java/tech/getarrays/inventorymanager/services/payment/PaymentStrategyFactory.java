package tech.getarrays.inventorymanager.services.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.getarrays.inventorymanager.dto.PaymentRequestDTO.PaymentMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentStrategyFactory {

    private final Map<PaymentMethod, PaymentStrategy> strategyMap;

    public PaymentStrategyFactory(List<PaymentStrategy> strategies) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getMethod,
                        s -> s
                ));
        log.info("Registered payment strategies: {}", strategyMap.keySet());
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        PaymentStrategy strategy = strategyMap.get(method);

        if (strategy == null) {
            log.error("Unsupported payment method: {}", method);
            throw new IllegalArgumentException(
                    "Unsupported payment method: " + method + ". Available methods: " + strategyMap.keySet()
            );
        }

        return strategy;
    }

    public Set<PaymentMethod> getAvailableMethods() {
        return Collections.unmodifiableSet(strategyMap.keySet());
    }

    public boolean isSupported(PaymentMethod method) {
        return strategyMap.containsKey(method);
    }
}
