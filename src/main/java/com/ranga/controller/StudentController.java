package com.ranga.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ranga.exception.ResourceNotFoundException;
import com.ranga.model.Student;
import com.ranga.repository.StudentRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("/students")
	public List<Student> getAllEmployees() {
		return studentRepository.findAll();
	}

	@GetMapping("/students/{id}")
	public ResponseEntity<Student> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Student student = studentRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + employeeId));
		return ResponseEntity.ok().body(student);
	}

	@PostMapping("/students")
	public Student createEmployee(@Valid @RequestBody Student student) {
		return studentRepository.save(student);
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<Student> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                  @Valid @RequestBody Student studentDetails) throws ResourceNotFoundException {
		Student student = studentRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		student.setEmailId(studentDetails.getEmailId());
		student.setLastName(studentDetails.getLastName());
		student.setFirstName(studentDetails.getFirstName());
		final Student updatedStudent = studentRepository.save(student);
		return ResponseEntity.ok(updatedStudent);
	}

	@DeleteMapping("/students/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Student student = studentRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found for this id :: " + employeeId));

		studentRepository.delete(student);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
