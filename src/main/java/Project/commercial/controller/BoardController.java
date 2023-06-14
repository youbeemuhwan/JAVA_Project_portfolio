package Project.commercial.controller;

import Project.commercial.Dto.BoardCreateRequestDto;
import Project.commercial.Dto.BoardCreateResponseDto;
import Project.commercial.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/create")
    public BoardCreateResponseDto create(@RequestBody BoardCreateRequestDto boardCreateRequestDto, Authentication authentication){
        return boardService.create(boardCreateRequestDto, authentication);


    }

}
