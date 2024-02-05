package pandemic.util;
/**
 * An enum class representing different console colors using ANSI escape codes.
 * The class defines a set of constants, each representing a different color.
 */
public enum Color {
	 /**
     * Represents blue color with code "\033[0;34m".
     */
	BLUE("\033[0;34m"),
	
    /**
     * Represents light blue color with code "\033[0;36m".
     */
	L_BLUE("\033[0;36m"),
	/**
     * Represents grey color with code "\033[0;90m".
     */
	GREY("\033[0;90m"),
	/**
     * Represents red color with code "\033[0;31m".
     */
	RED("\033[0;31m"),
	
    /**
     * Represents yellow color with code "\033[0;33m".
     */
	YELLOW("\033[0;33m"),
	/**
     * Represents black color with code "\033[0m".
     */
	BLACK("\033[0m"),
	 /**
     * Represents green color with code "\033[0;32m".
     */
	GREEN("\033[0;32m"),
	 /**
     * Represents the console color reset code "\033[0m".
     */
	CC("\033[0m");
	
	
	private final String colorCode;
	 /**
     * Constructs a new Color constant with the specified color code.
     * @param colorCode the ANSI escape code for the color
     */
	private Color(String colorCode) {
		this.colorCode = colorCode;
	}
	 /**
     * Returns the ANSI escape code for the color represented by this constant.
     * @return the ANSI escape code for the color
     */
	public String getColor() {
		return this.colorCode;
	}
}
