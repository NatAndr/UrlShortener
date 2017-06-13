package com.andrianova.url.shortener.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by natal on 12-Jun-17.
 */
@Controller
public class HelpController {

    @RequestMapping("/help")
    public String getHelp() {
        return "redirect:/static/help.html";
    }
}
