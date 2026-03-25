# 🚀 Selenium Cucumber BDD Automation Framework (CI/CD + Allure Reporting)

![Build Status](https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml/badge.svg)

---

## 📊 Live Test Report

👉 **View Latest Allure Report:**
https://mohammadmajid.github.io/selenium-cucumber-bdd-framework/

✔ Automatically updated on every CI run
✔ Includes test results, trends, logs, and failure analysis

---

## 🧪 Project Overview

This is an **enterprise-grade test automation framework** built using:

* Selenium WebDriver (Java)
* Cucumber BDD (Gherkin)
* TestNG & JUnit (Dual Runner Support)
* Maven
* GitHub Actions (CI/CD)
* Allure Reporting

The framework is designed to support **scalable UI automation**, **BDD practices**, and **continuous testing pipelines**.

---

## 🔁 Dual Test Runner Support (JUnit + TestNG)

This framework supports executing the same test cases using both:

* ✅ **JUnit**
* ✅ **TestNG**

### 💡 Why This Matters

* Enables flexibility across team preferences
* Supports migration between frameworks without rewriting tests
* Demonstrates advanced framework design

### ⚙️ How It Works

* Test logic (Step Definitions + Page Objects) is framework-agnostic
* Separate runner classes handle execution
* Same feature files and step definitions are reused

### ▶️ Execution Options

Run with TestNG:

```bash
mvn test -DsuiteXmlFile=testng.xml
```

Run with JUnit:

```bash
mvn test -Dcucumber.options="--tags @Smoke"
```

---

## 🌐 Cross-Browser Testing Support

This framework supports execution across multiple browsers:

* ✅ Google Chrome
* ✅ Mozilla Firefox

### ⚙️ Implementation

* Browser selection is configurable
* WebDriver setup is abstracted in DriverFactory
* Same test suite runs across browsers

### ▶️ Execution Example

Run on Chrome:

```bash
mvn test -Dbrowser=chrome
```

Run on Firefox:

```bash
mvn test -Dbrowser=firefox
```

---

## 🏗 Framework Architecture

### Core Components

* **Feature Files** → Define behavior using Gherkin
* **Step Definitions** → Map steps to automation logic
* **Page Object Model (POM)** → Encapsulate UI interactions
* **DriverFactory (ThreadLocal)** → Thread-safe driver management
* **Test Runners (JUnit/TestNG)** → Control execution
* **WebDriver Layer** → Browser automation
* **Reporting Layer** → Allure Reports

### Execution Flow

Feature → Step Definitions → Page Objects → DriverFactory → WebDriver → Allure Report

---

## 🔄 CI/CD Pipeline

This project includes a fully automated CI/CD pipeline:

* Trigger: On every push
* Executes regression suite
* Generates Allure reports
* Publishes reports to GitHub Pages

### Tools Used

* GitHub Actions
* Maven
* Allure

👉 Workflow:
https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml

---

## ⚙️ How to Run Locally

```bash
git clone https://github.com/MohammadMajid/selenium-cucumber-bdd-framework.git
cd selenium-cucumber-bdd-framework
mvn clean test
```

---

## 📁 Project Structure

```text
src
 ├── main/java/com/seleniumcucumberbddframework
 │   ├── utlis (DriverFactory)
 │   ├── pages
 │   └── stepdefinitions
 ├── test/resources
 │   └── features
 └── runners
```

---

## 📸 Sample Report

*(Add screenshot here for best impact)*

```text
/screenshots/allure-report.png
```

---

## 🚀 Key Highlights

* ✔ BDD-based automation using Cucumber
* ✔ Scalable Page Object Model design
* ✔ Thread-safe WebDriver using ThreadLocal
* ✔ Dual runner support (JUnit + TestNG)
* ✔ Cross-browser testing (Chrome, Firefox)
* ✔ Integrated CI/CD pipeline (GitHub Actions)
* ✔ Automated report publishing (GitHub Pages)
* ✔ Real-time test visibility via Allure

---

## 💡 Future Enhancements

* Parallel execution optimization
* Dockerized test execution
* Selenium Grid / BrowserStack integration
* API + UI hybrid automation
* AI-driven test generation

---

## 👨‍💻 Author

**Mohammad Majid**
Senior QA / SDET | Automation | CI/CD | Cloud

---

## ⭐ Support

If you find this project useful, please ⭐ the repository!