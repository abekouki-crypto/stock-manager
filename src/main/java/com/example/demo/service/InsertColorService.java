package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertColorService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	* 引数の色名を登録
	* @param String 色名
	* @return
	*/
	public void insertColor(String color) {
		jdbcTemplate.update("INSERT INTO color_master(color_name) Values(?)", color);
	}

	/**
	* 登録する色の重複チェック
	* @param　String　色名
	* @return 重複した数
	*/
	public int colorCount(String colorName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(color_name) ");
		sb.append("FROM color_master ");
		sb.append("WHERE color_name ='" + colorName + "';");
		String sql = sb.toString();
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		return count;
	}
}
