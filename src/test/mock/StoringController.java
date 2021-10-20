package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoringController {

//	@Autowired
//	JdbcTemplate jdbcTemplate;
	
	
    @GetMapping("/storing")
    public String storing() {
        return "/storing"; 
	}
}
