# CoreInteractionSystem

CoreInteractionSystem — это модульная Java-платформа на базе Spring Framework, предназначенная для оптимизации взаимодействия между банковской платформой и легаси core-системами.

## 📦 Архитектура модулей

| Модуль               | Назначение                                                                 |
|----------------------|----------------------------------------------------------------------------|
| `api-specs`          | OpenAPI 3.0 YAML-файлы: схемы, endpoints, SSOT                             |
| `inbound-api`        | Генерация REST-контроллеров из спецификаций                                |
| `outbound-client`    | Генерация WebClient / Feign API-клиентов                                   |
| `database`           | JPA + PostgreSQL/H2: локальное хранилище, fallback                         |
| `rate-limiter`       | Ограничение частоты обращений к core                                       |
| `circuit-breaker`    | Circuit Breaker / retry / resilience логика                                |
| `fallback-engine`    | Возврат дефолтных/кешированных ответов при сбоях core                      |
| `monitoring`         | AOP для логов, метрик, ошибок (Prometheus, Spring Boot Actuator)          |
| `request-deduplication` | Объединение одинаковых запросов в один core-вызов                        |
| `caching`            | Redis / Caffeine кеш — быстрый и конфигурируемый                          |

## 🧠 Ключевые компоненты

### 🗃 Кеш (`caching`)
- Использует Redis и/или Caffeine
- TTL, инвалидация, fallback при ошибках
- Общий кеш доступен из fallback-engine

### 🚦 Троттлинг (`rate-limiter`)
- Ограничение количества одновременных запросов
- Rate-limiting на уровне IP/endpoint/токена

### ♻️ Circuit Breaker (`circuit-breaker`)
- Resilience4j: автоматическое отключение проблемных сервисов
- Retry с экспоненциальной задержкой
- Статистика ошибок и логика восстановления

### 🧱 Fallback Engine
- Возврат дефолтных ответов
- Получение данных из локальной базы (`database`) или кеша (`caching`)
- AOP-обёртки для автоматического применения

### 📊 Monitoring
- Метрики через Spring Boot Actuator
- Подключение Prometheus и Grafana
- Логирование ошибок и латентности

## 📂 Генерация кода

Код для `inbound-api` и `outbound-client` генерируется автоматически из `api-specs` с помощью OpenAPI Generator.

## 🚀 Запуск

```bash
./mvn clean install
```
