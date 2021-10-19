package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.ReturnListService;
import com.example.dto.Color;
import com.example.dto.Id;
import com.example.dto.Product;
import com.example.dto.Type;

@Controller
public class MainController {

	@Autowired
	private ReturnListService listService;

	@RequestMapping(value = "/")
	public String main(Model model) {
		System.out.println("mainを通ってます");

		List<Color> colorList = listService.color();
		List<Type> typeList = listService.type();
		List<Id> idList = listService.id();
		List<Product> productList = listService.product(idList);

		model.addAttribute("colorList", colorList);
		model.addAttribute("typeList", typeList);
		model.addAttribute("productList", productList);
		return "main_menu";
	}

}
