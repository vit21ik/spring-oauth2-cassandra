package paradigma.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created on 5/19/16.
 */
@RestController
public class UserController {

    @RequestMapping("/api/me")
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }

    @RequestMapping("/test")
    public Map<String, String> test() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("value", "test");
        map.put("date", String.valueOf(LocalDate.now()));
        return map;
    }

}