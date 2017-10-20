package com.aemetta.arst.player;

public enum Shape {
	
	I(Color.Cyan,
			new int[]{3,4,5,6},
			new int[]{22,22,22,22},
			new Wang[]{Wang.WCAP, Wang.HORIZ, Wang.HORIZ, Wang.ECAP}),
	O(Color.Yellow,
			new int[]{4,4,5,5},
			new int[]{23,22,23,22},
			new Wang[]{Wang.NWCORN, Wang.SWCORN, Wang.NECORN, Wang.SECORN}),
	T(Color.Magenta,
			new int[]{3,4,4,5},
			new int[]{22,22,23,22},
			new Wang[]{Wang.WCAP, Wang.STEE, Wang.NCAP, Wang.ECAP}),
	Z(Color.Red,
			new int[]{5,4,4,3},
			new int[]{22,22,23,23},
			new Wang[]{Wang.ECAP, Wang.SWCORN, Wang.NECORN, Wang.WCAP}),
	S(Color.Green,
			new int[]{3,4,4,5},
			new int[]{22,22,23,23},
			new Wang[]{Wang.WCAP, Wang.SECORN, Wang.NWCORN, Wang.ECAP}),
	J(Color.Blue,
			new int[]{5,4,3,3},
			new int[]{22,22,22,23},
			new Wang[]{Wang.ECAP, Wang.HORIZ, Wang.SWCORN, Wang.NCAP}),
	L(Color.Orange,
			new int[]{3,4,5,5},
			new int[]{22,22,22,23},
			new Wang[]{Wang.WCAP, Wang.HORIZ, Wang.SECORN, Wang.NCAP}),
	Brace(Color.Brown,
			new int[]{5,4,4},
			new int[]{22,22,23},
			new Wang[]{Wang.ECAP, Wang.SWCORN, Wang.NCAP}),
	Twig(Color.White,
			new int[]{5,4,3},
			new int[]{22,22,22},
			new Wang[]{Wang.ECAP, Wang.HORIZ, Wang.WCAP}),
	Dot(Color.White,
			new int[]{4},
			new int[]{22},
			new Wang[]{Wang.LONER});
	
	public Color color;
	public int[] x;
	public int[] y;
	public Wang[] wang;
	
	Shape(Color color, int[] x, int[] y, Wang[] shape){
		this.color = color;
		this.x = x;
		this.y = y;
		this.wang = shape;
	}
}
