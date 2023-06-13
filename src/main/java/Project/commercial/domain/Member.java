package Project.commercial.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    @Email(message = "올바른 E-mail 형식이 아닙니다.")
    private String email;


    @NotNull
    private String username;

    @NotNull
    private String password;

    private Integer point;

    @Builder
    public Member(String email, String username, String password, Integer point) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.point = point;
    }
}


