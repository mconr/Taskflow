package com.monir.taskflow.repository;


import com.monir.taskflow.model.Statut;
import com.monir.taskflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
List<Task> findByStatut(Statut statut);
List<Task> findByDateEcheanceBefore(LocalDate date);
}