FROM node:18 AS build

WORKDIR /app

# Copier seulement le dossier frontend où est package.json
COPY src/main/webapp/package*.json ./
COPY src/main/webapp/ ./

RUN npm install
RUN npm run build

# Étape finale Nginx
FROM nginx:1.27

COPY nginx.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
