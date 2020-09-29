package tpu.ru.labor.exchange.rest;

import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/")
public class RootRest {

    @GetMapping
    public String getCurrentTime() {
        return "Service worked.";
    }
}
