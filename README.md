# Selenium Cucumber BDD Automation Framework

🔃[Build Status](https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions)

## Project Overview

This is an **enterprise-grade test automation framework** built using:

* Selenium WebDriver (Java)
* Cucumber BDD (Gherkin)
* TestNG & JUnit (Dual Runner Support)
* Maven
* Docker Compose
* Selenium Grid 4
* GitHub Actions (CI/CD)
* Allure Reporting

The framework is designed to support **scalable UI automation**, **BDD practices**, and **continuous testing pipelines**.

## What This Repo Supports

* Local execution with Chrome or Firefox
* Remote execution on Selenium Grid 4
* Dockerized Grid using `docker-compose.yml`
* Fully containerized Maven test execution through a dedicated runner container
* Scenario-level parallel execution with TestNG + Cucumber
* Thread-safe driver management via `ThreadLocal`
* JUnit runner for simple serial execution
* Allure and Cucumber HTML/JSON reporting

## Execution Modes

`DriverFactory` is now property-driven, so the same suite can run locally, on Dockerized Grid, or on a cloud provider.

### Supported runtime properties

* `-Dbrowser=chrome|firefox`
* `-Dexecution.mode=local|grid|browserstack`
* `-Dgrid.url=http://localhost:4444`
* `-Dapp.base.url=http://localhost:8080`
* `-Dheadless=true|false`
* `-Dparallel.threads=4`
* `-Dplatform.name=linux`
* `-Dbrowser.version=latest`

## How to Run

### Local Chrome

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

### Local Firefox

```bash
mvn clean test -Dbrowser=firefox -Dheadless=true
```

### TestNG parallel execution

This uses the TestNG runner plus Cucumber scenario data providers in parallel mode.

```bash
mvn clean test -Ptestng-runner -Dparallel.threads=4 -Dbrowser=chrome -Dheadless=true
```

### Start Selenium Grid with Docker

```bash
docker compose up -d
```

If your machine uses the standalone Compose binary, use:

```bash
docker-compose up -d
```

That starts only the Grid services by default. The Maven test runner is isolated behind the `tests` profile so the hub and nodes can be reused across multiple runs.
The default local footprint is Chrome-only to keep Apple Silicon and Colima setups stable. Add the `cross-browser` profile when you explicitly want Firefox as well.
An `app` container also serves `src/app` over HTTP on port `8080`, which is required for remote browsers running in Grid containers.

Grid UI:

```text
http://localhost:4444
```

To scale Chrome nodes for higher throughput:

```bash
docker compose up -d --scale chrome=3 --scale firefox=1
```

For standalone Compose:

```bash
docker-compose --profile cross-browser up -d --scale chrome=3 --scale firefox=1
```

If you scale nodes, also increase the expected node count for the runner profile:

```bash
EXPECTED_GRID_NODES=4 docker compose --profile tests up --build --abort-on-container-exit test-runner
```

### Run tests on Dockerized Selenium Grid

From the host machine:

```bash
mvn clean test -Ptestng-runner -Dexecution.mode=grid -Dgrid.url=http://localhost:4444 -Dapp.base.url=http://localhost:8080 -Dbrowser=chrome -Dparallel.threads=3
```

Fully containerized, using the dedicated Maven runner:

```bash
docker compose --profile tests up --build --abort-on-container-exit test-runner
```

For standalone Compose:

```bash
docker-compose --profile tests up --build --abort-on-container-exit test-runner
```

Override browser and concurrency for the runner container:

```bash
BROWSER=firefox PARALLEL_THREADS=2 EXPECTED_GRID_NODES=2 docker compose --profile tests up --build --abort-on-container-exit test-runner
```

### Run tests on BrowserStack

```bash
mvn clean test -Ptestng-runner \
  -Dexecution.mode=browserstack \
  -Dbrowser=chrome \
  -Dbrowserstack.username=YOUR_USERNAME \
  -Dbrowserstack.accessKey=YOUR_ACCESS_KEY \
  -Dplatform.name=Windows \
  -Dbrowser.version=latest
```

## Parallel Execution Notes

* TestNG is the recommended runner for enterprise parallel execution in this repo.
* `BDDRunnerTestNG` uses a parallel Cucumber `DataProvider`.
* `parallel.threads` controls how many scenarios run at the same time.
* `ThreadLocal<WebDriver>` keeps driver instances isolated per scenario thread.
* For the most stable Grid runs, keep `SE_NODE_MAX_SESSIONS=1` and scale node count horizontally.

## Docker Grid Topology

The included `docker-compose.yml` provisions:

* `selenium-hub`
* `chrome` node
* `firefox` node
* `test-runner` profile for Maven-based execution inside Docker

This is a good baseline for local enterprise-style execution and mirrors the topology most teams later move into CI.

The runner waits on the Grid status API before starting Maven, which removes the usual race where tests begin before the hub has registered nodes.
The sample app is served by the `app` container so Grid browsers consume the same HTTP URL model you would use in CI or a shared environment.

## Reports

### Allure

```bash
mvn allure:report
```

### Cucumber HTML/JSON

Generated under `target/cucumber-results/` and `target/cucumber-report/`.

## Framework Architecture

* **Feature Files** -> Define behavior using Gherkin
* **Step Definitions** -> Map steps to automation logic
* **Page Object Model** -> Encapsulates UI interactions
* **DriverFactory** -> Local, Grid, and cloud driver creation
* **ThreadLocal WebDriver** -> Safe parallel browser sessions
* **JUnit/TestNG Runners** -> Serial and parallel execution options
* **Reporting Layer** -> Allure and Cucumber reports

---

## CI/CD

GitHub Actions is already configured to run the suite and publish Allure reports:

https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml

## Author

**Mohammad Majid**
If you find this project useful, please ⭐ the repository!
