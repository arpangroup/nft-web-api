## Kickstart Build the Project

````bash
 mvn clean install -DskipTests
````

## Project Structure:
- **Root project** (`pom.xml`): Maven [Aggregator]() project that is aggregating the 4 child modules so they can be built as a group.
- **REST project** (`react-demo-rest/pom.xml`): Spring Boot application that will host the REST services backing the UI. This project produces a JAR file that is included in the final Spring Boot executable JAR file.
- **Web project** (`react-demo-web/pom.xml`): Hosts the HTML, JS, styles and frontend dependencies. This project is built using [Vite]() . Vite is a modern toolchain that allows you to focus on code, not build tools. This project produces a JAR file containing static resources that is included in the final Spring Boot executable JAR file.
- **Parent project** (`react-demo-parent/pom.xml`): Parent POM project for REST, Web, and application modules that manages the dependency versions and common plugins
- **Application project** (`react-demo-app/pom.xml`): Maven JAR project that generates the final deployable JAR file. This project depends on the REST and Web projects


## Technology Stack Overview

### Web:
- **[React]()**: An open-source JavaScript library created by Facebook to build user interfaces. React uses JSX (an extension of JavaScript), which allows component-based JavaScript and HTML markup to be managed as a single unit.  
- **[Vite]()**: Vite is a modern toolchain that allows you to focus on code, not build tools
### Server:
- **[Spring Boot]()**:
### Database:
- **[MySQL]()**
### Build:
- Maven
- npm
### Testing:
- JUnit5
- Vitest


## ✅ Final Project Structure
````scss
nft-backend-api/
│
├── pom.xml             <-- ROOT POM (artifactId: nft-backend-api)
│
├── common/              # Shared config, DTOs, JPA base classes
│
├── user-service/        # User microservice
│
├── product-service/     # Product microservice
│
└── nft-app/             # Aggregator app (combines all services)

````


````scss
nft-backend-api/
├── pom.xml                <-- ROOT POM (artifactId: nft-backend-api)
├── common/
│   └── pom.xml            <-- CHILD module (artifactId: common)
├── user-service/
│   └── pom.xml
├── product-service/
│   └── pom.xml

````

