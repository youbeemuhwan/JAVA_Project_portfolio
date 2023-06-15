package Project.commercial.service;

import Project.commercial.Dto.BoardCreateRequestDto;
import Project.commercial.Dto.BoardCreateResponseDto;
import Project.commercial.Dto.BoardModifiedRequestDto;
import Project.commercial.Dto.BoardModifiedResponseDto;
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
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> (new RuntimeException("잘못된 접근 입니다.")));
        Board board = boardRepository.findById(boardModifiedRequestDto.getId()).orElseThrow(
                () -> (new RuntimeException("해당 게시글은 존재하지 않습니다.")));

        if(!board.getMember().equals(member))
        {
            throw new RuntimeException("게시글 수정 권한이 없습니다.");

        }

        boardModifiedRequestDto.setModified_at(LocalDateTime.now());

        board.updateBoard(boardModifiedRequestDto);

        return BoardModifiedResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .modified_at(board.getModified_at())
                .build();
    }

    public void delete(Long id){
        Board board = boardRepository.findById(id).orElseThrow(
                ()-> (new RuntimeException("해당 게시글은 존재하지 않습니다.")));

        boardRepository.delete(board);
    }

    public Page<Board> list(Pageable pageable){
       return boardRepository.findAll(pageable);

    }



}
