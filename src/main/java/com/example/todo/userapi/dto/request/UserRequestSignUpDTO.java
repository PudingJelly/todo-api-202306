package com.example.todo.userapi.dto.request;

import com.example.todo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestSignUpDTO {

    @NotBlank
    @Email // 이메일 형식인지 검증해주는 어노테이션
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 2, max = 5)
    private String userName;

    // 엔터티로 변환
    public User toEntity(String uploadedFilePath) {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .userName(this.userName)
                .profileImg(uploadedFilePath)
                .build();
    }

}
