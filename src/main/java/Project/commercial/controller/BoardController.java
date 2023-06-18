package Project.commercial.controller;
import Project.commercial.Dto.*;
import Project.commercial.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/create")
    @ResponseBody
    public BoardCreateResponseDto create(@RequestBody BoardCreateRequestDto boardCreateRequestDto,
                                         Authentication authentication){
        return boardService.create(boardCreateRequestDto, authentication);
    }

    @PatchMapping("/board/modified")
    @ResponseBody
    public BoardModifiedResponseDto modified(@RequestBody BoardModifiedRequestDto boardModifiedRequestDto,
                                             Authentication authentication)
    {
        return boardService.modified(boardModifiedRequestDto, authentication);
    }

    @DeleteMapping("/board/delete")
    @ResponseBody
    public String delete(@RequestBody BoardModifiedRequestDto boardModifiedRequestDto,
                         Authentication authentication){
        boardService.delete(boardModifiedRequestDto, authentication);
        return "Delete Done";
    }

    @GetMapping("/board/list")
    @ResponseBody
    public List<BoardDto> list(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return boardService.list(pageable);
    }


    @GetMapping("/board/list/member")
    @ResponseBody
    public List<BoardDto> listByMember(Authentication authentication,
                                    @PageableDefault(size=5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
       return boardService.listByMember(authentication, pageable);


    }

    @GetMapping("/board/search")
    @ResponseBody
    public List<BoardDto> search(@RequestParam String keyword,
                              @PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return boardService.search(keyword, pageable);
    }








}
