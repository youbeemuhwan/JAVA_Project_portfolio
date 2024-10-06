package Project.commercial.controller;

import Project.commercial.dto.board.*;
import Project.commercial.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping()
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    @ResponseBody
    public ResponseBoardDto createBoard(@RequestPart(value = "BoardCreateRequestDto") CreateBoardDto createBoardDto,
                                        @Nullable @RequestPart(value = "files", required = false) List<MultipartFile> files
                                         , Authentication authentication
                                         ) throws IOException
    {
        return boardService.createBoard(createBoardDto, files ,authentication);
    }

    @PatchMapping("/board/{id}")
    @ResponseBody
    public void updateBoard(@PathVariable() Long id, @RequestBody UpdateBoardDto updateBoardDto,
                                             Authentication authentication)
    {
        boardService.updateBoard(id, updateBoardDto, authentication);
    }

    @DeleteMapping("/board/{id}")
    @ResponseBody
    public String deleteBoard(@PathVariable Long id,
                         Authentication authentication)
    {
        boardService.deleteBoard(id, authentication);
        return "Delete Done";
    }

    @GetMapping("/board/list")
    @ResponseBody
    public List<BoardDto> getBoards(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC)
                                   Pageable pageable)
    {
        return boardService.getBoards(pageable);
    }


    @GetMapping("/board/list/me")
    @ResponseBody
    public List<BoardDto> getMyBoards(Authentication authentication,
                                    @PageableDefault(size=5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
       return boardService.getMyBoards(authentication, pageable);
    }

    @GetMapping("/member/{memberId}/board")
    @ResponseBody
    public List<BoardDto> getBoardsByMember(@PathVariable Long memberId,
                                       @PageableDefault(size=5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return boardService.getBoardsByMember(memberId, pageable);
    }

    @GetMapping("/board/search")
    @ResponseBody
    public List<BoardDto> searchBoards(@RequestParam String keyword,
                              @PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return boardService.searchBoards(keyword, pageable);
    }

    @GetMapping("/board/{id}")
    @ResponseBody
    public BoardDto getBoardDetail(@PathVariable Long id)
    {
        return boardService.getBoardDetail(id);
    }
}
