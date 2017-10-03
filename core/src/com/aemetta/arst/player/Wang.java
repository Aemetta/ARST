package com.aemetta.arst.player;

public enum Wang {
	
	LONER(0, 0),
	MIDDLE(1, 2),
	HORIZ(2, 0),
	VERT(3, 2),
	WCAP(1, 0),
	ECAP(3, 0),
	NCAP(3, 1),
	SCAP(3, 3),
	WTEE(0, 2),
	ETEE(2, 2),
	NTEE(1, 1),
	STEE(1, 3),
	NWCORN(0, 1),
	NECORN(2, 1),
	SWCORN(0, 3),
	SECORN(2, 3);
	
	public int x;
	public int y;
	
	Wang(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Wang rotate(int a){
		switch(this.name()){
		case "LONER": break;
		case "MIDDLE": break;
		case "HORIZ":
			if(a!=2)return Wang.VERT;
			break;
		case "VERT":
			if(a!=2)return Wang.HORIZ;
			break;
		case "WCAP":
			if(a==1)return Wang.NCAP;
			else if(a==2)return Wang.ECAP;
			else if(a==3)return Wang.SCAP;
			break;
		case "ECAP":
			if(a==1)return Wang.SCAP;
			else if(a==2)return Wang.WCAP;
			else if(a==3)return Wang.NCAP;
			break;
		case "NCAP":
			if(a==1)return Wang.ECAP;
			else if(a==2)return Wang.SCAP;
			else if(a==3)return Wang.WCAP;
			break;
		case "SCAP":
			if(a==1)return Wang.WCAP;
			else if(a==2)return Wang.NCAP;
			else if(a==3)return Wang.ECAP;
			break;
		case "WTEE":
			if(a==1)return Wang.NTEE;
			else if(a==2)return Wang.ETEE;
			else if(a==3)return Wang.STEE;
			break;
		case "ETEE":
			if(a==1)return Wang.STEE;
			else if(a==2)return Wang.WTEE;
			else if(a==3)return Wang.NTEE;
			break;
		case "NTEE":
			if(a==1)return Wang.ETEE;
			else if(a==3)return Wang.WTEE;
			else if(a==2)return Wang.STEE;
			break;
		case "STEE":
			if(a==1)return Wang.WTEE;
			else if(a==2)return Wang.NTEE;
			else if(a==3)return Wang.ETEE;
			break;
		case "NWCORN":
			if(a==1)return Wang.NECORN;
			else if(a==2)return Wang.SECORN;
			else if(a==3)return Wang.SWCORN;
			break;
		case "SECORN":
			if(a==1)return Wang.SWCORN;
			else if(a==2)return Wang.NWCORN;
			else if(a==3)return Wang.NECORN;
			break;
		case "NECORN":
			if(a==1)return Wang.SECORN;
			else if(a==2)return Wang.SWCORN;
			else if(a==3)return Wang.NWCORN;
			break;
		case "SWCORN":
			if(a==1)return Wang.NWCORN;
			else if(a==2)return Wang.NECORN;
			else if(a==3)return Wang.SECORN;
			break;
		}
		return null;
	}
	
	public Wang shear(boolean bottom){
		switch(this.name()){
		case "LONER":
			return Wang.LONER;
		case "MIDDLE":
			if(bottom)return Wang.STEE;
			return Wang.NTEE;
		case "HORIZ":
			return Wang.HORIZ;
		case "VERT":
			if(bottom)return Wang.SCAP;
			return Wang.NCAP;
		case "WCAP":
			return Wang.WCAP;
		case "ECAP":
			return Wang.ECAP;
		case "NCAP":
			if(bottom)return Wang.LONER;
			return Wang.NCAP;
		case "SCAP":
			if(!bottom)return Wang.LONER;
			return Wang.SCAP;
		case "WTEE":
			if(bottom)return Wang.SWCORN;
			return Wang.NWCORN;
		case "ETEE":
			if(bottom)return Wang.SECORN;
			return Wang.NECORN;
		case "NTEE":
			if(bottom)return Wang.HORIZ;
			return Wang.NTEE;
		case "STEE":
			if(!bottom)return Wang.HORIZ;
			return Wang.STEE;
		case "NWCORN":
			if(bottom)return Wang.WCAP;
			return Wang.NWCORN;
		case "SECORN":
			if(!bottom)return Wang.ECAP;
			return Wang.SECORN;
		case "NECORN":
			if(bottom)return Wang.ECAP;
			return Wang.NECORN;
		case "SWCORN":
			if(!bottom)return Wang.WCAP;
			return Wang.SWCORN;
		}
		return null;
	}
}
