package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShippingController {

//	@Autowired
//	JdbcTemplate jdbcTemplate;
	
	
    @GetMapping("/shipping")
    public String shipping() {
        return "/shipping"; 
	}
}
