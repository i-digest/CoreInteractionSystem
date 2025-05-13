# CoreInteractionSystem

**CoreInteractionSystem** — это модульная Java-платформа на базе Spring Boot, предназначенная для оптимизации взаимодействия между банковской платформой и легаси core-системами с помощью кэширования, тротлинга, fallback-логики, идемпотентности и мониторинга.

---

## 📦 Архитектура модулей

| Модуль                   | Назначение                                                                 |
|--------------------------|----------------------------------------------------------------------------|
| `api-specs`              | OpenAPI 3.0 YAML-файлы: схемы, endpoints, SSOT                            |
| `commons`                | Сгенерированные DTO из спецификаций, переиспользуются в других модулях     |
| `inbound-api`            | REST-контроллеры, аннотации @RateLimiter, входящие точки                   |
| `outbound-client`        | WebClient / RestTemplate-клиенты для внешних core-сервисов                |
| `database`               | Локальное хранилище на Spring Data JPA, используется для fallback         |
| `circuit-breaker`        | Circuit Breaker-логика на базе Resilience4j с fallback-обёртками          |
| `fallback-engine`        | Централизованная логика возврата fallback-данных из базы                  |
| `monitoring`             | AOP-логгирование вызовов, сбор метрик с помощью Micrometer                |
| `request-deduplication`  | Аннотация @Idempotent + AOP-аспект предотвращения повторных запросов      |

---

## ⚙️ Ключевые особенности

- **Кэширование на уровне сервисов** (Redis, Caffeine)
- **RateLimiter на контроллерах** (`@RateLimiter`)
- **Circuit Breaker** через `CircuitBreakerExecutor`
- **Fallback из базы** при сбоях core-систем
- **@Idempotent** аннотация для POST-запросов с сохранением результата
- **Мониторинг и логгирование** через AOP и Micrometer

---

## 🚀 Быстрый старт

```bash
mvn clean install
