package com.example.demo.controller;

import java.math.BigDecimal;
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

import com.example.dto.Color;
import com.example.dto.Id;
import com.example.dto.Product;
import com.example.dto.Type;

@Controller
public class SearchProductController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(
			@RequestParam(name = "productName", defaultValue = "noName") String productName,
			@RequestParam(name = "type", defaultValue = "noType") String typeName,
			@RequestParam(name = "color", defaultValue = "noColor") String colorName,
			@RequestParam(name = "minNum", defaultValue = "0") int minNum,
			@RequestParam(name = "maxNum", defaultValue = "99999999") int maxNum,
			Model model) {
		System.out.println("searchを通ってます");

		List<Color> colorList = color();
		List<Type> typeList = type();
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

		List<Id> list = getId(trimProductName, typeName, colorName, minNum, maxNum);

		List<Product> productList = product(list);

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

	//色の全取得
	public List<Color> color() {
		String sql = "SELECT * FROM color_master";
		List<Map<String, Object>> colorList = jdbcTemplate.queryForList(sql);
		List<Color> list = new ArrayList<>();
		for (Map<String, Object> str : colorList) {
			Color color = new Color();
			color.setColorId((int) str.get("color_id"));
			color.setColorName((String) str.get("color_name"));
			list.add(color);
		}
		return list;
	}

	//種類の全取得
	public List<Type> type() {
		String sql = "SELECT * FROM type_master";
		List<Map<String, Object>> typeList = jdbcTemplate.queryForList(sql);
		List<Type> list = new ArrayList<>();
		for (Map<String, Object> str : typeList) {
			Type type = new Type();
			type.setTypeId((int) str.get("type_id"));
			type.setTypeName((String) str.get("type_name"));
			list.add(type);
		}
		return list;
	}

	//検索条件に当てはまる商品IDの取得
	public List<Id> getId(String productName, String type, String color, int min, int max) {
		List<Map<String, Object>> idList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.product_id ");
		sb.append("FROM product_master p ");
		sb.append("INNER JOIN color_master c ON p.color_id = c.color_id ");
		sb.append("INNER JOIN type_master t ON p.type_id = t.type_id ");
		/*
		 商品名、種類名、色名の順
		 */
		//111　完了
		if (!productName.equals("noName") && !type.equals("noType") && !color.equals("noColor")) {
			sb.append("WHERE p.product_name LIKE '%" + productName + "%' AND ");
			sb.append("t.type_name = ? AND ");
			sb.append("c.color_name = ? AND ");
			sb.append("p.price >= ? AND price <= ? ;");
			System.out.println(sb.toString());
			System.out.println("111");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, type, color, min, max);
		}
		//110　完了
		if (!productName.equals("noName") && !type.equals("noType") && color.equals("noColor")) {
			sb.append("WHERE p.product_name LIKE '%" + productName + "%' AND ");
			sb.append("t.type_name = ? AND ");
			sb.append("p.price >= ? AND price <= ? ;");
			System.out.println(sb.toString());
			System.out.println("110");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, type, min, max);
		}
		//101　完了
		if (!productName.equals("noName") && type.equals("noType") && !color.equals("noColor")) {
			sb.append("WHERE p.product_name LIKE '%" + productName + "%' AND ");
			sb.append("c.color_name = ? AND ");
			sb.append("p.price >= ? AND price <= ?;");
			System.out.println(sb.toString());
			System.out.println("101");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, color, min, max);
		}
		//011　完了
		if (productName.equals("noName") && !type.equals("noType") && !color.equals("noColor")) {
			sb.append("WHERE t.type_name = ? AND ");
			sb.append("c.color_name = ? AND ");
			sb.append("p.price >= ? AND price <= ? ;");
			System.out.println(sb.toString());
			System.out.println("011");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, type, color, min, max);
		}
		//001　完了
		if (productName.equals("noName") && type.equals("noType") && !color.equals("noColor")) {
			sb.append("WHERE c.color_name = ? AND ");
			sb.append("p.price >= ? AND price <= ? ;");
			System.out.println(sb.toString());
			System.out.println("001");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, color, min, max);
		}
		//010　完了
		if (productName.equals("noName") && !type.equals("noType") && color.equals("noColor")) {
			sb.append("WHERE t.type_name = ? AND ");
			sb.append("p.price >= ? AND price <= ?;");
			System.out.println(sb.toString());
			System.out.println("010");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, type, min, max);
		}
		//100　完了
		if (!productName.equals("noName") && type.equals("noType") && color.equals("noColor")) {
			sb.append("WHERE p.product_name LIKE '%" + productName + "%' AND ");
			sb.append("p.price >= ? AND price <= ?;");
			System.out.println(sb.toString());
			System.out.println("100");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, min, max);
		}
		//000　完了
		if (productName.equals("noName") && type.equals("noType") && color.equals("noColor")) {
			sb.append("WHERE p.price >= ? AND price <= ?;");
			System.out.println(sb.toString());
			System.out.println("000");
			String sql = sb.toString();
			idList = jdbcTemplate.queryForList(sql, min, max);
		}
		System.out.println(idList);
		List<Id> list = new ArrayList<>();
		for (Map<String, Object> str : idList) {
			Id id = new Id();
			id.setId((int) str.get("product_id"));
			list.add(id);
		}
		return list;
	}

	//商品の取得
	public List<Product> product(List<Id> idList) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.product_id AS p_id,t.type_name,p.product_name,c.color_name,p.price,");
		sb.append("(SELECT SUM(change_quantity) from change_log_table ch WHERE ch.product_id = ?) AS quantity ");
		sb.append("FROM product_master p INNER JOIN color_master c ON p.color_id = c.color_id ");
		sb.append("INNER JOIN type_master t ON p.type_id = t.type_id ");
		sb.append("WHERE p.product_id = ?;");
		String sql = sb.toString();

		List<Product> list = new ArrayList<>();
		for (int i = 0; i < idList.size(); i++) {
			System.out.println(idList.get(i).getId());
			List<Map<String, Object>> productList = jdbcTemplate.queryForList(sql, idList.get(i).getId(),
					idList.get(i).getId());
			for (Map<String, Object> str : productList) {
				Product product = new Product();
				product.setProductId((int) str.get("p_id"));
				product.setTypeName((String) str.get("type_name"));
				product.setProductName((String) str.get("product_name"));
				product.setColorName((String) str.get("color_name"));
				product.setPrice((int) str.get("price"));
				product.setQuantity((BigDecimal) str.get("quantity"));
				list.add(product);
			}
		}
		return list;
	}

	//空文字判定
	public String emptyString(String productName) {

		return productName;
	}
}
