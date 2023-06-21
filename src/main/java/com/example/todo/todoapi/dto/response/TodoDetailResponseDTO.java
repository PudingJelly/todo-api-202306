package com.example.todo.todoapi.dto.response;

import com.example.todo.todoapi.entity.Todo;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDetailResponseDTO { // 클라이언트단에게 전달하기 위한 용도로 사용 될 DTO

    private String id;
    private String title;
    private boolean done;

    // 엔터티를 DTO로 만들어주는 생성자
    public TodoDetailResponseDTO(Todo todo) {
        this.id = todo.getTodoId();
        this.title = todo.getTitle();
        this.done = todo.isDone(); // boolean 타입의 데이터는 'is'로 'get'한다
    }
}





















