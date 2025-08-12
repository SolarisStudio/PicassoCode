package org.editor.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.piccode.piccodeplugin.PiccodePluginInterface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import org.editor.CodeEditor;

/**
 *
 * @author hexaredecimal
 */
public class ThemeManager {

	private static List<CodeEditor> editors = new ArrayList();
	private static List<PiccodePluginInterface> plugins = new ArrayList();
	public static Color RENDER_BG = Color.WHITE;
	public static Color RENDER_FG = Color.BLACK;
	public static Color RENDER_TXT2 = Color.BLUE;
	public static Color RENDER_GRID = Color.GRAY;

	public static void registerEditor(CodeEditor editor) {
		editors.add(editor);
	}

	public static void registerPlugin(PiccodePluginInterface plugin) {
		plugins.add(plugin);
	}

	public static void updateThemes(boolean dark) {
		editors.forEach(editor -> editor.setThemeMode(dark));
		plugins.forEach(plugin -> plugin.setThemeMode(dark));
		setFlatLaf(dark);

		if (dark) {
			RENDER_BG = new Color(18, 18, 18);
			RENDER_FG = Color.WHITE;
			RENDER_TXT2 = Color.GREEN;
			RENDER_GRID = new Color(18 * 5, 18 * 5, 18 * 5);
		} else {
			RENDER_BG = Color.WHITE;
			RENDER_FG = Color.BLACK;
			RENDER_TXT2 = Color.BLUE;
			RENDER_GRID = new Color(230, 230, 230);
		}
	}

	public static void setFlatLaf(boolean dark) {
		try {
			if (dark) {
				FlatDarkLaf.setup();
				FlatDarkLaf.updateUI();
			} else {
				FlatLightLaf.setup();
				FlatLightLaf.updateUI();
			}
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}
	}
}
