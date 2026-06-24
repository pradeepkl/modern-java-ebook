# Modern Java — The Mindset Shift

**Companion source code for the book**

| | |
|---|---|
| **Book** | *Modern Java — The Mindset Shift* |
| **Author** | Pradeep Kumar L |
| **Email** | [pradeep@classpath.in](mailto:pradeep@classpath.in) |
| **Edition** | First Edition, 2026 |

Runnable Java examples for every code listing in the book. Each chapter directory maps to that chapter's listings; file headers identify the listing number (for example, `Listing 1.1 — VarTypeInference.java`).

---

## Requirements

- **JDK 21 or later** (Temurin/Adoptium recommended)
- A terminal with `javac` and `java` on your `PATH`

Some examples use preview language features (instance `main` methods, pattern matching) and are compiled with `--enable-preview --release 21`. Install JDK 21+ from [adoptium.net](https://adoptium.net).

---

## Quick Start

```bash
git clone https://github.com/pradeepkl/modern-java-ebook.git
cd modern-java-ebook
```

### Flat chapters (1–4, 6–16)

Most listings are self-contained files in a flat package under `chapterXX/`:

```bash
cd chapter01
javac --enable-preview --release 21 *.java
java chapter01.VarTypeInference
```

Replace `chapter01` and `VarTypeInference` with the chapter and class you want to run. Each `.java` file declares its package (for example `package chapter01;`), so run classes with the fully qualified name.

### Chapter 5 — Java Platform Module System

Chapter 5 is a multi-module JPMS project. See [chapter05/README.md](chapter05/README.md) for the module layout and full listing paths.

```bash
cd chapter05
./compile.sh          # Git Bash or WSL on Windows
```

Listing 5.1 (`ListPlatformModules.java`) is standalone and uses instance `main`:

```bash
javac --enable-preview --release 21 ListPlatformModules.java
java ListPlatformModules
```

---

## Repository Layout

```
modern-java-ebook/
├── chapter01/ … chapter16/   # Book listings (one directory per chapter)
│   └── *.java                # Runnable source files
│   └── *.java.warning        # Compile notes from pipeline validation (optional)
├── chapter05/
│   ├── modules/              # Multi-module JPMS examples
│   ├── compile.sh            # Build canonical modules
│   └── README.md             # Chapter 5 listing index
├── SOURCE-CODE.md            # Full listing-to-file index (all chapters)
└── README.md                 # This file
```

**Finding a listing:** Open the chapter folder and look for the listing number in the file header, or consult [SOURCE-CODE.md](SOURCE-CODE.md) for a complete cross-reference.

**`.java.warning` files:** Sidecar notes recorded when a listing could not be compiled cleanly during book production. The companion `.java` file is still the authoritative listing from the book.

---

## Chapter Index

| Ch | Title | Directory | Listings |
|----|-------|-----------|----------|
| 1 | Modern Java: A Shift in Mindset | [`chapter01/`](chapter01/) | 3 |
| 2 | Writing Java the Modern Way | [`chapter02/`](chapter02/) | 6 |
| 3 | Inheritance Reimagined | [`chapter03/`](chapter03/) | 13 |
| 4 | Exception Handling the Modern Way | [`chapter04/`](chapter04/) | 22 |
| 5 | The Module System: Encapsulation as Enforceable Architecture | [`chapter05/`](chapter05/) | 24 |
| 6 | Pattern Matching in Modern Java | [`chapter06/`](chapter06/) | 18 |
| 7 | Primitive Types, Boxing, and the Cost of Abstraction | [`chapter07/`](chapter07/) | 9 |
| 8 | Designing Type-Safe APIs with Generics | [`chapter08/`](chapter08/) | 8 |
| 9 | Modern Date and Time | [`chapter09/`](chapter09/) | 13 |
| 10 | Concurrency Foundations and Coordination | [`chapter10/`](chapter10/) | 13 |
| 11 | Declarative and Structured Concurrency | [`chapter11/`](chapter11/) | 13 |
| 12 | Collections, Ownership, and State Safety | [`chapter12/`](chapter12/) | 11 |
| 13 | Declarative Data Transformations | [`chapter13/`](chapter13/) | 15 |
| 14 | Streams: Building Blocks of Declarative Pipelines | [`chapter14/`](chapter14/) | 22 |
| 15 | Streams: Orchestrating Pipelines | [`chapter15/`](chapter15/) | 11 |
| 16 | Design Patterns Reimagined with Modern Java | [`chapter16/`](chapter16/) | 8 |

**Total:** 209 listings across 16 chapters, plus supplementary examples in several chapters.

For per-listing file names (for example, Listing 4.15 → `TryWithResources.java`), see [SOURCE-CODE.md](SOURCE-CODE.md).

---

## Topics Covered

| Chapters | Topics |
|----------|--------|
| 1–2 | `var`, records, lambdas, method references, immutability, intent-oriented code |
| 3–4 | Sealed classes, interfaces, composition, modern exception design, try-with-resources |
| 5 | JPMS modules — `exports`, `requires`, `opens`, ServiceLoader, `jlink` |
| 6 | `instanceof` patterns, switch expressions, record patterns, guarded patterns |
| 7–8 | Primitives, boxing, `Optional`, generics, wildcards, PECS |
| 9 | `java.time` — `Instant`, `ZonedDateTime`, `Duration`, `Clock` |
| 10–11 | Threads, executors, atomics, `CompletableFuture`, structured concurrency, virtual threads |
| 12–13 | Immutable collections, defensive copying, enhanced Map APIs, comparators |
| 14–15 | Stream pipelines, collectors, parallel streams, custom collectors |
| 16 | Strategy, factory, builder, visitor, and other patterns with modern Java |

---

## Copyright and License

Copyright © 2026 Pradeep Kumar L. All rights reserved.

The code examples in this repository are provided for **educational purposes** alongside the book. Readers are encouraged to study, compile, and adapt them for learning and personal projects.

Java and all Java-based trademarks are trademarks of Oracle Corporation. This repository and the book are independent publications and are not affiliated with or endorsed by Oracle Corporation.

For permissions or inquiries: [pradeep@classpath.in](mailto:pradeep@classpath.in)

---

## Author

**Pradeep Kumar L** — software engineer, architect, trainer, and technical author with over 20 years of experience in enterprise systems, cloud platforms, and the modern Java ecosystem.

- Email: [pradeep@classpath.in](mailto:pradeep@classpath.in)
- LinkedIn: [linkedin.com/in/lpradeepk](https://linkedin.com/in/lpradeepk/)
- GitHub: [github.com/pradeepkl](https://github.com/pradeepkl)

---

## For Maintainers

This repository also contains the automated eBook production pipeline used to write and publish the book. See [PIPELINE.md](PIPELINE.md) for setup, agent commands, and KDP publishing workflow.
