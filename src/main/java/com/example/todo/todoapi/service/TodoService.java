package com.example.todo.todoapi.service;

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.Todo;
import com.example.todo.todoapi.repository.TodoRepository;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    // 할 일 목록 조회
    // 요청에 따라 데이터 갱신, 삭제 등이 발생 한 후,
    // 최신의 데이터 내용을 클라이언트에게 전달해서 렌더링 하기 위해
    // 목록 리턴 메서드를 서비스에서 처리.
    public TodoListResponseDTO retrieve(String userId) {

        // 로그인 한 유저의 정보를 데이터베이스에서 조회
        User user = getUser(userId);

        List<Todo> entityList = todoRepository.findAllByUser(user);

        List<TodoDetailResponseDTO> dtoList = entityList.stream()
                /*.map(todo -> new TodoDetailResponseDTO(todo))*/
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }


    // 할 일 삭제
    public TodoListResponseDTO delete(final String todoId) {
        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error("id가 존재하지 않아 삭제 실패했습니다. " +
                    "-ID: {}, err: {}", todoId, e.getMessage());
            throw new RuntimeException("id가 존재하지 않아 삭제 실패했습니다.");
        }
        return retrieve("");
    }


    // 할 일 등록
    public TodoListResponseDTO create(
            final TodoCreateRequestDTO requestDTO,
            final String userId
    )
            throws RuntimeException {

        Todo todo = requestDTO.toEntity(getUser(userId));


        todoRepository.save(todo);
        log.info("할 일 저장 완료! 제목: {}", requestDTO.getTitle());
        return retrieve(userId);
    }

    // 할 일 수정
    public TodoListResponseDTO update(final TodoModifyRequestDTO requestDTO)
            throws RuntimeException {
        Optional<Todo> targetEntity = todoRepository.findById(requestDTO.getId());
        targetEntity.ifPresent(entity -> {
            entity.setDone(requestDTO.isDone()); //boolean 타입은 getDone이 아닌 isDone
            //화면단에서 done의 값을 미리 받아 오기 때문에 여기에서 뒤집을 필요가 없음

            todoRepository.save(entity);
        });
        return retrieve("");
    }
}



