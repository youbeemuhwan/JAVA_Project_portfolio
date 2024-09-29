package Project.commercial.service;


import Project.commercial.domain.Board;
import Project.commercial.domain.BoardImage;
import Project.commercial.domain.Member;
import Project.commercial.dto.board.*;
import Project.commercial.dto.member.MemberDto;
import Project.commercial.repository.BoardImageRepository;
import Project.commercial.repository.BoardRepository;
import Project.commercial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

@Slf4j

public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardImageRepository boardImageRepository;
    @Value("${file.dir}")
    private String fileDir;
    @Transactional
    public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto,
                                         List<MultipartFile> files ,
                                         Authentication authentication) throws IOException {

            LocalDateTime now = LocalDateTime.now();
            boardCreateRequestDto.setCreatedAt(now);

            String email = authentication.getName();
            Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("해당 유저는 존재하지 않습니다."));
            boardCreateRequestDto.setMember(member);

            Board boardEntity = boardCreateRequestDto.toEntity(boardCreateRequestDto);
            Board saveBoard = boardRepository.save(boardEntity);

            if(files != null){
                extractFiles(files, saveBoard);
            }

            Board board = boardRepository.findById(saveBoard.getId()).orElseThrow(() -> new EntityNotFoundException("해당 게시판을 찾을 수 없습니다."));
            List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(board.getId());

        return BoardCreateResponseDto.builder()
                .id(saveBoard.getId())
                .title(saveBoard.getTitle())
                .content(saveBoard.getContent())
                .starRate(saveBoard.getStarRate())
                .createdAt(saveBoard.getCreatedAt())
                .username(saveBoard.getMember().getUsername())
                .boardImageList(boardImageList)
                .build();
        }
    @Transactional
    public BoardModifiedResponseDto modified(BoardModifiedRequestDto boardModifiedRequestDto, Authentication authentication){
        Long board_id = boardModifiedRequestDto.getId();
        Board board = getBoardAuthority(board_id, authentication);

        boardModifiedRequestDto.setModified_at(LocalDateTime.now());

        board.updateBoard(boardModifiedRequestDto);

        return BoardModifiedResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

    @Transactional
    public void delete(Map<String, Long> board_id_map, Authentication authentication){
        Long board_id = board_id_map.get("board_id");
        Board board = getBoardAuthority(board_id, authentication);

        boardRepository.delete(board);
    }
    @Transactional(readOnly = true)
    public List<BoardDto> listByMember(Long member_id, Pageable pageable){
        Page<Board> boardList = boardRepository.findByMemberId(member_id, pageable);
        return getBoardList(boardList);
    }

    @Transactional(readOnly = true)
    public List<BoardDto> listByMe(Authentication authentication,Pageable pageable){
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new EntityNotFoundException("잘못된 접근입니다.")));

        Page<Board> findBoards = boardRepository.findByMemberId(member.getId(), pageable);
        return getBoardList(findBoards);
    }
    @Transactional(readOnly = true)
    public List<BoardDto> search(String keyword, Pageable pageable){
        Page<Board> findBoards = boardRepository.findByTitleContaining(keyword, pageable);
        return getBoardList(findBoards);

    }
    @Transactional(readOnly = true)
    public BoardDto detailPage(Map<String, Long> map_board_id){

        Long board_id = map_board_id.get("board_id");
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> (new EntityNotFoundException("해당 게시글은 존재하지 않습니다.")));

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
                .starRate(board.getStarRate())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .member(memberDto)
                .boardImages(board.getBoardImageList())
                .build();
    }

    public List<BoardDto> list(Pageable pageable){
        Page<Board> findBoards = boardRepository.findAll(pageable);
        return getBoardList(findBoards);

    }

    private List<BoardDto> getBoardList(Page<Board> boards) {
        return boards.getContent().stream().map(board -> {
            Member member = board.getMember();

            MemberDto memberDto = MemberDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .build();

            return BoardDto.builder()
                    .id(board.getId())
                    .content(board.getContent())
                    .starRate(board.getStarRate())
                    .createdAt(board.getCreatedAt())
                    .boardImages(board.getBoardImageList())
                    .modifiedAt(board.getModifiedAt())
                    .member(memberDto)
                    .build();
        }).collect(Collectors.toList());
    }



    private Board getBoardAuthority(Long board_id, Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new EntityNotFoundException("잘못된 접근 입니다.")));
        Board board = boardRepository.findById(board_id).orElseThrow(
                () -> (new EntityNotFoundException("해당 게시글은 존재하지 않습니다.")));

        if(!board.getMember().equals(member))
        {
            throw new AccessDeniedException("게시글에 대한 권한이 없습니다.");
        }

        return board;
    }

    private void extractFiles(List<MultipartFile> files, Board saveBoard) throws IOException {
        for(MultipartFile file : files){
            if(!(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))){
                throw new MultipartException("해당 첨부파일 형식이 [ jpeg, png ] 가 아닙니다.");
            }

            String originalFilename = file.getOriginalFilename();
            String saveFileName = createSaveFileName(originalFilename);
            file.transferTo(new File(getFullPath(saveFileName)));

            BoardImageRequestDto boardImageRequestDto = BoardImageRequestDto.builder()
                    .uploadImageName(originalFilename)
                    .storeImageName(saveFileName)
                    .board(saveBoard)
                    .build();

            BoardImage boardImageEntity = boardImageRequestDto.toEntity(boardImageRequestDto);
            boardImageRepository.save(boardImageEntity);
        }
    }

    private String createSaveFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int index = originalFileName.lastIndexOf(".");
        if (index == -1) {
            throw new IllegalArgumentException("파일 이름에 확장자가 없습니다: " + originalFileName);
        }
        return originalFileName.substring(index + 1);
    }


    private String getFullPath(String fileName) throws IOException {
        String fullPath = fileDir + fileName;

        File directory = new File(fileDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("저장 경로 디렉토리를 생성할 수 없습니다: " + fileDir);
            }
        }
        return fullPath;
    }
}
