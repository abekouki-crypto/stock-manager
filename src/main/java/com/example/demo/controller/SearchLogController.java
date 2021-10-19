package com.example.demo.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.ProductLog;

@Controller
public class SearchLogController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/searchLog", method = RequestMethod.GET)
	public String searchLog(
			@RequestParam(name = "productId") Integer id,
			Model model) {
		String productName = productName(id);
		List<ProductLog> list = productLog(id);
		model.addAttribute("productName", productName);
		model.addAttribute("logList", list);
		return "product_log";
	}

	//SQL文
	//IDから商品名の取得
	public String productName(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT product_name ");
		sb.append("FROM product_master ");
		sb.append("WHERE product_id = ?");
		String sql = sb.toString();
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, id);
		String productName = (String) map.get("product_name");
		System.out.println(productName);
		return productName;
	}

	//履歴の取得
	public List<ProductLog> productLog(int id) {
		List<ProductLog> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.product_name, c.change_date, c.change_contents, c.change_quantity ");
		sb.append("FROM change_log_table c ");
		sb.append("INNER JOIN product_master p ON c.product_id = p.product_id ");
		sb.append("WHERE c.product_id = ?;");
		String sql = sb.toString();

		List<Map<String, Object>> productLogList = jdbcTemplate.queryForList(sql, id);

		for (Map<String, Object> str : productLogList) {
			ProductLog productLog = new ProductLog();
			productLog.setProductName((String) str.get("product_name"));
			productLog.setChangeDate((Date) str.get("change_date"));
			productLog.setContent((String) str.get("change_contents"));
			productLog.setQuantity((int) str.get("change_quantity"));
			list.add(productLog);
		}
		return list;
	}

}
