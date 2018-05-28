FROM postgres:9.6-alpine
ENV POSTGRES_USER ycPgUser
ENV POSTGRES_PASSWORD ycPgSecret
ENV POSTGRES_DB connPGdb
ADD create_schema.sql /docker-entrypoint-initdb.d/
