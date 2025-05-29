#Common commands

#Start spring boot app from command line with profile

mvn spring-boot:run -Dspring-boot.run.profiles=local

Start core services order:
1. Discovery service
2. Config service
3. Gateway 
----------------------
That's a solid and ambitious goal — and you’re off to a strong start with the config service already running. Let’s break your goal into manageable progress steps so you're ready to deliver **an impactful 5-day microservices training** next week.

---

### ✅ Your Goal:

**Deliver a 5-day hands-on Spring Cloud microservices training with working demos.**

---

### 🧱 Foundation: What You Have Now

* ✔ Config service is running — great first step!

---

### 📅 Weekly Progress Plan to Be Ready by Next Week

#### **🔹 Today–Tomorrow (Day 1–2): Setup Core Microservices Stack**

* [x] ✅ **Service Registry (Eureka)**
* [x] ✅ **Gateway Service (Spring Cloud Gateway)**
* [x] ✅ Hook up Config Server to all services
* [x] Add `@RefreshScope` where needed

#### **🔹 Day 3: Basic Microservices (2-3 business services)**

* [ ] ✅ Product Service (sample REST CRUD)
* [ ] ✅ Order Service (talks to Product via REST)
* [ ] ✅ Use Feign Client or WebClient
* [ ] Configurable URLs via Config Server

#### **🔹 Day 4: Resilience + Observability**

* [ ] ✅ Circuit Breaker (Resilience4j)
* [ ] ✅ Retry / Fallback
* [ ] ✅ Sleuth & Zipkin for tracing
* [ ] ✅ Centralized logging (e.g., ELK or simple log file strategy)

#### **🔹 Day 5: Security + Final Integration**

* [ ] ✅ Add Spring Security + JWT Authentication
* [ ] ✅ One protected endpoint per service
* [ ] ✅ Secure Gateway to authenticate incoming requests
* [ ] ✅ Document APIs (Swagger or SpringDoc)

---

### 🎯 Bonus Tips for Training Delivery

* **Start each day with a 10-min concept overview.**
* **Then live-code or show working code**, let trainees code along.
* End with a **“What you built today” summary** and small assignments.
* Use **Postman** or **Swagger** to demo endpoints visually.
* **Prep fallback code** in case something breaks during demo.

---

Would you like help creating:

1. A template project setup for all services?
2. Sample YAMLs for config and Eureka registration?
3. A slide outline for the 5-day session?

Let me know — and let’s also log what you’ll get done **today** so we stay on track. What do you plan to finish before end of day?
