package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShippingMenuController {

//	@Autowired
//	JdbcTemplate jdbcTemplate;
	
	
    @RequestMapping("/shipping_process")
    public String shippingProcess() {
        return "/shipping_process"; 
	}
			
    @RequestMapping("/shipping_excel")
	    public String shippingExcel() {
	        return "/shipping_excel"; 
		
	}
}
