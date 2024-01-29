package com.example.urlshortenerservice;

import com.example.urlshortenerservice.config.ZonedDateTimeReadConverter;
import com.example.urlshortenerservice.config.ZonedDateTimeWriteConverter;
import com.example.urlshortenerservice.model.DatabaseSequence;
import com.example.urlshortenerservice.model.Link;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = UrlShortenerServiceApplication.class)
@AutoConfigureMockMvc
@EnableMongoRepositories
@TestPropertySource(locations = "classpath:application-test.properties")
class UrlShortenerServiceApplicationTests {

	@Container
	public static MongoDBContainer mongoDBContainer = new MongoDBContainer(
			DockerImageName.parse("mongo:latest"));

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MockMvc mvc;

	@DynamicPropertySource
	static void mongoDbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void givenLongURL_thenGetShortURLInResponse() throws Exception {
		MappingMongoConverter conv = (MappingMongoConverter) mongoTemplate.getConverter();
		// tell mongodb to use the custom converters
		conv.setCustomConversions(customConversions());
		conv.afterPropertiesSet();
		mongoTemplate.createCollection("links");
		mongoTemplate.createCollection("counters");

		mongoTemplate.remove(new Query(), Link.class);
		mongoTemplate.remove(new Query(), DatabaseSequence.class);

		String shortenURLRequest = """
    				{"longUrl" : "https://www.reallylongurl.com/blah1/blah2/blah3/dfsdfdsfdsfsdfdsfdsfds"}
				""";

		DatabaseSequence seq = DatabaseSequence.builder().id(Link.SEQUENCE_NAME).seq(1000000003L).build();
		mongoTemplate.save(seq);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/shortenUrl/create")
						.contentType(MediaType.APPLICATION_JSON).content(shortenURLRequest))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		String shortenURLActualResponse = mvcResult.getResponse().getContentAsString();
		String shortenURLExpectedResponse = """
    				{
    				"shortUrl":"http://localhost:8080/015ftgK",
    				"longUrl":"https://www.reallylongurl.com/blah1/blah2/blah3/dfsdfdsfdsfsdfdsfdsfds"
    				}
				    """;

		ObjectMapper objectMapper = new ObjectMapper();
		ShortenURLTestResponse actualResponse = objectMapper.readValue(shortenURLActualResponse, ShortenURLTestResponse.class);
		ShortenURLTestResponse expectedResponse = objectMapper.readValue(shortenURLExpectedResponse, ShortenURLTestResponse.class);

		Assert.assertEquals(expectedResponse.getShortUrl(), actualResponse.getShortUrl());

		DatabaseSequence databaseSequence = mongoTemplate.find(
						Query.query(Criteria.where("_id").is(Link.SEQUENCE_NAME)), DatabaseSequence.class)
				.get(0);
		Assert.assertTrue(databaseSequence.getSeq() == 1000000004L);
	}

	private MongoCustomConversions customConversions() {
		List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
		converters.add(new ZonedDateTimeReadConverter());
		converters.add(new ZonedDateTimeWriteConverter());
		return new MongoCustomConversions(converters);
	}
}
