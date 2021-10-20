package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.InsertTypeService;

@Controller
public class TypeRegisterController {
	@Autowired
	InsertTypeService insertTypeService;

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

		int typeCount = insertTypeService.typeCount(stripTypeName);
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
		insertTypeService.insertType(stripTypeName);
		return "main_menu";
	}
}