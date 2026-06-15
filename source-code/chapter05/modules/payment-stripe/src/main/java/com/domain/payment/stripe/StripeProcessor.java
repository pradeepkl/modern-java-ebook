// Java 9+
package com.domain.payment.stripe;

import com.domain.payment.api.PaymentProcessor;
import com.domain.payment.api.PaymentRequest;
import com.domain.payment.api.PaymentResult;
import java.util.logging.Logger;

// Public but NOT exported — invisible to consumers.
// Discoverable only through ServiceLoader.
public class StripeProcessor implements PaymentProcessor {

    private static final Logger log =
            Logger.getLogger(StripeProcessor.class.getName());

    @Override
    public PaymentResult process(PaymentRequest request) {
        log.info("Processing via Stripe: " + request.amount()
                + " for order: " + request.orderId());
        return PaymentResult.success(request.orderId());
    }
}
