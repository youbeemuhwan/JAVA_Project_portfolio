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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board/create")
    @ResponseBody
    public BoardCreateResponseDto create(@RequestPart(value = "BoardCreateRequestDto") BoardCreateRequestDto boardCreateRequestDto,
                                         @RequestPart(value = "files", required = false) List<MultipartFile> files
                                         ,Authentication authentication
                                         ) throws IOException {
        return boardService.create(boardCreateRequestDto, files ,authentication);
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
                         Authentication authentication)
    {
        boardService.delete(boardModifiedRequestDto, authentication);
        return "Delete Done";
    }

    @GetMapping("/board/list")
    @ResponseBody
    public List<BoardDto> list(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC)
                                   Pageable pageable)
    {
        return boardService.list(pageable);
    }


    @GetMapping("/board/list/me")
    @ResponseBody
    public List<BoardDto> listByMe(Authentication authentication,
                                    @PageableDefault(size=5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
       return boardService.listByMe(authentication, pageable);


    }

    @GetMapping("/board/list/member")
    @ResponseBody
    public List<BoardDto> listByMember(Long member_id,
                                       @PageableDefault(size=5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return boardService.listByMember(member_id, pageable);

    }

    @GetMapping("/board/list/search")
    @ResponseBody
    public List<BoardDto> search(@RequestParam String keyword,
                              @PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return boardService.search(keyword, pageable);
    }

    @GetMapping("/board/detail")
    @ResponseBody
    public BoardDto detailPage(@RequestBody Long board_id){
        return boardService.detailPage(board_id);

    }








}
