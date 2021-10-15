package com.example.dto;

import java.math.BigDecimal;

public class Product {
	int productId;
	String typeName;
	String productName;
	String colorName;
	int intPrice;
	String strPrice;
	String strQuantity;
	String picture;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {

		this.productName = productName;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public int getIntPrice() {
		return intPrice;
	}

	public void setIntPrice(int price) {
		this.intPrice = price;
	}

	public String getPrice() {
		return strPrice;
	}

	public void setPrice(int price) {
		// 3桁ごとにカンマ
		String strPrice = String.format("%,d", price);
		this.strPrice = strPrice;
	}

	public String getQuantity() {
		return strQuantity;
	}

	public void setQuantity(BigDecimal quantity) {
		// 3桁ごとにカンマ
		// nullの場合空文字入力
		if (quantity == null) {
			String strQuantity = "";
			this.strQuantity = strQuantity;
		} else {
			int intQuantity = quantity.intValue();
			String strQuantity = String.format("%,d", intQuantity);
			this.strQuantity = strQuantity;
		}
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
