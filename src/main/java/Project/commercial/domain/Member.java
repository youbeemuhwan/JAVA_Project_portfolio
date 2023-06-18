package Project.commercial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "member")
    @BatchSize(size = 100)
    private List<Board> board = new ArrayList<Board>();


    @Builder
    public Member(String email, String username, String password, Integer point) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.point = point;
    }
}


