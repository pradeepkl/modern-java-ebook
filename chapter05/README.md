# Chapter 5 — Java Modules

Runnable multi-module project from the book, plus pedagogical
`module-info.java` variants under `modules/*/examples/`.

## Layout

```text
chapter05/
  ListPlatformModules.java     # Listing 5.1 (standalone)
  modules/
    order-api/                 # API module (canonical)
    order-impl/                # Implementation module
    billing/                   # Consumer module
    payment-api/               # Payment API
    payment-stripe/            # Stripe implementation
    app/                       # Microservice entry module
    order/                     # Monolithic example (listing 5.2)
  compile.sh                   # Build canonical modules
  out/                         # Compiled module output
```

## Build

From this directory:

```bash
./compile.sh
```

Canonical modules: order-api, order-impl, billing, payment-api, payment-stripe, app.

## Listings

- Listing 5.1 — `ListPlatformModules.java`
- Listing 5.10 — `modules/billing/src/main/java/com/domain/billing/BillingService.java`
- Listing 5.11 — `modules/order-api/examples/listing-5.11-requires-transitive/module-info.java`
- Listing 5.11 — `modules/billing/examples/listing-5.11-requires-transitive-billing/module-info.java`
- Listing 5.12 — `modules/order-impl/examples/listing-5.12-exports-vs-opens/module-info.java`
- Listing 5.14 — `modules/order-impl/examples/listing-5.14-broad-opens/module-info.java`
- Listing 5.15 — `modules/order-impl/src/main/java/com/domain/order/impl/OrderValidator.java`
- Listing 5.16 — `modules/payment-api/src/main/java/com/domain/payment/api/PaymentProcessor.java`
- Listing 5.16a — `modules/payment-api/src/main/java/com/domain/payment/api/PaymentRequest.java`
- Listing 5.16b — `modules/payment-api/src/main/java/com/domain/payment/api/PaymentResult.java`
- Listing 5.17 — `modules/payment-api/src/main/java/module-info.java`
- Listing 5.18 — `modules/payment-stripe/src/main/java/com/domain/payment/stripe/StripeProcessor.java`
- Listing 5.19 — `modules/payment-stripe/src/main/java/module-info.java`
- Listing 5.2 — `modules/order/src/main/java/module-info.java`
- Listing 5.20 — `modules/order-impl/src/main/java/module-info.java`
- Listing 5.21 — `modules/order-impl/src/main/java/com/domain/order/impl/OrderServiceImpl.java`
- Listing 5.22 — `modules/app/src/main/java/module-info.java`
- Listing 5.3 — `modules/order-api/src/main/java/com/domain/order/api/OrderStatus.java`
- Listing 5.4 — `modules/order-api/src/main/java/com/domain/order/api/Order.java`
- Listing 5.5 — `modules/order-api/src/main/java/com/domain/order/api/OrderService.java`
- Listing 5.6 — `modules/order-api/src/main/java/module-info.java`
- Listing 5.7 — `modules/order-impl/examples/listing-5.7-basic/module-info.java`
- Listing 5.8 — `modules/order-api/examples/listing-5.8-targeted-exports/module-info.java`
- Listing 5.9 — `modules/billing/src/main/java/module-info.java`
