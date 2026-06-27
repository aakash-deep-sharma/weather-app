ARG DOCKER_BASE_IMAGE
ARG DOCKER_BASE_IMAGE_TAG

# --- Builder Stage ---
FROM ${DOCKER_BASE_IMAGE}:${DOCKER_BASE_IMAGE_TAG} as builder

ARG JAR_FILE
COPY $JAR_FILE /app.jar

RUN java -Djarmode=layertools -jar app.jar extract

# --- Final Stage ---
ARG DOCKER_BASE_IMAGE
ARG DOCKER_BASE_IMAGE_TAG

FROM ${DOCKER_BASE_IMAGE}:${DOCKER_BASE_IMAGE_TAG}

ARG DOCKER_ROOT_USER
ARG DOCKER_BASE_USER

VOLUME /tmp
VOLUME /base
VOLUME /logs

ENV JAVA_OPTS=""

USER $DOCKER_ROOT_USER

# Set WORKDIR early so relative paths work cleanly
WORKDIR /app

RUN chown -R $DOCKER_BASE_USER:$DOCKER_BASE_USER /app

# Copy scripts to root if needed, or adjust path
COPY --chown=$DOCKER_BASE_USER:$DOCKER_BASE_USER ./src/main/docker/bin/*.sh /

# Copy the extracted layers into the WORKDIR (/app)
COPY --chown=$DOCKER_BASE_USER:$DOCKER_BASE_USER --from=builder dependencies/ ./
COPY --chown=$DOCKER_BASE_USER:$DOCKER_BASE_USER --from=builder snapshot-dependencies/ ./
COPY --chown=$DOCKER_BASE_USER:$DOCKER_BASE_USER --from=builder spring-boot-loader/ ./
COPY --chown=$DOCKER_BASE_USER:$DOCKER_BASE_USER --from=builder application/ ./

RUN chmod 555 /entrypoint.sh

USER $DOCKER_BASE_USER

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "/entrypoint.sh" ]
