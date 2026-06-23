# QE UI Module (`qe-ui/`)

## 🎯 Overview
This is a dedicated Maven module for all frontend (UI) automation testing for the Automation Exercise application. It drives real or headless browsers (Chrome, Firefox, etc.) to mimic user interactions using **Selenium WebDriver** and **TestNG**. 

The framework strictly adheres to the **Page Object Model (POM)** design pattern to separate web element locators from the test execution logic, ensuring scalability and low maintenance.

---

## 🏗️ Architecture & Folder Structure

The tests are categorized into four specific domains under `src/test/java/.../tests/` to support dynamic CI/CD execution strategies:

### 1. `smoke` (Happy Paths)
Proves that the core business features (Login, Cart, Checkout) are working when the user does everything correctly.
- **Execution Strategy:** Run on every pull request or minor commit.
- **Examples:** `AuthSmokeTests`, `CartTests`, `CheckoutTests`.

### 2. `negative` (Error Handling)
Proves that the application correctly handles human errors and blocks unauthorized behavior.
- **Execution Strategy:** Run during nightly regression.
- **Examples:** `NegativeAuthTests` (bad passwords), `SecurityRoutingTests` (unauthorized access to checkout).

### 3. `exhaustive` (Security & Boundaries)
Massive Data-Driven tests (via TestNG `@DataProvider`) designed to break the UI using extreme edge cases, HTML injection, SQL injection, and foreign character encoding.
- **Execution Strategy:** Run weekly or during dedicated security audits.
- **Examples:** `ExhaustiveAuthTests` (300+ char emails, Emojis), `ExhaustiveFormTests` (XSS payloads).

### 4. `misc` (Cross-Cutting & Infrastructure)
Tests that evaluate non-functional requirements or site-wide configurations.
- **Execution Strategy:** Run during environment sanity checks.
- **Examples:** `MiscCrossCuttingTests` (Browser console errors, mobile viewport responsive checks, broken links).

---

## 🚀 Execution Commands

Execute tests via the terminal using Maven. You can target specific TestNG groups or classes.

**1. Run the entire UI Test Suite (Headless):**
```bash
mvn test -pl qe-ui -Denv=qa -Dheadless=true
```

**2. Run ONLY Smoke Tests (Fast execution):**
```bash
mvn test -pl qe-ui -Dgroups="smoke" -Denv=qa -Dheadless=true
```

**3. Run ONLY Negative or Exhaustive Tests:**
```bash
mvn test -pl qe-ui -Dgroups="negative,exhaustive" -Denv=qa
```

**4. Run a specific test class:**
```bash
mvn test -pl qe-ui -Dtest="AuthSmokeTests" -Denv=qa
```

*(Note: Omit `-Dheadless=true` if you want to watch the Chrome browser physically open and execute the steps).*
