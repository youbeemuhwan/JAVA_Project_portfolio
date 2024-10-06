package Project.commercial.dto.member;

import Project.commercial.domain.Member;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberDto {

    private String email;

    private String username;

    private String password;

    private Integer point;


    @Builder
    public CreateMemberDto(String email, String username, String password, Integer point) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.point = point;
    }

    public Member toEntity(CreateMemberDto createMemberDto){
        return Member.builder()
                .email(createMemberDto.getEmail())
                .password(createMemberDto.getPassword())
                .username(createMemberDto.getUsername())
                .point(createMemberDto.getPoint())
                .build();

    }

}




