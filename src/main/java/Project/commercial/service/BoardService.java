package Project.commercial.service;


import Project.commercial.Dto.*;
import Project.commercial.domain.Board;
import Project.commercial.domain.BoardImage;
import Project.commercial.domain.Member;
import Project.commercial.repository.BoardImageRepository;
import Project.commercial.repository.BoardRepository;
import Project.commercial.repository.CategoryRepository;
import Project.commercial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardImageRepository boardImageRepository;
    @Value("${file.dir}")
    private String fileDir;

    public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto,
                                         List<MultipartFile> files ,
                                         Authentication authentication) throws IOException {

            LocalDateTime now = LocalDateTime.now();
            boardCreateRequestDto.setCreated_at(now);

            String email = authentication.getName();
            Member member = memberRepository.findByEmail(email).orElseThrow();
            boardCreateRequestDto.setMember(member);

            Board boardEntity = boardCreateRequestDto.toEntity(boardCreateRequestDto);
            Board saveBoard = boardRepository.save(boardEntity);

            extractFiles(files, saveBoard);

            Board board = boardRepository.findById(saveBoard.getId()).orElseThrow();
            List<BoardImage> boardImageList = boardImageRepository.findAllByBoard_id(board.getId());

        return BoardCreateResponseDto.builder()
                .id(saveBoard.getId())
                .title(saveBoard.getTitle())
                .content(saveBoard.getContent())
                .star_rate(saveBoard.getStar_rate())
                .created_at(saveBoard.getCreated_at())
                .username(saveBoard.getMember().getUsername())
                .boardImageList(boardImageList)
                .build();
        }

    public BoardModifiedResponseDto modified(BoardModifiedRequestDto boardModifiedRequestDto, Authentication authentication){
        Long board_id = boardModifiedRequestDto.getId();
        Board board = getBoardAuthority(board_id, authentication);

        boardModifiedRequestDto.setModified_at(LocalDateTime.now());

        board.updateBoard(boardModifiedRequestDto);

        return BoardModifiedResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .modified_at(board.getModified_at())
                .build();
    }


    public void delete(Map<String, Long> board_id_map, Authentication authentication){
        Long board_id = board_id_map.get("board_id");
        Board board = getBoardAuthority(board_id, authentication);

        boardRepository.delete(board);
    }

    public List<BoardDto> listByMember(Long member_id, Pageable pageable){
        Page<Board> boardList = boardRepository.findByMember_id(member_id, pageable);
        return getBoardList(boardList);
    }


    public List<BoardDto> listByMe(Authentication authentication,Pageable pageable){
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new RuntimeException("잘못된 접근입니다.")));

        Page<Board> findBoards = boardRepository.findByMember_id(member.getId(), pageable);
        return getBoardList(findBoards);
    }

    public List<BoardDto> search(String keyword, Pageable pageable){
        Page<Board> findBoards = boardRepository.findByTitleContaining(keyword, pageable);
        return getBoardList(findBoards);

    }

    public BoardDto detailPage(Map<String, Long> map_board_id){

        Long board_id = map_board_id.get("board_id");
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> (new RuntimeException("해당 게시글은 존재하지 않습니다.")));

        Member member = board.getMember();
        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .build();

        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .star_rate(board.getStar_rate())
                .created_at(board.getCreated_at())
                .modified_at(board.getModified_at())
                .member(memberDto)
                .boardImages(board.getBoardImageList())
                .build();
    }

    public List<BoardDto> list(Pageable pageable){
        Page<Board> findBoards = boardRepository.findAll(pageable);
        return getBoardList(findBoards);

    }

    private List<BoardDto> getBoardList(Page<Board> boards) {
        int number = boards.getNumberOfElements();
        List<BoardDto> BoardList = new ArrayList<>();

        for(int i = 0; i <= number -1; i++ ){

            Member member = boards.getContent().get(i).getMember();
            Board board = boards.getContent().get(i);

           MemberDto memberDto =
                     MemberDto.builder()
                             .id(member.getId())
                             .email(member.getEmail())
                             .username(member.getUsername())
                             .build();

            BoardDto boardDto =
                    BoardDto.builder()
                            .id(board.getId())
                            .content(board.getContent())
                            .star_rate(board.getStar_rate())
                            .created_at(board.getCreated_at())
                            .boardImages(board.getBoardImageList())
                            .modified_at(board.getModified_at())
                            .member(memberDto)
                                    .build();

            BoardList.add(boardDto);
        }

        return BoardList;
    }

    private Board getBoardAuthority(Long board_id, Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new RuntimeException("잘못된 접근 입니다.")));
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> (new RuntimeException("해당 게시글은 존재하지 않습니다.")));

        if(!board.getMember().equals(member))
        {
            throw new RuntimeException("게시글에 대한 권한이 없습니다.");
        }

        return board;
    }

    private void extractFiles(List<MultipartFile> files, Board saveBoard) throws IOException {
        for(MultipartFile file : files){
            if(!(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))){
                throw new RuntimeException("해당 첨부파일 형식이 올바르지 않습니다.");
            }

            String originalFilename = file.getOriginalFilename();
            String saveFileName = createSaveFileName(originalFilename);
            file.transferTo(new File(getFullPath(saveFileName)));

            BoardImageRequestDto  boardImageRequestDto = BoardImageRequestDto.builder()
                    .uploadImageName(originalFilename)
                    .storeImageName(saveFileName)
                    .board(saveBoard)
                    .build();

            BoardImage boardImageEntity = boardImageRequestDto.toEntity(boardImageRequestDto);
            boardImageRepository.save(boardImageEntity);
        }
    }

    private String createSaveFileName(String originalFileName){
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName){
        int index = originalFileName.lastIndexOf(".");
        return originalFileName.substring(index + 1);

    }

    private String getFullPath(String fileName){
        return fileDir + fileName;

    }
}
