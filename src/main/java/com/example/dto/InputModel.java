package com.example.dto;

import java.util.List;

import lombok.Data;

@Data
public class InputModel {
	List<Integer> id;
	List<String> quantity;
	List<String> logReason;
}
