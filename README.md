

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

