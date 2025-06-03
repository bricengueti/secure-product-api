# Étape 1 : Extraction du contenu du jar
FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl as builder

WORKDIR /exploded

ARG JAVA_FILE=target/*.jar

COPY ${JAVA_FILE} app.jar

RUN unzip app.jar

# Étape 2 : Image exécutable minimale
FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl

ENV JAVA_OPTS="-Djava.awt.headless=true"

ARG PROJECT_ID

# Copier uniquement les ressources nécessaires
COPY --from=builder /exploded/BOOT-INF/lib /app/lib
COPY --from=builder /exploded/BOOT-INF/classes /app/classes
COPY --from=builder /exploded/META-INF /app/META-INF

# Optionnel : ajout d'infos système à des fins de debug/trace
RUN echo '{"projectId": "'${PROJECT_ID}'", "java_version": "'$(java -version 2>&1 | tail -1)'"}' > /external-info.json

# Lancement de l'application
ENTRYPOINT ["java", "-XX:+AlwaysActAsServerClassMachine", "-Djava.awt.headless=true", "-cp", "/app/classes:/app/lib/*", "com.TNBtech.secure_product_api.SecureProductApiApplication"]
