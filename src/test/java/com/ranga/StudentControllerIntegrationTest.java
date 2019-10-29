package com.ranga;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ranga.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetAllEmployees() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetEmployeeById() {
		Student student = restTemplate.getForObject(getRootUrl() + "/employees/1", Student.class);
		System.out.println(student.getFirstName());
		assertNotNull(student);
	}

	@Test
	public void testCreateEmployee() {
		Student student = new Student();
		student.setEmailId("admin@gmail.com");
		student.setFirstName("admin");
		student.setLastName("admin");

		ResponseEntity<Student> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", student, Student.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdateEmployee() {
		int id = 1;
		Student student = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Student.class);
		student.setFirstName("admin1");
		student.setLastName("admin2");

		restTemplate.put(getRootUrl() + "/employees/" + id, student);

		Student updatedStudent = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Student.class);
		assertNotNull(updatedStudent);
	}

	@Test
	public void testDeleteEmployee() {
		int id = 2;
		Student student = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Student.class);
		assertNotNull(student);

		restTemplate.delete(getRootUrl() + "/employees/" + id);

		try {
			student = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Student.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
