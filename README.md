# TaskFlow – Kanban simple (Spring Boot + React/Vite)

## Aperçu

TaskFlow est une application Kanban pour gérer des tâches (TODO / IN_PROGRESS / DONE) avec authentification JWT.
- Backend: API REST Spring Boot (H2 en mémoire, Spring Security + JWT)
- Frontend: React + Vite + Tailwind CSS

Objectif: fournir un tableau Kanban minimaliste où l’on peut se connecter, lister, créer et déplacer des tâches entre colonnes.

## Stack technique

- Langages:
  - Java (22, compatible 17+)
  - JavaScript (ES2020+)
- Backend:
  - Spring Boot 3.x (Web, Security, Data JPA)
  - Spring Security 6.x (JWT avec `io.jsonwebtoken`)
  - H2 Database (en mémoire, dev)
  - Maven
- Frontend:
  - React 18 + React Router 6
  - Vite (dev server & build)
  - Tailwind CSS 3 + PostCSS + Autoprefixer
  - Axios

## Structure du projet

Racine `taskflow/` (c’est ici que se trouve l’app Vite + le backend Spring):
```
.
├─ src/                      # Frontend React (Vite) + Backend Java dans src/main
│  ├─ main/java/...          # Code Java (Spring Boot)
│  ├─ main/resources/        # application.properties, templates, static ...
│  └─ ...
├─ index.html                # Entrée Vite
├─ package.json              # Scripts Vite (dev/build/preview)
├─ tailwind.config.js        # Config Tailwind
├─ postcss.config.js         # Config PostCSS
├─ pom.xml                   # Build Maven
└─ ...
```
Note: Il existe un répertoire `taskflow-frontend/` (Create React App). Il est historique et non utilisé. L’application active côté front est à la racine `taskflow/` avec Vite.

## Prérequis

- Java 17+ (Java 22 ok)
- Maven 3.8+
- Node.js 18+ et npm
- Windows PowerShell (les commandes ci-dessous sont pour Windows)

## Configuration par défaut

- Backend écoute sur: `http://localhost:8081`
- Frontend écoute sur: `http://localhost:5173` (Vite)
- Base de données: H2 en mémoire (réinitialisée à chaque démarrage)
- Console H2: `http://localhost:8081/h2-console`
  - JDBC URL: `jdbc:h2:mem:taskdb`
- JWT: clé de dev définie dans `src/main/resources/application.properties` (à remplacer en prod)

Dev user (créé automatiquement au démarrage):
- Username: `diag_user_live`
- Password: `DiagP@ss123`

## Démarrage rapide (2 terminaux)

1) Backend Spring Boot (port 8081)
```powershell
Set-Location 'C:\\Users\\youne\\OneDrive\\Desktop\\taskflow\\taskflow'
mvn -DskipTests spring-boot:run
```
Attendez le log: `Tomcat started on port 8081`.

2) Frontend Vite (port 5173)
```powershell
Set-Location 'C:\\Users\\youne\\OneDrive\\Desktop\\taskflow\\taskflow'
npm install
npm run dev
```
Ouvrez l’URL affichée (souvent `http://localhost:5173`).

Frontend -> Backend
- Par défaut, le frontend cible `http://localhost:8081/api` (voir `src/config.js`).
- Vous pouvez surcharger avec `VITE_API_BASE` si besoin.

## Utilisation

- Page Login -> connectez-vous avec `diag_user_live / DiagP@ss123`.
- Créez des tâches (Titre requis) et déplacez-les via drag & drop entre colonnes.
- Bouton Logout pour ré-initialiser la session côté front (le JWT est en localStorage).

## API (principaux endpoints)

Base API: `http://localhost:8081/api`

- Auth
  - `POST /api/auth/register` — body JSON `{ "username", "password" }`
  - `POST /api/auth/login` — body JSON `{ "username", "password" }` → `{ token }`
- Tasks (JWT requis)
  - `GET /api/tasks` — liste des tâches
  - `POST /api/tasks` — créer `{ "titre", "description", "dateEcheance"? }`
  - `PUT /api/tasks/{id}` — mettre à jour (ex: `{ "statut": "IN_PROGRESS" }`)

Note: le backend renvoie 401/403 si le JWT manque/est invalide; le frontend redirige vers /login dans ce cas.

## Configuration CORS

- CORS autorise par défaut l’origine `http://localhost:5173` (Vite) et `http://127.0.0.1:5173`.
- Si Vite choisit un autre port (ex: 5174), mettez à jour les origines autorisées côté backend (fichier `SecurityConfig` / `CorsConfig`) ou forcez Vite à utiliser 5173.

## Variables & config utiles

- `src/main/resources/application.properties`
  - `server.port=8081` — port backend
  - `jwt.secret` — clé HMAC (dev). Remplacez en prod.
  - `spring.h2.console.enabled=true` — console H2
- `src/config.js` (frontend)
  - `export const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8081/api'`

## Build & exécution (alternatives)

- Packager le backend:
```powershell
Set-Location 'C:\\Users\\youne\\OneDrive\\Desktop\\taskflow\\taskflow'
mvn -DskipTests package
```
- Lancer un aperçu du frontend buildé:
```powershell
npm run build
npm run preview
```

## Dépannage

- Port 8080 occupé: le backend est déjà configuré sur 8081. Si 8081 est pris, modifiez `server.port`.
- 401 juste après démarrage: H2 est vide et l’utilisateur de dev est créé au boot; attendez 2–5s et réessayez ou relancez le login.
- CORS/Network error dans le navigateur: assurez-vous d’ouvrir Vite depuis l’URL affichée (5173), et que le backend est bien sur 8081. Vérifiez que le JWT est présent côté front (sinon relog).
- CSS “absent”: Tailwind est configuré (`tailwind.config.js`, `postcss.config.js`, `src/index.css` importé dans `src/main.jsx`). Faites Ctrl+F5.

## Sécurité / Production (notes)

- Remplacez `jwt.secret` par une clé longue et aléatoire en prod.
- Changez l’auth (users en base persistante) et supprimez le `DevDataLoader` qui crée l’utilisateur de dev.
- Remplacez H2 par Postgres/MySQL en prod (déjà esquissé en commentaires dans `application.properties`).
- Ajustez précisément CORS (origines autorisées) pour votre domaine.

---

Identifiants de dev
- Username: `diag_user_live`
- Password: `DiagP@ss123`

Bonne utilisation !
