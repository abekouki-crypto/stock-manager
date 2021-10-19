package com.example.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.dto.Color;
import com.example.dto.Id;
import com.example.dto.Product;
import com.example.dto.Type;

@Service
public class ReturnListService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	* 色テーブルにある色の全取得
	* @param
	* @return 全ての色
	*/
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

	/**
	* 種類テーブルにある色の全取得
	* @param
	* @return 全ての種類
	*/
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

	/**
	 * 商品IDの全取得
	 * @param
	 * @return 全ての商品ID
	 */
	public List<Id> id() {
		String sql = "SELECT product_id FROM product_master;";
		List<Map<String, Object>> idList = jdbcTemplate.queryForList(sql);
		List<Id> list = new ArrayList<>();
		for (Map<String, Object> str : idList) {
			Id id = new Id();
			id.setId((int) str.get("product_id"));
			list.add(id);
		}
		return list;
	}

	/**
	 * リストにあるIDから商品を取得する
	 * @param List<Id>
	 * @return 商品データリスト
	 */
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
}
