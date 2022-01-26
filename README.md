# SpaceFlightNewsApiIntegration app

This is a simple demo app which acts as a proxy between client and SpaceFlightNews
API (https://api.spaceflightnewsapi.net/v3/documentation).

It provides one GET mapping:

1) http://localhost:8080/articles?newsSite=&title=

You can pass two request params: newsSite and title which will be used to filtering results. After getting error
response from SpaceFlightNewsApi, spring-retry will be trying twice to get correct response.

## Usage

To run this project you need to have maven 3, java 17.

## Tech stack:

Spring boot 2.6.2\
Spring WebFlux Client Spring-retry\
Spring Retry\
Lombok\
okhttp3.mockwebserver

## License

[MIT](https://choosealicense.com/licenses/mit/)