package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.InsertColorService;

@Controller
public class ColorRegisterController {
	@Autowired
	InsertColorService insertColorService;

	//色登録画面に遷移
	@RequestMapping(value = "/color_register")
	public String main(Model model) {
		System.out.println("color_registerを通ってます");
		return "color_register";
	}

	//色を登録してメイン画面に遷移
	@RequestMapping(value = "/color_insert")
	public String colorRegisterSuccess(
			@RequestParam(name = "colorName") String colorName,
			Model model) {
		String stripColorName = colorName.strip();
		System.out.println(stripColorName.length());

		int colorCount = insertColorService.colorCount(stripColorName);
		if (0 < colorCount) {
			String errMsg = "すでに登録してある色名です";
			model.addAttribute("errmsg", errMsg);
			return "color_register";
		}

		if ("".equals(stripColorName)) {
			String errMsg = "色名を入力してください";
			model.addAttribute("errmsg", errMsg);
			return "color_register";
		}

		if (stripColorName.length() > 10) {
			String errMsg = "最大文字数を超えています";
			model.addAttribute("errmsg", errMsg);
			return "color_register";
		}

		insertColorService.insertColor(stripColorName);
		return "main_menu";
	}
}
