package Project.commercial.dto.member;

import Project.commercial.domain.Member;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpDto {

    private String email;

    private String username;

    private String password;

    private Integer point;


    @Builder
    public MemberSignUpDto(String email, String username, String password, Integer point) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.point = point;
    }

    public Member toEntity(MemberSignUpDto memberSignUpDto){
        return Member.builder()
                .email(memberSignUpDto.getEmail())
                .password(memberSignUpDto.getPassword())
                .username(memberSignUpDto.getUsername())
                .point(memberSignUpDto.getPoint())
                .build();

    }

}




