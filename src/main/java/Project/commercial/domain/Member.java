package Project.commercial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @JsonIgnore
    private String password;

    private Integer point;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @BatchSize(size = 5)
    private List<Board> board;

    @Builder
    public Member(String email, String username, String password, Integer point) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.point = point;
    }

    public void updateMemberPoint(Integer point){
        this.point = point;
    }
}


