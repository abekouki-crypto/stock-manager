package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StoringMenuController {

//	@Autowired
//	JdbcTemplate jdbcTemplate;
	
	
    @RequestMapping("/storing_process")
    public String storingProcess() {
        return "/storing_process"; 
	}
			
    @RequestMapping("/storing_excel")
	    public String storingExcel() {
	        return "/storing_excel"; 
		
	}
}
