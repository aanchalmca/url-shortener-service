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

