package Project.commercial.Dto;

import Project.commercial.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequestDto {

    private String email;

    private String password;
}




