package org.d3javu.bd.controllers.baseController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BaseController {

    @GetMapping
    public String index() {
        return "redirect:/posts";
    }
}
