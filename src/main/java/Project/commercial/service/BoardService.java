package Project.commercial.service;

import Project.commercial.Dto.BoardCreateRequestDto;
import Project.commercial.Dto.BoardCreateResponseDto;
import Project.commercial.domain.Board;
import Project.commercial.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto, Authentication authentication){
        LocalDateTime now = LocalDateTime.now();
        boardCreateRequestDto.setCreated_at(now);
        authentication.get


        Board entity = boardCreateRequestDto.toEntity(boardCreateRequestDto);
        boardRepository.save(entity);
        return BoardCreateResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .created_at(entity.getCreated_at())
                .username(entity.getMember().getUsername())
                .build();
    }



}
