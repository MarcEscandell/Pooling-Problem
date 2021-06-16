FROM marcescandellmari/or-tools:latest
COPY ./out/artifacts/pooling_problem_jar/pooling-problem.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "pooling-problem.jar"]