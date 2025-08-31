package com.crediya.model.constants;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class SalaryBaseRules {

	public final BigDecimal MIN = BigDecimal.valueOf(0);
	public final BigDecimal MAX = BigDecimal.valueOf(15_000_000);
	
}
