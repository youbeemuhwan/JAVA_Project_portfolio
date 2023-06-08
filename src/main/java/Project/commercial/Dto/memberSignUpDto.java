package Project.commercial.Dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class memberSignUpDto {


    private Long id;


    private String email;


    private String username;


    private String password;

}




