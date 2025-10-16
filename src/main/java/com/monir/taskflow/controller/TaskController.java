package com.monir.taskflow.controller;


import com.monir.taskflow.model.Statut;
import com.monir.taskflow.model.Task;
import com.monir.taskflow.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
private final TaskService service;
private final Logger logger = LoggerFactory.getLogger(TaskController.class);


public TaskController(TaskService service) { this.service = service; }


@GetMapping
public List<Task> getAll(@RequestParam(required = false) Statut statut,
@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate before) {
if (statut != null) return service.findByStatut(statut);
if (before != null) return service.findDueBefore(before);
return service.getAllTasks();
}


@GetMapping("/{id}")
public Task getById(@PathVariable Long id) { return service.getTaskById(id); }


@PostMapping
public ResponseEntity<?> create(@Valid @RequestBody Task task) {
	try {
		Task created = service.createTask(task);
		return ResponseEntity.ok(created);
	} catch (Exception ex) {
		logger.error("Error creating task", ex);
		// DEBUG: return the exception message and stack info in the response body to help debugging locally
		return ResponseEntity.status(500).body(Map.of("error", "Internal server error", "message", ex.getMessage()));
	}
}


@PutMapping("/{id}")
public Task update(@PathVariable Long id, @Valid @RequestBody Task task) { return service.updateTask(id, task); }


@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
service.deleteTask(id);
return ResponseEntity.noContent().build();
}


}