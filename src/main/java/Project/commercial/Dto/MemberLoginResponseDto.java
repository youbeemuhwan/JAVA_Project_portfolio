package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberLoginResponseDto {


    private String email;

    private String username;

    private String token;

    public MemberLoginResponseDto(String email, String username, String token) {
        this.email = email;
        this.username = username;
        this.token = token;
    }
}
