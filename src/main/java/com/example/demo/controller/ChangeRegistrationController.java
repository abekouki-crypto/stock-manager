package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.Color;
import com.example.dto.Product;
import com.example.dto.Type;

@Controller
public class ChangeRegistrationController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	// 商品情報変更画面に遷移
	@RequestMapping(value = "/change_registration", method = RequestMethod.GET)
	public String main(@RequestParam(name = "id", defaultValue = "noId") int productId, Model model) {
		System.out.println("change_registrationを通ってます");

		Product product = selectProduct(productId);
		List<Color> colorList = color();
		List<Type> typeList = type();

		model.addAttribute("productId", productId);
		model.addAttribute("product", product);
		model.addAttribute("colorlist", colorList);
		model.addAttribute("typeList", typeList);

		return "change_registration";
	}

	// 商品情報を変更してメイン画面に遷移
	@RequestMapping(value = "/update_one")
	public String changeRegistrationSuccess(@RequestParam(name = "id", defaultValue = "noId") int productId,
			@RequestParam(name = "product", defaultValue = "noName") String product,
			@RequestParam(name = "type", defaultValue = "noType") String type,
			@RequestParam(name = "color", defaultValue = "noColor") String color,
			@RequestParam(name = "price", defaultValue = "0") Integer price, MultipartFile multipartFile, Model model) {

		System.out.println("これがぁ！！！！商品！！！IDダッッッッッッッッッ！！！！！！！！！！" + productId);

		if (!multipartFile.isEmpty()) {
			try {
				saveImage(multipartFile);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		String stripProduct = product.strip();

		int colorId = colorId(color);
		int typeId = typeId(type);

		updateOne(stripProduct, typeId, colorId, price, multipartFile, productId);

		return "main_menu";
	}

	// 色の全取得用SQL
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

	// 種類の全取得用SQL
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

	// 色名からIDの取得用SQL
	public int colorId(String color) {
		String sql = "SELECT color_id FROM color_master WHERE color_name = ?;";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, color);
		int colorId = (int) map.get("color_id");
		return colorId;
	}

	// 種類名からIDの取得用SQL
	public int typeId(String type) {
		String sql = "SELECT type_id FROM type_master WHERE type_name = ?;";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, type);
		int typeId = (int) map.get("type_id");
		return typeId;
	}

	// SQL
	// 商品情報の一件取得
	public Product selectProduct(int productId) {

		// SELECT文
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.product_id AS p_id,t.type_name,p.product_name,c.color_name,p.price,p.picture_path ");
		sb.append("FROM product_master p INNER JOIN color_master c ON p.color_id = c.color_id ");
		sb.append("INNER JOIN type_master t ON p.type_id = t.type_id ");
		sb.append("WHERE p.product_id = ?;");

		String query = sb.toString();
		// 検索実行、mapで取得した値をproductクラスのインスタンスにセット
		Map<String, Object> product = jdbcTemplate.queryForMap(query, productId);

		// Mapから値を取得
		int id = (int) product.get("p_id");
		String productName = (String) product.get("product_name");
		String typeName = (String) product.get("type_name");
		String colorName = (String) product.get("color_name");
		int price = (int) product.get("price");
		String picturePath = (String) product.get("picture_path");

		// productクラスに値をセット
		Product setProduct = new Product();
		setProduct.setProductId(id);
		setProduct.setProductName(productName);
		setProduct.setTypeName(typeName);
		setProduct.setColorName(colorName);
		setProduct.setIntPrice(price);
		// setProduct.setPrice(price);
		setProduct.setPicture(picturePath);

		return setProduct;
	}

	// 商品情報を１件更新.
	public void updateOne(String productName, int typeId, int colorId, int price, MultipartFile multipartFile,
			int productId) {

		// ファイル名をに現在年月日時分秒にリネーム

		// htmlでアップロードしたファイル名を取得
		File oldFileName = new File(multipartFile.getOriginalFilename());

		// 現在日時情報で初期化されたインスタンスの取得
		LocalDateTime localNowDateTime = LocalDateTime.now();
		DateTimeFormatter javaFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		// 日時情報を指定フォーマットの文字列で取得
		String nowDateTime = localNowDateTime.format(javaFormat);
		File newFileName = new File(nowDateTime + ".jpg");
		oldFileName.renameTo(newFileName);

		// DBへ保存するパス
		String picturePath = "../img/";

		// パスを先頭に付けてファイル名をくっつける
		File input = new File(picturePath + newFileName);
		String stringInputName = picturePath + input.getName();

		String query = "UPDATE product_master SET product_name = ? , type_id = ? , color_id = ? , price = ? , picture_path = ? WHERE product_id = ?";

		// １件更新
		int product = jdbcTemplate.update(query, productName, typeId, colorId, price, stringInputName, productId);
		System.out.println(product + "件登録しました");
	}

	// imgファイルに画像の保存
	public void saveImage(MultipartFile multipartFile) throws IOException {
		// ファイル名をに現在年月日時分秒にリネーム

		// ファイル名を取得
		File oldFileName = new File(multipartFile.getOriginalFilename());

		// 現在日時情報で初期化されたインスタンスの取得
		LocalDateTime localNowDateTime = LocalDateTime.now();
		DateTimeFormatter javaFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		// 日時情報を指定フォーマットの文字列で取得
		String nowDateTime = localNowDateTime.format(javaFormat);
		File newFileName = new File(nowDateTime + ".jpg");
		oldFileName.renameTo(newFileName);

		// 保存先を定義
		String uploadPath = "src/main/resources/static/img/";
		byte[] bytes = multipartFile.getBytes();

		// 指定ファイルへ読み込みファイルを書き込み
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(new File(uploadPath + newFileName)));
		stream.write(bytes);
		stream.close();
	}

}
