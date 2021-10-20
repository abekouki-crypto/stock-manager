package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.ReturnListService;
import com.example.demo.service.SearchProductIdService;
import com.example.dto.Color;
import com.example.dto.Id;
import com.example.dto.Product;
import com.example.dto.Type;

@Controller
public class SearchProductController {
	@Autowired
	ReturnListService returnListService;
	SearchProductIdService searchProductIdService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(
			@RequestParam(name = "productName", defaultValue = "noName") String productName,
			@RequestParam(name = "type", defaultValue = "noType") String typeName,
			@RequestParam(name = "color", defaultValue = "noColor") String colorName,
			@RequestParam(name = "minNum", defaultValue = "0") int minNum,
			@RequestParam(name = "maxNum", defaultValue = "99999999") int maxNum,
			Model model) {
		System.out.println("searchを通ってます");

		List<Color> colorList = returnListService.color();
		List<Type> typeList = returnListService.type();
		model.addAttribute("colorList", colorList);
		model.addAttribute("typeList", typeList);

		String trimProductName = productName.strip();

		System.out.println(trimProductName);
		System.out.println(typeName);
		System.out.println(colorName);
		System.out.println(minNum);
		System.out.println(maxNum);

		String searchHeader = "検索結果";
		if (minNum >= maxNum) {
			System.out.println("数字の並びおかしいぜ");
			String errMsg = "数字の範囲が異常です";
			model.addAttribute("errmsg", errMsg);
			return "main_menu";
		}

		List<Id> list = searchProductIdService.getId(trimProductName, typeName, colorName, minNum, maxNum);

		List<Product> productList = returnListService.product(list);

		if (!(productList.size() == 0)) {
			model.addAttribute("productList", productList);
		} else {
			String notSearchResult = "検索条件に一致する結果が見つかりません。";
			model.addAttribute("notSearchResult", notSearchResult);
		}
		model.addAttribute("searchHeader", searchHeader);
		return "main_menu";
	}

	/*
	 SQL
	 * */

}
