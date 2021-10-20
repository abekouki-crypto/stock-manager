package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.dto.Id;

public class SearchProductIdService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	* 引数の色名を登録
	* @param String 商品名
	* 		 String 種類名
	* 		 String 色名
	* 		 int 最小価格
	* 		 int 最大価格
	* @return IDのリスト
	*/
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
}
