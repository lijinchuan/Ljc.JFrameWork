package Ljc.JFramework.TypeUtil;

import java.math.BigDecimal;

public class UInt64 {
	public static BigDecimal MAX_VAL = new BigDecimal("18446744073709551615");
	private long _val = 0;

	public UInt64(BigDecimal val) {
		_val = val.longValue();
	}

	public static UInt64 valueOf(BigDecimal val) {

		return new UInt64(val);
	}

	public BigDecimal getVal() {
		if (_val > 0) {
			return BigDecimal.valueOf(_val);
		}

		return MAX_VAL.add(BigDecimal.valueOf(_val));
	}
}
