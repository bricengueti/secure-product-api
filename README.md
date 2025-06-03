# SecureProductAPI

SecureProductAPI est une API de gestion de produits construite avec Spring Boot, offrant une authentification robuste par JWT (token d'accès + refresh token), une architecture modulaire, et un support multienvironnement via des profils (`dev`, `prod`, `staging`).

## 📄 Table des Matières

* ✨ Fonctionnalités
* 🔧 Technologies
* 🚀 Lancement rapide
* 🔐 Authentification JWT
* 🛋️ Configuration multi-environnement
* 📁 Structure du projet

---

## ✨ Fonctionnalités

* Authentification par JWT avec access/refresh token
* Gestion des utilisateurs, produits, catégories
* Endpoints RESTful sécurisés
* Rafraîchissement de token
* Swagger UI pour tester l'API
* Support multienvironnement : `dev`, `prod`, `staging`

---

## 🔧 Technologies

* Java 17
* Spring Boot 3+
* Spring Security
* JWT
* Maven
* Docker (optionnel)

---

## 🚀 Lancement rapide

### Prérequis

* Java 17
* Maven 3.6+
* Docker (si vous utilisez la containerisation)

### Lancement local

```bash
git clone https://github.com/votre-utilisateur/secure-product-api.git
cd secure-product-api
mvn clean install
mvn spring-boot:run
```

### Avec Docker

```bash
docker-compose up --build
```

---

## 🔐 Authentification JWT

### Enregistrement

```http
POST /auth/register
Content-Type: application/json
{
  "username": "jane",
  "password": "securepass",
  "email": "jane@example.com"
}
```

### Connexion

```http
POST /auth/login
Content-Type: application/json
{
  "username": "jane",
  "password": "securepass"
}
```

### Rafraîchir le token

```http
POST /auth/refresh-token
{
  "refreshToken": "..."
}
```

---

## 🛋️ Configuration multi-environnement

Le projet charge automatiquement le fichier de configuration selon le profil actif :

* `application-dev.properties` pour le développement
* `application-prod.properties` pour la production
* `application-staging.properties` pour la préproduction

### Pour définir l'environnement actif :

```bash
# via la ligne de commande
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# ou en variable d'environnement
SPRING_PROFILES_ACTIVE=prod
```

---

## 📁 Structure du projet

```
secure-product-api/
├── config/                      # Configuration de sécurité, Swagger
├── controller/                 # Endpoints REST (Auth, User, Product...)
├── DTO/                        # Objets de transfert de données
├── entities/                   # Entités JPA
├── mapper/                     # Mappers (si utilisation MapStruct)
├── repositories/               # Accès aux données (Spring Data JPA)
├── services/                   # Logique métier
├── utils/                      # Utilitaires divers
├── resources/                  # Fichiers de configuration
│   ├── application.properties
│   ├── application-dev.properties
│   ├── application-prod.properties
│   └── application-staging.properties
├── docker-compose.yml          # Déploiement conteneurisé
└── pom.xml                     # Dépendances Maven
```

---

3. Utiliser dans vos services :

```java
messageSource.getMessage("user.created", null, locale);
```

---

## 📜 Licence

Ce projet est distribué sous licence MIT.
