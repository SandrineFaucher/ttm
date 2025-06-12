FROM mongo:6

COPY init-replica.js /docker-entrypoint-initdb.d/init-replica.js

RUN chmod +x /docker-entrypoint-initdb.d/init-replica.js