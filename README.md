# Gestion de Formation – Excellent Training · Green Building

Application full-stack de gestion des formations, avec un back-end Spring Boot (Java) et un front-end React.

---

## Introduction

Cette plateforme permet de gérer l’ensemble du cycle de vie des formations :  
catalogue des formations, gestion des participants, formateurs (internes/externes), structures, employeurs, profils, utilisateurs et rôles.  

Elle inclut également un tableau de bord statistique (nombre de formations par année, répartition par profil, etc.).

### Technologies utilisées
- **Back-end** : Spring Boot 3.5.13, Spring Data JPA, Spring Security, MySQL  
- **Front-end** : React 19 (create-react-app)  
- **Base de données** : MySQL 8  

---

## Prérequis

- Java 17 ou 21  
- Maven  
- Node.js (v18+)  
- MySQL Server 8  
- IDE (IntelliJ, VS Code, Eclipse)

---

## Configuration base de données

```sql
CREATE DATABASE gestion_formation;
```

Modifier `application.properties` :

```
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
server.port=8888
```

---

## Lancement

### Backend
```
./mvnw spring-boot:run
```

### Frontend
```
cd frontend
npm install
npm start
```

Accès : http://localhost:3000

---

## Licence

MIT
