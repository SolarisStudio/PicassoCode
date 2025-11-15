package org.editor.events;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import org.editor.CodeEditor;
import org.editor.EditorWindow;
import org.editor.dialogs.AboutDialog;
import org.editor.fs.FileFilter;
import org.fife.rsta.ui.GoToDialog;
import org.fife.ui.rsyntaxtextarea.FileLocation;

/**
 *
 * @author hexaredecimal
 */
public class MenuEvents {

	public static void gotoLineEvent(ActionEvent e) {
		var findDialog = EditorWindow.findDialog;
		var replaceDialog = EditorWindow.replaceDialog;
		if (findDialog.isVisible()) {
			findDialog.setVisible(false);
		}
		if (replaceDialog.isVisible()) {
			replaceDialog.setVisible(false);
		}
		GoToDialog dialog = new GoToDialog(EditorWindow.win);

		var ed = EditorWindow.getSelectedEditor();
		if (ed == null) {
			return;
		}
    var textArea = ed.textArea;
		dialog.setMaxLineNumberAllowed(textArea.getLineCount());
		dialog.setVisible(true);
		int line = dialog.getLineNumber();
		if (line > 0) {
			try {
				textArea.setCaretPosition(textArea.getLineStartOffset(line - 1));
			} catch (BadLocationException ble) { // Never happens
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				ble.printStackTrace();
			}
		}
	}

	static void replaceEvent(ActionEvent e) {
		var replaceDialog = EditorWindow.replaceDialog;
		var findDialog = EditorWindow.findDialog;
		if (findDialog.isVisible()) {
			findDialog.setVisible(false);
		}
		replaceDialog.setVisible(true);
	}

	static void findEvent(ActionEvent e) {
		var replaceDialog = EditorWindow.replaceDialog;
		var findDialog = EditorWindow.findDialog;
		if (replaceDialog.isVisible()) {
			replaceDialog.setVisible(false);
		}
		findDialog.setVisible(true);
	}

	public static void aboutDialog(ActionEvent e) {
		new AboutDialog(EditorWindow.win);
	}

	public static void closeTab(ActionEvent e) {
		EditorWindow.removeTab();
	}

	public static void closeAllTabs(ActionEvent e) {
		EditorWindow.removeAllTabs();
	}

	static void openFile(ActionEvent e) {
		var fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setFileFilter(FileFilter.mdFilter);
		fileChooser.setFileFilter(FileFilter.picsFilter);

		int status = fileChooser.showOpenDialog(EditorWindow.win);
		if (status != JFileChooser.APPROVE_OPTION) {
			return;
		}

		var fp = fileChooser.getSelectedFile();
		var path = fp.toPath();
		EditorWindow.addTab(path, null);
	}

	static void saveFile(ActionEvent e) {
		if (EditorWindow.tabsCount() == 0) {
			return;
		}
		var ed = EditorWindow.getSelectedEditor();
		if (ed == null) {
			return;
		}
		ed.saveFile();
	}

	static void saveFileAs(ActionEvent e) {
		if (EditorWindow.tabsCount() == 1) {
			return;
		}
		var ed = EditorWindow.getSelectedEditor();
		if (ed == null) {
			return;
		}
		ed.saveFileAs();
	}

	static void saveAllFiles(ActionEvent e) {
		EditorWindow.saveAll();
	}

	static void closeFile(ActionEvent e) {
		closeTab(e);
	}

	static void quit(ActionEvent e) {
		closeAllTabs(e);
		System.exit(0);
	}

	static void newFile(ActionEvent e) {
		// TODO: Use a file creator dialog in the future
		EditorWindow.addTab(e);
	}
}
