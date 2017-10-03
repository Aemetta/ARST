package com.aemetta.arst.player;

public enum Shape {
	
	I(1,
			new int[]{3,4,5,6},
			new int[]{22,22,22,22},
			new Wang[]{Wang.WCAP, Wang.HORIZ, Wang.HORIZ, Wang.ECAP}),
	O(2,
			new int[]{4,4,5,5},
			new int[]{23,22,23,22},
			new Wang[]{Wang.NWCORN, Wang.SWCORN, Wang.NECORN, Wang.SECORN}),
	T(3,
			new int[]{3,4,4,5},
			new int[]{22,22,23,22},
			new Wang[]{Wang.WCAP, Wang.STEE, Wang.NCAP, Wang.ECAP}),
	Z(4,
			new int[]{5,4,4,3},
			new int[]{22,22,23,23},
			new Wang[]{Wang.ECAP, Wang.SWCORN, Wang.NECORN, Wang.WCAP}),
	S(5,
			new int[]{3,4,4,5},
			new int[]{22,22,23,23},
			new Wang[]{Wang.WCAP, Wang.SECORN, Wang.NWCORN, Wang.ECAP}),
	J(6,
			new int[]{5,4,3,3},
			new int[]{22,22,22,23},
			new Wang[]{Wang.ECAP, Wang.HORIZ, Wang.SWCORN, Wang.NCAP}),
	L(7,
			new int[]{3,4,5,5},
			new int[]{22,22,22,23},
			new Wang[]{Wang.WCAP, Wang.HORIZ, Wang.SECORN, Wang.NCAP}),
	Brace(8,
			new int[]{5,4,4},
			new int[]{22,22,23},
			new Wang[]{Wang.ECAP, Wang.SWCORN, Wang.NCAP}),
	Twig(9,
			new int[]{5,4,3},
			new int[]{22,22,22},
			new Wang[]{Wang.ECAP, Wang.HORIZ, Wang.WCAP}),
	Dot(10,
			new int[]{4},
			new int[]{22},
			new Wang[]{Wang.LONER});
	
	public int color;
	public int[] x;
	public int[] y;
	public Wang[] wang;
	
	Shape(int color, int[] x, int[] y, Wang[] shape){
		this.color = color;
		this.x = x;
		this.y = y;
		this.wang = shape;
	}
}
