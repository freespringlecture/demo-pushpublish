package me.freelife;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.PushBuilder;

@Controller
public class SimpleController {

    @GetMapping("/")
    public String index(Model model, PushBuilder pushBuilder) {
        model.addAttribute("name", "freelife");
        model.addAttribute("message", "여러분 반갑습니다!!");
        /**
         * <script src="/webjars/jquery/3.3.1/dist/jquery.js"></script>
         *     <script src="/webjars/bootstrap/4.2.1/js/bootstrap.js"></script>
         *     <link rel="stylesheet" href="/webjars/bootstrap/4.2.1/css/bootstrap.min.css">
         */
        pushBuilder.path("/webjars/jquery/3.3.1/dist/jquery.js").push();
        pushBuilder.path("/webjars/bootstrap/4.2.1/js/bootstrap.js").push();
        pushBuilder.path("/webjars/bootstrap/4.2.1/css/bootstrap.min.css").push();

        return "index";
    }
}
