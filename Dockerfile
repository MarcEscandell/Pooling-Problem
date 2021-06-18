# Pull image with OR-Tools and other dependencies installed
FROM marcescandellmari/or-tools:latest
# Copy project Spring-boot artifact
COPY ./build/libs/pooling-problem-0.0.1.jar /home/pooling-problem/
WORKDIR /home/pooling-problem/
# Spring boot connects to 8080 port by default
EXPOSE 8080
# Exploit (extract) jar file to avoid nested jars issue with
# OR-Tools when calling Loader.loadNativeLibraries();
# This also improves Sring boot service response
RUN jar -xf pooling-problem-0.0.1.jar
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
