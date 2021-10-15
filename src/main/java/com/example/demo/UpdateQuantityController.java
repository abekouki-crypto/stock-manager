package com.example.demo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.dto.RegistInfo;

import lombok.Data;

@Controller
public class UpdateQuantityController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/updateQuantity", method = RequestMethod.GET)
	public String updateQuantity(
			@ModelAttribute InputModel form) {
		System.out.println(form.id.size());
		System.out.println(form.quantity);
		System.out.println(form.logReason.size());
		List<RegistInfo> list = registList(form);
		updateQuantity(list);
		System.out.println("通っている");

		return "main_menu";
	}

	@Data
	public static class InputModel {
		List<Integer> id;
		List<String> quantity;
		List<String> logReason;
	}

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
		for (int i = 0; i < model.id.size(); i++) {
			if (!model.quantity.get(i).equals("")) {

				//int id = Integer.parseInt(model.id.get(i));
				int quantity = Integer.parseInt(model.quantity.get(i));

				RegistInfo rInfo = new RegistInfo();

				rInfo.setId(model.id.get(i));
				rInfo.setSqlDate(sqlDate);
				rInfo.setQuantity(quantity);
				rInfo.setLogReason(model.logReason.get(i));

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
