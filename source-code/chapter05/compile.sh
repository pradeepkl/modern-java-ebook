#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
javac \
  --module-source-path "order-api=source-code\chapter05\modules\order-api\src\main\java;order-impl=source-code\chapter05\modules\order-impl\src\main\java;billing=source-code\chapter05\modules\billing\src\main\java;payment-api=source-code\chapter05\modules\payment-api\src\main\java;payment-stripe=source-code\chapter05\modules\payment-stripe\src\main\java;app=source-code\chapter05\modules\app\src\main\java" \
  -d out \
  modules/app/src/main/java/module-info.java \
  modules/billing/src/main/java/com/domain/billing/BillingService.java \
  modules/billing/src/main/java/module-info.java \
  modules/order-api/src/main/java/com/domain/order/api/Order.java \
  modules/order-api/src/main/java/com/domain/order/api/OrderService.java \
  modules/order-api/src/main/java/com/domain/order/api/OrderStatus.java \
  modules/order-api/src/main/java/module-info.java \
  modules/order-impl/src/main/java/com/domain/order/impl/OrderServiceImpl.java \
  modules/order-impl/src/main/java/module-info.java \
  modules/payment-api/src/main/java/com/domain/payment/api/PaymentProcessor.java \
  modules/payment-api/src/main/java/module-info.java \
  modules/payment-stripe/src/main/java/com/domain/payment/stripe/StripeProcessor.java \
  modules/payment-stripe/src/main/java/module-info.java

# Standalone listing 5.1 (instance main — requires preview):
# javac --enable-preview --release 21 ListPlatformModules.java
