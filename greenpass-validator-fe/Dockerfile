FROM node:12.22-alpine AS build
WORKDIR /tmp
RUN npm install @angular/cli -g
COPY . /tmp
RUN npm i
RUN npm run build

FROM nginx:alpine
COPY --from=build /tmp/dist/greenpass-validator-fe/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
