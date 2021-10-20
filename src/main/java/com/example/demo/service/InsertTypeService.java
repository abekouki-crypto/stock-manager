package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class InsertTypeService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	* 引数の種類名を登録
	* @param String 色名
	* @return
	*/
	public void insertType(String type) {
		jdbcTemplate.update("INSERT INTO type_master(type_name) Values(?)", type);
	}

	/**
	* 登録する種類の重複チェック
	* @param　String　種類名
	* @return 重複した数
	*/
	public int typeCount(String typeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(type_name) ");
		sb.append("FROM type_master ");
		sb.append("WHERE type_name ='" + typeName + "';");
		String sql = sb.toString();
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		System.out.println("カウント確認");
		System.out.println(count);
		return count;
	}
}
