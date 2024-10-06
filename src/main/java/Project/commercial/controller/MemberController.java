package Project.commercial.controller;

import Project.commercial.dto.member.MemberLoginRequestDto;
import Project.commercial.dto.member.MemberLoginResponseDto;
import Project.commercial.dto.member.CreateMemberDto;
import Project.commercial.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping()
    @ResponseBody
    public String createMember(@RequestBody CreateMemberDto createMemberDto)
    {
        memberService.signUp(createMemberDto);

        return "SIGN UP DONE";
    }
    @GetMapping("/login")
    @ResponseBody
    public MemberLoginResponseDto login(@RequestBody MemberLoginRequestDto memberLoginRequestDto)
    {
        return memberService.login(memberLoginRequestDto);
    }

}
