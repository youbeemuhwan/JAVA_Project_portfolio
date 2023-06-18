package Project.commercial.controller;

import Project.commercial.Dto.MemberLoginRequestDto;
import Project.commercial.Dto.MemberLoginResponseDto;
import Project.commercial.Dto.MemberSignUpDto;
import Project.commercial.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/signup")
    @ResponseBody
    public String signUp(@RequestBody MemberSignUpDto memberSignUpDto){

        memberService.signUp(memberSignUpDto);

        return "SIGN UP DONE";

    }
    @GetMapping("/member/login")
    @ResponseBody
    public MemberLoginResponseDto login(@RequestBody MemberLoginRequestDto memberLoginRequestDto){
        return memberService.login(memberLoginRequestDto);
    }

    @GetMapping("/member/logout")
    @ResponseBody
    public String logout(){
        return "logout done";
    }

    @GetMapping("/member/testToken")
    @ResponseBody
    public String testToken(){
        return "Token Available";
    }

}
