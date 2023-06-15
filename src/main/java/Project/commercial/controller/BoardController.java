package Project.commercial.controller;

import Project.commercial.Dto.BoardCreateRequestDto;
import Project.commercial.Dto.BoardCreateResponseDto;
import Project.commercial.Dto.BoardModifiedRequestDto;
import Project.commercial.Dto.BoardModifiedResponseDto;
import Project.commercial.domain.Board;
import Project.commercial.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/create")
    @ResponseBody
    public BoardCreateResponseDto create(@RequestBody BoardCreateRequestDto boardCreateRequestDto, Authentication authentication){
        return boardService.create(boardCreateRequestDto, authentication);
    }

    @PatchMapping("/board/modified")
    @ResponseBody
    public BoardModifiedResponseDto modified(@RequestBody BoardModifiedRequestDto boardModifiedRequestDto, Authentication authentication)
    {
        return boardService.modified(boardModifiedRequestDto, authentication);
    }

    @DeleteMapping("/board/delete")
    @ResponseBody
    public String delete(@RequestBody Long board_id){
        boardService.delete(board_id);
        return "Delete Done";
    }

    @GetMapping("/board/list")
    @ResponseBody
    public Page<Board> list(@PageableDefault(page = 1, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return boardService.list(pageable);
    }



}
