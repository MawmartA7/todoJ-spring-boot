package br.com.todo.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.todo.todo.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByDoneTrue();

    public List<Task> findByDoneFalse();
}
