package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ColorRegisterController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	//色登録画面に遷移
	@RequestMapping(value = "/color_register", method = RequestMethod.GET)
	public String main(Model model) {
		System.out.println("color_registerを通ってます");
		return "color_register";
	}

	//色を登録してメイン画面に遷移
	@RequestMapping(value = "/color_insert")
	public String colorRegisterSuccess(
			@RequestParam(name = "colorName") String colorName,
			Model model) {
		
		String stripColorName =colorName.strip();
		System.out.println(stripColorName.length());
		
		int colorCount = colorCount(stripColorName);
		if (0 < colorCount) {
			String errMsg = "すでに登録してある色名です";
			model.addAttribute("errmsg", errMsg);
			return "color_register";
		}
		
		if("".equals(stripColorName)) {
			String errMsg = "色名を入力してください";
			model.addAttribute("errmsg", errMsg);
			return "color_register";
		}
		
		if(stripColorName.length() > 10) {
			String errMsg = "最大文字数を超えています";
			model.addAttribute("errmsg", errMsg);
			return "color_register";
		}
		
		insertColor(stripColorName);
		return "main_menu";
	}

	//色の登録用SQL
	public void insertColor(String color) {
		jdbcTemplate.update("INSERT INTO color_master(color_name) Values(?)", color);
	}

	//色の重複確認用SQL
	public int colorCount(String colorName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(color_name) ");
		sb.append("FROM color_master ");
		sb.append("WHERE color_name ='" + colorName + "';");
		String sql = sb.toString();
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		System.out.println("カウント確認");
		System.out.println(count);
		return count;
	}
}
