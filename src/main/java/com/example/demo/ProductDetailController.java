package com.example.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dto.Product;

@Controller
public class ProductDetailController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	// 商品詳細画面に遷移
	@GetMapping("/product_detail/{id}")
	public String hello(@PathVariable("id") int id, Model model) {
	
		Product productName = selectProduct(id);
		
		model.addAttribute("productName", productName);

		return "product_detail";

	}

	// SQL
	// 商品情報の一件取得
	public Product selectProduct(int id) {

		// SELECT文		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.product_id AS p_id,t.type_name,p.product_name,c.color_name,p.price,p.picture_path ");
		sb.append("FROM product_master p INNER JOIN color_master c ON p.color_id = c.color_id ");
		sb.append("INNER JOIN type_master t ON p.type_id = t.type_id ");
		sb.append("WHERE p.product_id = ?;");
		
		String query = sb.toString();
		// 検索実行、mapで取得した値をproductクラスのインスタンスにセット
		Map<String, Object> product = jdbcTemplate.queryForMap(query,id);

		// Mapから値を取得
		int productId = (int) product.get("p_id");
		String productName = (String) product.get("product_name");
		String typeName = (String) product.get("type_name");
		String colorName = (String) product.get("color_name");
		int price = (int) product.get("price");
		String picturePath = (String) product.get("picture_path");
		
		// productクラスに値をセット
		Product setProduct = new Product();
		setProduct.setProductId(productId);
		setProduct.setProductName(productName);
		setProduct.setTypeName(typeName);
		setProduct.setColorName(colorName);
		setProduct.setPrice(price);
		setProduct.setPicture(picturePath);

		return setProduct;
	}

}
