package Project.commercial.service;

import Project.commercial.dto.member.MemberLoginRequestDto;
import Project.commercial.dto.member.MemberLoginResponseDto;
import Project.commercial.dto.member.CreateMemberDto;
import Project.commercial.domain.Member;
import Project.commercial.jwt.JwtProvider;
import Project.commercial.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void signUp(CreateMemberDto createMemberDto){

        if (memberRepository.existsByEmail(createMemberDto.getEmail()))
        {
            throw new RuntimeException("해당 이메일은 존재하는 이메일 입니다.");
        }

        String encode = passwordEncoder.encode(createMemberDto.getPassword());
        createMemberDto.setPassword(encode);

        createMemberDto.setPoint(10000);

        Member member = createMemberDto.toEntity(createMemberDto);

        memberRepository.save(member);


    }

    public MemberLoginResponseDto login(MemberLoginRequestDto memberLoginRequestDto){
        Member loginMember = memberRepository.findByEmail(memberLoginRequestDto.getEmail())
                .orElseThrow(() ->(new RuntimeException("존재하지 않는 계정 이거나 올바르지 못환 패스워드 입니다.")));

       if(!passwordEncoder.matches(memberLoginRequestDto.getPassword(), loginMember.getPassword()))
       {
           throw new RuntimeException("존재하지 않는 계정 이거나 올바르지 않은 패스워드 입니다.");
       }

       return MemberLoginResponseDto.builder()
               .email(loginMember.getEmail())
               .username(loginMember.getUsername())
               .token(jwtProvider.createToken(loginMember.getId(), loginMember.getEmail()))
               .build();
    }
}
