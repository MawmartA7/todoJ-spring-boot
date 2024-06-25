package br.com.todo.todo.models;

import br.com.todo.todo.dto.TaskDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Boolean done;

    public Task(TaskDTO taskDTO) {
        this.name = taskDTO.name();
        this.description = taskDTO.description();
        this.priority = taskDTO.priority();
        this.done = taskDTO.done();
    }

    public Task(TaskDTO taskDTO, Long id) {
        this.id = id;
        this.name = taskDTO.name();
        this.description = taskDTO.description();
        this.priority = taskDTO.priority();
        this.done = taskDTO.done();
    }
}
