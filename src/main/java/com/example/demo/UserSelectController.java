package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSelectController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	// userテーブルの全データ抽出クエリ実行
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public String userAll() {
		return jdbcTemplate.queryForList("SELECT * FROM class_master").toString();
	}
	// userテーブルのID抽出
	@RequestMapping(path="/users/{id}", method=RequestMethod.GET)
	public String userMatchId(@PathVariable String id) {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("SELEC * FROM user WHERE id = ?", id);
		return list.toString();
	}
}