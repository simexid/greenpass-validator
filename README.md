# greenpass-validator

GreenPass validator project - both FrontEnd and BackEnd. Ready to use application on Docker\
Running on [Simexid greenpass validator](https://greenpassvalidator.simexid.org)

## greenpass-validator-fe

Angular project

Use `npm run start:net` or `ng serve --host 0.0.0.0 --ssl --proxy-config proxy.conf.json` in order to start the application locally with proxy configuration. Default ser on proxy.conf.json redirect all traffico to /api to port :8080. After start reach the application on [https://127.0.0.1:4200](https://127.0.0.1:4200).

## greenpass-validator-be

Maven/SpringBoot project

Use `mvn spring-boot:run` to start the back-end application. After start the application is reachable at the endpoint [http://127.0.0.1:8080](http://127.0.0.1:8080). At this time, the only method exposed is:
|Method|Endpoint|Body input|
|----|-----|-------|
|POST|/api/checkCertificate|{"code": "HC1:...code..."}|

## Docker

The project is ready to be deployed on docker container. You need docker and docker-compose installed on your system, then you can start the compose of docker container by run the script on _script_ folder `sh script/runComposer.sh`

Please note that the default port for FE and BE application are different when running on docker container, the FE is reachable on the default port 80, the BE on port 8087. Nginx server is configured to proxy the /api request to port 8087 locally, so you can reach the application on https://_your-hostname-or-ip_.

**HTTPS**\
The application must run in SSL mode becouse some browser block camera access on unsecure connection

## License
Released under GNU GPL v3, check LICENSE file


**Author:** [Simexid](https://www.simexid.org)

