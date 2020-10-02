package tpu.ru.labor.exchange.rest;

import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/")
public class RootRest {

    @GetMapping
    public String checkServiceStatus() {
        return "Service worked.";
    }
}
