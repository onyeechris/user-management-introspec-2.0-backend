FROM ppc64le/node:8 as builder
WORKDIR /app

COPY ./package.json ./package-lock.json ./
RUN npm install
COPY . .
#replace the types, which carry base url, with this simple path based base url because static ip is the enemy of scalability
COPY ./deployment/types.js ./src/actions
RUN npm run build

FROM nginx
EXPOSE 8181
COPY ./deployment/nginx-conf/default.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/build /usr/share/nginx/html
