package com.example.demo.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.dto.InputModel;
import com.example.dto.RegistInfo;

public class UpdateQuantityService {
	@Autowired
	JdbcTemplate jdbcTemplate;

	//数量更新SQL
	public int updateQuantity(List<RegistInfo> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO change_log_table ");
		sb.append("(product_id, change_date, change_contents, change_quantity) ");
		sb.append("VALUES(?,?,?,?);");
		String sql = sb.toString();
		int insertNumber = 0;
		for (int i = 0; i < list.size(); i++) {
			jdbcTemplate.update(sql, list.get(i).getId(), list.get(i).getSqlDate(), list.get(i).getLogReason(),
					list.get(i).getQuantity());
			insertNumber++;
		}
		System.out.println(insertNumber + "件登録しました");
		return insertNumber;
	}

	//更新用リスト作成
	public List<RegistInfo> registList(InputModel model) {
		List<RegistInfo> list = new ArrayList<>();
		Date sqlDate = sqlDate();
		for (int i = 0; i < model.getId().size(); i++) {
			if (!model.getQuantity().get(i).equals("")) {

				//int id = Integer.parseInt(model.id.get(i));
				int quantity = Integer.parseInt(model.getQuantity().get(i));

				RegistInfo rInfo = new RegistInfo();

				rInfo.setId(model.getId().get(i));
				rInfo.setSqlDate(sqlDate);
				rInfo.setQuantity(quantity);
				rInfo.setLogReason(model.getLogReason().get(i));

				list.add(rInfo);
			}
		}
		return list;
	}

	//SQLデータに適応したデート型の変換
	public Date sqlDate() {
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();

		java.util.Date dateObj = calendar.getTime();
		String formattedDate = dtf.format(dateObj);
		System.out.println(formattedDate);
		Date date = Date.valueOf(formattedDate);
		System.out.println(formattedDate);
		return date;
	}
}
