FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration production

FROM nginx:alpine
COPY --from=builder /app/dist/game-store-ui /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf