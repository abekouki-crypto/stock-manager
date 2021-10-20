package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.service.UpdateQuantityService;
import com.example.dto.InputModel;
import com.example.dto.RegistInfo;

@Controller
public class UpdateQuantityController {
	@Autowired
	UpdateQuantityService updateQuantityService;

	@RequestMapping(value = "/updateQuantity", method = RequestMethod.GET)
	public String updateQuantity(
			@ModelAttribute InputModel form) {
		System.out.println(form.getId().size());
		System.out.println(form.getQuantity());
		System.out.println(form.getLogReason().size());
		List<RegistInfo> list = updateQuantityService.registList(form);
		updateQuantityService.updateQuantity(list);
		System.out.println("通っている");

		return "main_menu";
	}
}
