# Selenium Cucumber BDD Framework

[![UI Test Reports CI](https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml/badge.svg)](https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/actions/workflows/ui-test-reports.yml)

GitHub Pages report URL:

- `https://github.com/MohammadMajid/selenium-cucumber-bdd-framework/settings/pages`

## Run Tests And Reports

Prerequisites:

- Java 17
- Maven 3.9+

Run from the project root:

```bash
cd "$(git rev-parse --show-toplevel)"
```

### JUnit runner

The default Maven test execution uses `BDDRunnerJUnit`.

Run tests:

```bash
mvn clean test
```

Generate the Allure HTML report:

```bash
mvn -U allure:report
```

View the Allure HTML report:

```bash
cd target/site/allure-maven/junit
python3 -m http.server 8123
```

Open in browser:

```text
http://127.0.0.1:8123
```

Generated outputs:

- Cucumber JSON/HTML inputs: `target/cucumber-results/junit`
- Cluecumber HTML report: `target/cucumber-report/junit`
- Allure raw results: `target/allure-results/junit`
- Allure HTML report folder: `target/site/allure-maven/junit`

### TestNG runner

The TestNG execution uses the Maven profile `testng-runner` and runs `BDDRunnerTestNG`.

Run tests:

```bash
mvn -Ptestng-runner clean test
```

Generate the Allure HTML report:

```bash
mvn -U -Ptestng-runner allure:report
```

View the Allure HTML report:

```bash
cd target/site/allure-maven/testng
python3 -m http.server 8123
```

Open in browser:

```text
http://127.0.0.1:8123
```

Generated outputs:

- Cucumber JSON/HTML inputs: `target/cucumber-results/testng`
- Cluecumber HTML report: `target/cucumber-report/testng`
- Allure raw results: `target/allure-results/testng`
- Allure HTML report folder: `target/site/allure-maven/testng`

## Headless Mode

The test suite supports headless execution through Java system properties.

Available system properties:

- `-Dheadless=true` enables headless browser mode
- `-Dbrowser=chrome` runs tests with Chrome
- `-Dbrowser=firefox` runs tests with Firefox

### Headless JUnit run

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

### Headless TestNG run

```bash
mvn -Ptestng-runner clean test -Dbrowser=chrome -Dheadless=true
```

### GitHub Actions example

Use headless Chrome in CI:

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

Or for the TestNG runner:

```bash
mvn -Ptestng-runner clean test -Dbrowser=chrome -Dheadless=true
```

If you also want the Allure HTML report in GitHub Actions:

```bash
mvn -U allure:report
```

Or for the TestNG runner:

```bash
mvn -U -Ptestng-runner allure:report
```

## GitHub Actions

A ready-to-use GitHub Actions workflow is available at `.github/workflows/ui-test-reports.yml`.

What it does:

- runs on `push`, `pull_request`, and `workflow_dispatch`
- uses Java 17
- runs both the JUnit and TestNG runners in headless Chrome
- generates separate JUnit and TestNG Allure HTML reports
- uploads the test outputs as downloadable artifacts even if the tests fail
- publishes the Allure HTML reports to GitHub Pages on the default branch

Uploaded artifact names:

- `allure-results-junit`
- `allure-html-junit`
- `surefire-reports-junit`
- `allure-results-testng`
- `allure-html-testng`
- `surefire-reports-testng`

Published GitHub Pages structure:

- `/` landing page with links to both reports
- `/junit/` JUnit Allure report
- `/testng/` TestNG Allure report

The GitHub Pages deployment runs only outside pull requests and only on the repository default branch.

Update the badge and Pages URL placeholders at the top of this file after the repository is available on GitHub.

### GitHub Pages setup

If the workflow fails on `actions/configure-pages@v5` with a `Get Pages site failed` or `Not Found` error, the repository does not have Pages enabled yet.

You have two valid ways to fix that:

- Manual setup in GitHub: open repository `Settings` -> `Pages` -> under `Build and deployment`, set `Source` to `GitHub Actions`.
- Automatic setup from the workflow: create a repository secret named `PAGES_ADMIN_TOKEN` and store a Personal Access Token that can administer the repository and write Pages configuration.

The workflow is already configured to use `PAGES_ADMIN_TOKEN` when it exists and will call `actions/configure-pages@v5` with `enablement: true`.

## Notes

- Do not open the Allure report with a direct `file://` path or `open .../index.html`. Some sections such as Categories, Suites, and Timeline can fail to load correctly that way.
- Serve the generated Allure report folder with `python3 -m http.server` and open it through `http://127.0.0.1:<port>` instead.
- If port `8123` is already in use, start the server on another port such as `8124`.
