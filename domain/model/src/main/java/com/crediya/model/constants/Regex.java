package com.crediya.model.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Regex {
	
	public final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	public final String IDENTIFICATION = "^\\d+$";
	
}
