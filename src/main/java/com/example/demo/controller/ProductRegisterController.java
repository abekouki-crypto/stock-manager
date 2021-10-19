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
import com.example.dto.Type;

@Controller
public class ProductRegisterController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	// 商品登録画面に遷移
	@RequestMapping(value = "/product_register", method = RequestMethod.GET)
	public String main(Model model) {
		System.out.println("product_registerを通ってます");

		List<Color> colorList = color();
		List<Type> typeList = type();

		model.addAttribute("colorlist", colorList);
		model.addAttribute("typeList", typeList);

		return "product_register";
	}

	// 商品情報を登録してメインに遷移
	@RequestMapping("/product_insert")
	public String productRegisterSuccess(@RequestParam(name = "product", defaultValue = "noName") String product,
			@RequestParam(name = "type", defaultValue = "noType") String type,
			@RequestParam(name = "color", defaultValue = "noColor") String color,
			@RequestParam(name = "price", defaultValue = "0") Integer price, MultipartFile multipartFile, Model model) {
		System.out.println("ここは通ってますもんね？");


		if (!multipartFile.isEmpty()) {
			try {
				saveImage(multipartFile);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		String stripProduct = product.strip();

		// 空欄チェック
		if (product.equals("noName") || type.equals("noType") || color.equals("noColor") || price == 0) {

			List<Color> colorList = color();
			List<Type> typeList = type();

			model.addAttribute("colorlist", colorList);
			model.addAttribute("typeList", typeList);

			String errMsg = "空の欄があります";
			model.addAttribute("errmsg", errMsg);
			return "product_register";
		}

		// 商品名の重複チェック
		int productCount = productCount(stripProduct);
		if (0 < productCount) {
			String errMsg = "すでに登録してある商品名です";
			model.addAttribute("errmsg", errMsg);
			return "product_register";
		}

		// 空文字チェック
		if ("".equals(stripProduct)) {
			String errMsg = "商品名を入力してください";
			model.addAttribute("errmsg", errMsg);
			return "product_register";
		}

		// 商品名の最大文字数チェック
		if (stripProduct.length() > 100) {
			String errMsg = "最大文字数を超えています";
			model.addAttribute("errmsg", errMsg);
			return "product_register";
		}

		int colorId = colorId(color);
		int typeId = typeId(type);
		insertProductMaster(stripProduct, typeId, colorId, price, multipartFile);

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
		System.out.println(colorId);
		return colorId;
	}

	// 種類名からIDの取得用SQL
	public int typeId(String type) {
		String sql = "SELECT type_id FROM type_master WHERE type_name = ?;";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, type);
		int typeId = (int) map.get("type_id");
		System.out.println(typeId);
		return typeId;
	}

	// 商品マスタ登録用SQL
	public void insertProductMaster(String productName, int typeId, int colorId, int price,
			MultipartFile multipartFile) {

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
		System.out.println(stringInputName);

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO product_master ");
		sb.append("(product_name,type_id,color_id,price,picture_path) ");
		sb.append("VALUES(?,?,?,?,?);");
		String sql = sb.toString();
		int num = jdbcTemplate.update(sql, productName, typeId, colorId, price, stringInputName);
		System.out.println(num + "件登録しました");
	}

	// 商品名の重複確認用SQL
	public int productCount(String productName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT COUNT(product_name) ");
		stringBuilder.append("FROM product_master ");
		stringBuilder.append("WHERE product_name ='" + productName + "';");
		String sql = stringBuilder.toString();
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		System.out.println("カウント確認");
		System.out.println(count);
		return count;
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
