#!/bin/sh

echo "Current date:"
date

echo "JAVA_OPTS=$JAVA_OPTS"

# Navigate to where layers are unpacked
cd /app

# Correctly execute the JarLauncher to read the unpacked layers
exec java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher