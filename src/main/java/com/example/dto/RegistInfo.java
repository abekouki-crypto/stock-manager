package com.example.dto;

import java.sql.Date;

public class RegistInfo {
	int id;
	Date sqlDate;
	int quantity;
	String logReason;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getSqlDate() {
		return sqlDate;
	}

	public void setSqlDate(Date sqlDate) {
		this.sqlDate = sqlDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getLogReason() {
		return logReason;
	}

	public void setLogReason(String logReason) {
		this.logReason = logReason;
	}
}
