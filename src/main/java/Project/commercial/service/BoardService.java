package Project.commercial.service;


import Project.commercial.Dto.*;
import Project.commercial.domain.Board;
import Project.commercial.domain.Member;
import Project.commercial.repository.BoardRepository;
import Project.commercial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    public BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto, Authentication authentication){
        LocalDateTime now = LocalDateTime.now();
        boardCreateRequestDto.setCreated_at(now);

        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow();

        boardCreateRequestDto.setMember(member);

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


    public BoardModifiedResponseDto modified(BoardModifiedRequestDto boardModifiedRequestDto, Authentication authentication){
        Board board = getBoardAuthority(boardModifiedRequestDto, authentication);

        boardModifiedRequestDto.setModified_at(LocalDateTime.now());

        board.updateBoard(boardModifiedRequestDto);

        return BoardModifiedResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .modified_at(board.getModified_at())
                .build();
    }



    public void delete(BoardModifiedRequestDto boardModifiedRequestDto, Authentication authentication){
        Board board = getBoardAuthority(boardModifiedRequestDto, authentication);

        boardRepository.delete(board);
    }





    public List<BoardDto> listByMember(Authentication authentication,Pageable pageable){
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new RuntimeException("잘못된 접근입니다.")));

        Page<Board> findBoards = boardRepository.findByMember_id(member.getId(), pageable);
        return getBoardList(findBoards);







    }

    public List<BoardDto> search(String keyword, Pageable pageable){
        Page<Board> findBoards = boardRepository.findByTitleContaining(keyword, pageable);
        return getBoardList(findBoards);

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
                    .created_at(board.getCreated_at())
                    .modified_at(board.getModified_at())
                    .member(memberDto)
                    .build();

            BoardList.add(boardDto);
        }

        return BoardList;
    }

    private Board getBoardAuthority(BoardModifiedRequestDto boardModifiedRequestDto, Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new RuntimeException("잘못된 접근 입니다.")));
        Board board = boardRepository.findById(boardModifiedRequestDto.getId()).orElseThrow(
                () -> (new RuntimeException("해당 게시글은 존재하지 않습니다.")));

        if(!board.getMember().equals(member))
        {
            throw new RuntimeException("게시글에 대한 권한이 없습니다.");

        }
        return board;
    }


}
