# Étape 1: Build de l'application
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Variables d'environnement pour Maven
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository -Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8"

# Répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml pour le cache des dépendances
COPY pom.xml .

# Télécharger les dépendances (optimisation cache)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src/ ./src/

# Construire l'application
RUN mvn clean package -DskipTests -B

# Étape 2: Image de production
FROM eclipse-temurin:17-jre-alpine

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Installer curl pour les health checks
RUN apk add --no-cache curl

# Répertoire de travail
WORKDIR /app

# Créer les dossiers nécessaires avec les bonnes permissions
RUN mkdir -p uploads/images && \
    chown -R appuser:appgroup /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Changer vers l'utilisateur non-root
USER appuser

# Exposer le port
EXPOSE 8080

# Point de santé
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Commande de démarrage
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]