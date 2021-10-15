package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TypeRegisterController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	// 種類登録画面に遷移
	@RequestMapping(value = "/type_register", method = RequestMethod.GET)
	public String main(Model model) {
		System.out.println("type_registerを通ってます");
		return "type_register";
	}

	// 種類を登録してメイン画面に遷移
	@RequestMapping("/type_insert")
	public String typeRegisterSuccess(@RequestParam(name = "typeName") String typeName, Model model) {

		String stripTypeName = typeName.strip();

		int typeCount = typeCount(stripTypeName);
		if (0 < typeCount) {
			String errMsg = "すでに登録してある種類名です";
			model.addAttribute("errmsg", errMsg);
			return "type_register";
		}

		if ("".equals(stripTypeName)) {
			String errMsg = "種類名を入力してください";
			model.addAttribute("errmsg", errMsg);
			return "type_register";
		}

		if (stripTypeName.length() > 20) {
			String errMsg = "最大文字数を超えています";
			model.addAttribute("errmsg", errMsg);
			return "type_register";
		}
		insertType(stripTypeName);
		return "main_menu";
	}

	// 種類の登録用SQL
	public void insertType(String type) {
		jdbcTemplate.update("INSERT INTO type_master(type_name) Values(?)", type);
	}

	// 種類の重複確認用SQL
	public int typeCount(String typeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(type_name) ");
		sb.append("FROM type_master ");
		sb.append("WHERE type_name ='" + typeName + "';");
		String sql = sb.toString();
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		System.out.println("カウント確認");
		System.out.println(count);
		return count;
	}
}