# Project Specification: URL Shortener & Security Engine

## 1. Objective

Build a high-performance, resilient RESTful URL shortener to master distributed caching, persistent storage, race condition management, and protection against common application security vulnerabilities.

---

## 2. Architecture Overview

```text
Client ──► Rate Limiter ──► API Layer ──► Redis Cache ──► PostgreSQL DB

```

The system follows a **Cache-Aside Architecture** to achieve sub-10ms redirection latency while ensuring zero data loss upon memory resets.

---

## 3. Functional Requirements

* **FR-1: Shorten URL (`POST /api/v1/shorten`)**
* Accepts a payload containing a target `longUrl`.
* Validates URL safety and structural integrity.
* Generates a cryptographically secure, unique 8 character Nano ID.
* Persists the mapping in MongoDB and writes it to Redis with an explicit TTL.


* **FR-2: Redirect URL (`GET /{shortCode}`)**
* Checks Redis for the provided `shortCode`:
* **Cache Hit:** Instantly returns an HTTP `302 Found` redirect to the `longUrl`.
* **Cache Miss:** Queries MongoDB. If found, populates Redis and executes the redirect; otherwise, returns HTTP `404 Not Found`.




* **FR-3: Cache-Aside Management**
* Evicts or populates cache entries lazily based on lookup demand and configurable Time-To-Live (TTL) expiration rules.



---

## 4. Security & System Design Requirements

* **SEC-1: Malicious URL & Loop Protection**
* Sanitizes input payloads and enforces a maximum length of 2,048 characters.
* Blocks self-referencing host URLs to prevent infinite redirection loops.
* Validates incoming domains against custom/external domain blocklists.


* **SEC-2: Distributed Rate Limiting**
* Applies a Redis-backed Token Bucket or Sliding Window algorithm per IP/Token (e.g., maximum 10 creation requests per minute).
* Responds with HTTP `429 Too Many Requests` when limits are exceeded.


* **SEC-3: Cache Penetration Defense**
* Caches null or empty markers in Redis with a short TTL (e.g., 60 seconds) for non-existent short codes to prevent repeated database lookup attacks.


* **SEC-4: Non-Sequential Code Generation**
* Employs cryptographically secure random generators for code creation to block sequential enumeration and scraping.



---

## 5. Non-Functional Requirements

* **NFR-1: Performance:** Response times under 10ms for cache hits.
* **NFR-2: Data Integrity:** Database persistence ensures zero link loss during Redis container restarts.
* **NFR-3: Containerization:** Fully automated environment startup via Docker Compose.

---

## 6. Tech Stack

* **Language & Runtime:** Java 25 / Spring Boot 4
* **In-Memory Cache & Rate Limiter:** Redis 7+
* **Persistent Database:** MongoDB version 8.3.11
* **Integration Testing:** JUnit 5, MockMvc, Testcontainers