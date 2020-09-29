package tpu.ru.labor.exchange.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/test")
public class TestRest {

    @GetMapping("/permission")
    public String checkPermissions() {
        return "You have access.";
    }

}
