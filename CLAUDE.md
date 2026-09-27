# CLAUDE.md

Monorepo e-commerce : backend Spring Boot + frontend Angular.

> Règle de travail : **expliquer chaque modification avant de l'appliquer**. Le propriétaire du projet veut comprendre son code.

## Structure

```
E-commerce/
├── E-commerce_backend/            # Spring Boot 3.4.8, Java 21, Maven (mvnw)
│   ├── docker-compose.yml         # MySQL 8 seul (à déplacer à la racine, étape 4)
│   └── src/main/
│       ├── resources/application.properties
│       └── java/com/lazaar/ecommerce/
│           ├── Configuration/     # SecurityConfiguration, JwtAuthenticationFilter, EmailConfig
│           ├── Controllers/       # Auth, Product, ProductCategory, ProductStatus, Cart, WishList
│           ├── DTO/request|response + UserMapper
│           ├── Exception/         # GlobalExceptionHandler, TokenRefreshException, OTPExpiredException
│           ├── models/            # User, Role, Product, ProductCategory, ProductStatus, Cart, CartItems, WishList, RefreshToken
│           ├── Repositories/      # Spring Data JPA
│           ├── Security/          # ⚠ contient les INTERFACES de services (AuthenticationService, JwtService, CartService…)
│           └── Services/implSecurity/  # implémentations des services + EmailService, EmailVerificationService
└── E-commerce_frontend/           # Angular 14, basé sur le template "Apex" (beaucoup de pages de démo)
    └── src/
        ├── environments/          # environment.ts / environment.prod.ts (pas encore d'apiUrl)
        └── app/
            ├── pages/content-pages/   # login, register, forgot-password, otp-verification, reset-password…
            ├── pages/full-pages/      # pages de démo du template
            └── shared/auth/           # auth-service.service.ts (API Spring) + auth.service.ts (Firebase, à supprimer)
```

- API backend : `http://localhost:8087/api/v1/...` ; Swagger : `/swagger-ui.html`
- Auth : JWT (access + refresh token), vérification email, OTP pour mot de passe oublié.
- DB : MySQL `ecommerce`, `ddl-auto=update`.

## Commandes

```bash
# Backend
cd E-commerce_backend && ./mvnw spring-boot:run
cd E-commerce_backend && ./mvnw test
# Frontend
cd E-commerce_frontend && npm install && npm start      # http://localhost:4200
```

## Configuration / secrets

- Les secrets vivent dans `.env` à la racine (**jamais commité**) ; modèle dans `.env.example`.
- `application.properties` ne contient que des placeholders `${VAR}`.

## Plan de travail

1. [ ] Sécurité/config : sortir tous les secrets vers un `.env` (non commité) + `.env.example`
2. [ ] Corrections auth backend : rôles `ROLE_`, signup sans choix du rôle, reset password avec vérification OTP, gestion globale des erreurs
3. [ ] Auth frontend : supprimer Firebase, guard, intercepteur enregistré, `environment.ts` (apiUrl), refresh token
4. [ ] Dockerisation complète : backend, frontend (Nginx), MySQL, `docker-compose.yml` à la racine
5. [ ] Produits backend : DTO, lecture publique, CRUD admin
6. [ ] Admin front : produits / catégories
7. [ ] Boutique front
8. [ ] Panier
9. [ ] Wishlist
10. [ ] Commandes / checkout
11. [ ] Admin commandes / utilisateurs
12. [ ] Finitions