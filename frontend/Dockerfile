FROM node:lts-alpine

ENV NODE_ENV development

WORKDIR /react-app

COPY ./tv-show-reminder-app/package*.json ./

RUN npm install

COPY ./tv-show-reminder-app ./

EXPOSE 3000

CMD ["npm", "start"]
