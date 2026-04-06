# Selenium Cucumber BDD Automation Framework

[![GitHub Actions](https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml/badge.svg)](https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml)
[![GitLab CI](https://gitlab.com/mmajid.automation/bddwithselenium/badges/main/pipeline.svg)](https://gitlab.com/mmajid.automation/bddwithselenium/-/pipelines)

## Project Overview

This is an **enterprise-grade test automation framework** built using:

* Selenium WebDriver (Java)
* Cucumber BDD (Gherkin)
* TestNG & JUnit (Dual Runner Support)
* Maven
* Docker Compose
* Selenium Grid 4
* GitHub Actions & GitLab CI/CD (Dual CI support)
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

### Execute Jenkinsfile: Complete Step-by-Step Guide

This repo includes a ready-to-use `Jenkinsfile` at the project root and a custom Jenkins Docker image with all required tooling.

#### Prerequisites

1. **Docker and Colima** (on macOS):
   ```bash
   brew install colima docker
   ```

2. **Repository cloned** to your local machine with all files including `Jenkinsfile`, `Dockerfile.jenkins`, and `docker-compose.yml`.

#### Step 1: Start Docker VM

```bash
colima start
```

#### Step 2: Start Jenkins Controller

Using the custom Jenkins image with Docker tooling preinstalled:

```bash
docker-compose --profile jenkins up -d --build jenkins
```

For standalone docker-compose:

```bash
docker-compose --profile jenkins up -d --build jenkins
```

**Verification**: Check Jenkins is running:
```bash
docker ps | grep jenkins
```

#### Step 3: Unlock Jenkins (First Time Only)

1. Get the initial admin password:
   ```bash
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```
   This outputs a token like: `b073197d8c2342a1bf77b1e1a7b499e5`

2. Open Jenkins in your browser:
   ```
   http://localhost:8081
   ```

3. Paste the token into the **Administrator password** field and click **Continue**.

4. Click **Install suggested plugins** (wait 2-3 minutes for installation).

5. Create your first admin user:
   - Username: `admin`
   - Password: (your choice)
   - Full name: (your choice)
   - Email: (your choice)
   - Click **Save and Continue**

6. Click **Save and Finish**.

#### Step 4: Create a Pipeline Job

1. In Jenkins dashboard, click **New Item**.

2. Enter Job name: `ui-grid-tests` (or any name you prefer).

3. Select **Pipeline** and click **OK**.

4. In the **Pipeline** section, configure:
   - **Definition**: select **Pipeline script from SCM**
   - **SCM**: select **Git**
   - **Repository URL**: paste your repository URL
     ```
     https://github.com/your-org/your-repo.git
     ```
     or for local filesystem (macOS):
     ```
     file:///Users/nafmjd/Documents/sample-work/Selenium Cucumber BDD Framework
     ```
   - **Branch**: `*/main` (or your branch)
   - **Script Path**: `Jenkinsfile` (default, keep as-is)

5. Click **Save**.

#### Step 5: Execute the Pipeline

1. In the job page, click **Build Now**.

2. Monitor the build progress:
   - Click the build number on the left (e.g., `#1`)
   - Click **Console Output** to watch logs in real-time
   - Pipeline stages and status appear as execution progresses

#### What the Pipeline Does

The `Jenkinsfile` automatically:

1. **Checks out** your repository code
2. **Starts Docker containers** for Selenium Grid, app, and test runner:
   ```
   docker-compose --profile tests up
   ```
3. **Runs TestNG + Cucumber tests** in the test-runner container with:
   - Chrome browser (headless)
   - Gridified execution against Selenium Hub
   - 3 parallel threads by default (configurable)
4. **Publishes test results**:
   - JUnit XML parsed and displayed in Jenkins UI
   - Test trends plotted over builds
5. **Archives all artifacts** from `target/` directory:
   - Cucumber HTML/JSON reports
   - Allure results
   - Surefire reports
6. **Cleans up** all running containers at the end (success or failure)

#### Step 6: Review Test Results

After the pipeline completes:

1. **Test Report** tab shows TestNG/JUnit results
2. **Artifacts** tab contains downloadable test reports
3. **Console Output** shows raw logs and errors (for debugging failures)

To download Cucumber HTML report:
- Click **Artifacts** → `cucumber-results/junit/cucumber-report.html`

To generate Allure visual report (optional, on your machine):
```bash
mvn allure:report
open target/site/allure-report/index.html
```

#### Jenkins Runtime Details

- **Java Version**: OpenJDK 21 (LTS) — no Java 17 EOL warnings
- **Docker Socket**: Mounted from host to Jenkins container for compose commands
- **Workspace**: Repository mounted at `/workspace` inside Jenkins container
- **Persistence**: Jenkins home stored in `jenkins_home` Docker volume (survives container restarts)

#### Troubleshooting

**Build fails with `docker-compose: command not found`**
- Rebuild Jenkins image:
  ```bash
  docker-compose --profile jenkins up -d --build jenkins
  ```

**Permission denied on `/var/run/docker.sock`**
- Restart Jenkins:
  ```bash
  docker-compose --profile jenkins restart jenkins
  ```

**Grid containers won't start**
- Verify Colima is running:
  ```bash
  colima status
  ```
- Check host Docker socket (should exist):
  ```bash
  ls -l /var/run/docker.sock
  ```

**Want to run tests locally instead?**
- Run directly without Jenkins:
  ```bash
  docker-compose --profile tests up --build --abort-on-container-exit test-runner
  ```

---

### Grid + TestNG Architecture

The custom Jenkins image includes both `docker` and `docker-compose`, allowing Jenkinsfile to orchestrate the full test stack:

- **Selenium Hub**: Central test node router on `http://selenium-hub:4444`
- **Chrome Node**: Runs tests in headless Chrome
- **App Container**: Serves test application on `http://app:80`
- **Test Runner**: Maven container running TestNG + Cucumber
- **Maven Cache**: Persistent volume to avoid re-downloading dependencies

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

The framework supports two CI/CD platforms with identical pipelines.

### GitHub Actions

Runs parallel JUnit and TestNG jobs and publishes Allure reports to GitHub Pages:

https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml

### GitLab CI/CD

Defined in `.gitlab-ci.yml` at the repository root. Mirrors the GitHub Actions workflow:

| Stage | Jobs | Description |
|---|---|---|
| `test` | `junit-headless` | Runs JUnit tests in headless Chrome, generates Allure report |
| `test` | `testng-headless` | Runs TestNG tests in headless Chrome (`-Ptestng-runner`), generates Allure report |
| `report` | `pages` | Copies Allure HTML into `public/` and deploys to GitLab Pages |

Both test jobs run in **parallel** on `maven:3.9.2-eclipse-temurin-17`, install Chromium, and upload Allure results as artifacts (retained 30 days).

**Pipeline:**
https://gitlab.com/mmajid.automation/bddwithselenium/-/pipelines

**Allure Reports (GitLab Pages):**
https://mmajid.automation.gitlab.io/bddwithselenium/

#### Trigger branches
- `main`
- `development`
- Merge requests

## Author

**Mohammad Majid**
If you find this project useful, please ⭐ the repository!
