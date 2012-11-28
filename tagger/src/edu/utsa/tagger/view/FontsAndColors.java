package edu.utsa.tagger.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public abstract class FontsAndColors
{
	// prevent instantiation
	private FontsAndColors(){}
		
	public static final Color VERY_LIGHT_GREY = new Color(235, 235, 235);
	public static final Color GREY_DARK = new Color(100, 100, 100);
	public static final Color GREY_MEDIUM = new Color(205, 205, 205);
	public static final Color GREY_LIGHT = new Color(225, 225, 225);
	public static final Color BLUE_DARK = new Color(100, 140, 245);
	public static final Color BLUE_MEDIUM = new Color(205, 205, 255);
	public static final Color BLUE_LIGHT = new Color(225, 225, 255);
	
	public static final Color TAG_BG_NORMAL = new Color(0, 0, 0, 0);
	public static final Color TAG_BG_HOVER = VERY_LIGHT_GREY;
	public static final Color TAG_BG_SELECTED = BLUE_DARK;
	public static final Color TAG_FG_NORMAL = GREY_DARK;
	public static final Color TAG_FG_HOVER = GREY_DARK;
	public static final Color TAG_FG_SELECTED = Color.white;
	
	public static final Color EVENT_BG_NORMAL = new Color(0, 0, 0, 0);
	public static final Color EVENT_BG_HOVER = GREY_LIGHT;
	public static final Color EVENT_BG_SELECTED = BLUE_DARK;
	public static final Color EVENT_FG_NORMAL = GREY_DARK;
	public static final Color EVENT_FG_HOVER = GREY_DARK;
	public static final Color EVENT_FG_SELECTED = Color.white;

	public static Font segoe_ui = loadFont("/segoeui.ttf", "Segoe UI");
	public static Font segoe_ui_light = loadFont("/segoeuil.ttf", "Segoe UI Light");
	public static Font segoe_ui_symbol = loadFont("/seguisym.ttf", "Segoe UI Symbol");
	
	private static Font loadFont(String path, String name)
	{
		try
		{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream stream;

			stream = FontsAndColors.class.getResourceAsStream(path);
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, stream));
			stream.close();

			return new Font(name, Font.PLAIN, 14);
		}
		catch (Exception e)
		{
			System.out.println("Couldn't load font.");
			e.printStackTrace();
			return null;
		}
	}
}
