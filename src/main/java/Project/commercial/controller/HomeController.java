package Project.commercial.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {
    @GetMapping(value = {"/", "/home", "/main"})
    @ResponseBody
    public String home(){
        return "Home";

    }
}
