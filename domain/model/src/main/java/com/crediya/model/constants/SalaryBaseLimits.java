package com.crediya.model.constants;

import java.math.BigDecimal;

public class SalaryBaseLimits {

    private SalaryBaseLimits() {}
	
	public static final BigDecimal MIN = BigDecimal.valueOf(0);
	public static final BigDecimal MAX = BigDecimal.valueOf(15_000_000);
	
}
