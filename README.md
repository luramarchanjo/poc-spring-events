# Overview

This is a simple poc using the Spring Events.

# Testing

First, you need to start you application.

Second, you need to do some requests to simulate the variety of tests listed below:

```http request
### Listener at same Thread - Transaction Commit

GET http://localhost:8080/same-thread/transaction-commit

### Listener at same Thread - Transaction Rollback

GET http://localhost:8080/same-thread/transaction-rollback

### Listener at same Thread - Listener Error and Slow

GET http://localhost:8080/same-thread/listener-error

### Listener at different Thread - Transaction Commit

GET http://localhost:8080/other-thread/transaction-commit

### Listener at different Thread - Transaction Rollback

GET http://localhost:8080/other-thread/transaction-rollback
```

# Annotations

When you use the `@Async` and you try to manipulate the entity. It will be
at [detached state](https://openjpa.apache.org/builds/1.2.3/apache-openjpa/docs/jpa_overview_em_lifecycle.html)
.

# Be Happy