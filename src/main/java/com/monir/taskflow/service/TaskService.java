package com.monir.taskflow.service;


import com.monir.taskflow.exception.ResourceNotFoundException;
import com.monir.taskflow.model.Statut;
import com.monir.taskflow.model.Task;
import com.monir.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;


@Service
public class TaskService {
private final TaskRepository repo;


public TaskService(TaskRepository repo) { this.repo = repo; }


public List<Task> getAllTasks() { return repo.findAll(); }


public Task createTask(Task task) { return repo.save(task); }


public Task getTaskById(Long id) {
return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
}


public Task updateTask(Long id, Task updated) {
Task t = getTaskById(id);
t.setTitre(updated.getTitre());
t.setDescription(updated.getDescription());
t.setDateEcheance(updated.getDateEcheance());
t.setStatut(updated.getStatut());
return repo.save(t);
}


public void deleteTask(Long id) { repo.deleteById(id); }


public List<Task> findByStatut(Statut statut) { return repo.findByStatut(statut); }


public List<Task> findDueBefore(LocalDate date) { return repo.findByDateEcheanceBefore(date); }
}