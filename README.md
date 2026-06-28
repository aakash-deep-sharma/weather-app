# Weather Alerts & 3-Day Prediction Dashboard

A resilient full-stack application built to fetch, aggregate, and visualize weather forecasts while maintaining absolute fault tolerance under heavy network failure states.

##  Key Features
- **3-Day Aggregated Data**: Automatically groups 3-hour raw forecasts into distinct daily outlook views.
- **Fault Tolerance**: Implements Resilience4j Circuit Breaker and Exponential Backoff Retry patterns.
- **Smart Two-Tier Cache Fallback**: Safely serves cached data if the remote API goes down, preventing broken states from poisoning the cache.
- **Modular Build Lifecycle**: Fully integrated Maven structure that builds the Angular frontend and injects it straight into the Spring Boot distribution JAR.

---

## Project Structure & Module Layout

```text
├── com.pub.sapient.modules (Parent Aggregator POM)
|   ├── com.pub.sapient
│   │   ├── src/main/docker-compose
│   │   ├── src/main/jenkins
│   │   └── pom.xml         (Parent project)
│   ├── com.pub.sapient.fe  (Angular Frontend Module Artifact)
│   │   ├── src/main/www    (Angular Source Root)
│   │   └── pom.xml         (Handles local Node isolation & asset compilation)
│   └── com.pub.sapient.be  (Spring Boot Backend Module Artifact)
│       ├── src/main/java   (Java Business Core)
│       └── pom.xml         (Handles Layered Docker Compilation)


*** Prerequisites & Configuration *** 
    Ensure you have the following installed locally:

    Java 21 (Eclipse Temurin JRE Recommended)

    Maven 3.9+

    Docker & Docker Compose

    Configuration Properties (application.yaml)
    The application looks for an environment property to handle API authorizations safely:


    weather:
    api:
        key: ${OPENWEATHER_API_KEY:e4b35f5360ad47afee8acab7e6154e78}
        base-url: [https://api.openweathermap.org](https://api.openweathermap.org)


*** Local Compilation & Build Instructions *** 
    To build the entire multi-module ecosystem (Frontend assets + Backend jar) from the project root directory, run:

    mvn clean package -Pnpm

    What this command does:
    Navigates to com.pub.sapient.fe.

    Downloads a standalone local Node/NPM instance using the exec-maven-plugin.

    Compiles the Angular production build with a custom context configuration path (--base-href=/web/).

    Moves the compiled resources directly into the root level of the artifact inside static/web/.

    Compiles com.pub.sapient.be, pulling in the frontend JAR as an internal dependency.

*** Containerization & Deployment ***

    This project uses modern Docker Multi-Stage builds and Spring Boot Layering (layertools) to separate dependencies from code changes, speeding up build and deployment times.

    1. Build the Application Docker Image
    Activate the docker compilation profile inside the backend module:

    mvn package -Pdocker

    2. Configure Environment Secrets
    Create an .env file in the same directory as your docker-compose.yml file:

    OPENWEATHER_API_KEY=your_actual_openweather_api_token

    3. Spin up the Stack
    Launch the application using Docker Compose:

    docker-compose up -d

    The application will boot up natively and can be accessed at:

    http://localhost:8080/web/index.html


*** Testing the Resilient Features ***
    You can easily test the system's resilience directly from the UI dashboard:

    1. Live Production Stream: Query Pune to fetch live data from the OpenWeather API.

    2. Cache Initialization: Once loaded, this data is saved to the local cache.

    3. Trigger Circuit Breaker: Toggle the Force Offline Mode switch on the UI and click Analyze Forecast. This forces an internal exception, tripping the circuit breaker.

    4. Verify Fallback: The UI will smoothly transition its header state to show cached data,   continuing to display the 3-day forecast without missing a beat.