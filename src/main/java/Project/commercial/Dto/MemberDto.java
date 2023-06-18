package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDto {

    private Long id;

    private String email;

    private String username;

    public MemberDto(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}