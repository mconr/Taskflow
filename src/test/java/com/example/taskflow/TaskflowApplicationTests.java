package com.monir.taskflow;


import com.monir.taskflow.controller.TaskController;
import com.monir.taskflow.model.Task;
import com.monir.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.mockito.Mockito.mock;


@SpringBootTest
class TaskControllerTest {
@Test
void contextLoads() {
TaskService mocked = mock(TaskService.class);
TaskController controller = new TaskController(mocked);
assert controller != null;
}
}