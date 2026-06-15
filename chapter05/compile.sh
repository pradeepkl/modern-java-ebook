#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
rm -rf out && mkdir -p out

# order-api
javac -d out --module-path out \
  --module-source-path "com.domain.order.api=modules/order-api/src/main/java" \
  $(find modules/order-api/src/main/java -name '*.java' | sort)

# payment-api
javac -d out --module-path out \
  --module-source-path "com.domain.payment.api=modules/payment-api/src/main/java" \
  $(find modules/payment-api/src/main/java -name '*.java' | sort)

# order-impl
javac -d out --module-path out \
  --module-source-path "com.domain.order.impl=modules/order-impl/src/main/java" \
  $(find modules/order-impl/src/main/java -name '*.java' | sort)

# billing
javac -d out --module-path out \
  --module-source-path "com.domain.billing=modules/billing/src/main/java" \
  $(find modules/billing/src/main/java -name '*.java' | sort)

# payment-stripe
javac -d out --module-path out \
  --module-source-path "com.domain.payment.stripe=modules/payment-stripe/src/main/java" \
  $(find modules/payment-stripe/src/main/java -name '*.java' | sort)

# app
javac -d out --module-path out \
  --module-source-path "com.domain.app=modules/app/src/main/java" \
  $(find modules/app/src/main/java -name '*.java' | sort)

# Standalone listing 5.1 (instance main — requires preview):
# javac --enable-preview --release 21 ListPlatformModules.java
