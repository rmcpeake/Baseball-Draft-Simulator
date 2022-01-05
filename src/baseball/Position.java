package baseball;

public enum Position {
	CATCHER,
	FIRSTBASEMAN,
	SECONDBASEMAN,
	THIRDBASEMAN,
	SHORTSTOP,
	LEFTFIELDER,
	CENTERFIELDER,
	RIGHTFIELDER,
	PITCHER;
	
	public String getPositionName() {
		switch (this) {
		case CATCHER:
			return "catcher";
		case FIRSTBASEMAN:
			return "first baseman";
		case SECONDBASEMAN:
			return "second baseman";
		case THIRDBASEMAN:
			return "third baseman";
		case SHORTSTOP:
			return "short stop";
		case LEFTFIELDER:
			return "left fielder";
		case CENTERFIELDER:
			return "center fielder";
		case RIGHTFIELDER:
			return "right fielder";
		case PITCHER:
			 return "pitcher";
		default:
			return "error";
		}
	}
	
	public static Position Parse(String string) {
		switch (string) {
		case "C":
			return CATCHER;
		case "1B":
			return FIRSTBASEMAN;
		case "2B":
		return SECONDBASEMAN;
		case "3B":
			return THIRDBASEMAN;
		case "SS":
			return SHORTSTOP;
		case "LF":
			return LEFTFIELDER;
		case "CF":
			return CENTERFIELDER;
		case "RF":
			return RIGHTFIELDER;
		case "P":
			return PITCHER;
		default:
			return null;
		}
	}
}

