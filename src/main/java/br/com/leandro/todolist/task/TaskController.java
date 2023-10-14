package br.com.leandro.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leandro.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/tasks/")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de inicio / termino deve ser maior que a data atual");
    }

    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A data de inicio deve ser maior que a data de término");
    }

    var taskCreated = this.taskRepository.save(taskModel);
    // ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    return ResponseEntity.status(HttpStatus.OK).body(taskCreated);

  }

  @GetMapping
  public List<TaskModel> list(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    var tasks = this.taskRepository.findByIdUser((UUID) idUser);
    return tasks;
  }

  // http://localhost:8080/tasks/15196168-erfeef
  // "/tasks/id"
  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

    // update sem todos os campos
    // se n encontrar nada retorna como null
    var task = this.taskRepository.findById(id).orElse(null);
    var idUser = request.getAttribute("idUser");

    // vericando se tarefa existe
    if (task == null) {
      System.out.println("estou no controller task put");
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("Tarefa não encontrada");
    }

    // verificando permissao usuario dono
    if (!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão");
    }

    Utils.copyNonNullProperties(taskModel, task);

    // taskModel.setIdUser((UUID) idUser);
    // taskModel.setId(id);
    var taskUpdated = this.taskRepository.save(task);
    return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
  }
}
