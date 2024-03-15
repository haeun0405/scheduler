package org.sparta.scheduler.Service;

import java.util.List;
import org.sparta.scheduler.Domain.Task;
import org.sparta.scheduler.Dto.TaskDTO;
import org.sparta.scheduler.Exception.TaskNotFoundException;
import org.sparta.scheduler.Exception.UnauthorizedTaskAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface TaskService {
    /**
     * 주어진 사용자 이름에 속한 모든 할일 카드를 조회합니다.
     *
     * @param username 사용자 이름
     * @return 사용자에 속한 할일 카드 목록
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
     */
    List<Task> getTasksByUser(String username);

    /**
     * 주어진 ID에 해당하는 할일 카드를 조회합니다.
     *
     * @param id 할일 카드 ID
     * @return 조회된 할일 카드
     * @throws TaskNotFoundException 할일 카드를 찾을 수 없을 때 발생
     */
    Task getTaskById(Long id);

    /**
     * 새로운 할일 카드를 생성합니다.
     *
     * @param taskDTO 할일 카드 생성 정보를 담은 DTO
     * @return 생성된 할일 카드
     * @throws UsernameNotFoundException 할일 카드를 생성하는 사용자를 찾을 수 없을 때 발생
     */
    Task createTask(TaskDTO taskDTO);

    /**
     * 주어진 ID에 해당하는 할일 카드를 업데이트합니다.
     *
     * @param taskId 할일 카드 ID
     * @param taskDTO 할일 카드 업데이트 정보를 담은 DTO
     * @return 업데이트된 할일 카드
     * @throws TaskNotFoundException 할일 카드를 찾을 수 없을 때 발생
     * @throws UnauthorizedTaskAccessException 할일 카드를 업데이트할 권한이 없을 때 발생
     */
    Task updateTask(Long taskId, TaskDTO taskDTO);

}
