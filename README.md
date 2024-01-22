# url-shortener-service
To create the short urls for long urls

**Components Used:**
* JDK 17
* SpringBoot 3.1.8
* MongoDB for data storage
* Actuator for healthcheck - http://localhost:8080/actuator/health
* Swagger - http://localhost:8080/swagger-ui/index.html
* Test containers for integration test

Two APIs
POST - To create a short url for the long url provided in the request
http://localhost:8080/shortenUrl/create

Example for body:
{
"longUrl": "https://www.bbc.co.uk/weather/sl1"
}

GET - Open a browser with shortlink provided. It will redirect to the original link 

Run following commands to set Mongodb Database from Mongo shell

_use url_shortener_

_db.createUser( { user: "url_shortener", pwd: "secret", roles: [ "readWrite", "dbAdmin" ]} )_

_db.createCollection("counters")_

_db.createCollection("links")_

*db.counters.insert({_id: "SHORT_URL_UNIQUE_ID", seq: 1000000000});*

