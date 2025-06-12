FROM mongo:6

COPY init-replica.sh /docker-entrypoint-initdb.d/init-replica.sh

RUN chmod +x /docker-entrypoint-initdb.d/init-replica.sh