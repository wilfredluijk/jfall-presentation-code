package nl.profit4cloud.hero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WorkaroundController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public String console() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "forward:/index.html";
    }
}
