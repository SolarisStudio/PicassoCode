package org.editor.events;

import java.awt.event.ActionEvent;
import java.util.List;
import org.editor.AccessFrame;
import org.editor.CanvasFrame;
import org.editor.EditorWindow;
import org.piccode.ast.Ast;
import org.piccode.backend.Compiler;
import org.piccode.piccodescript.ErrorAsciiKind;
import org.piccode.rt.PiccodeException;

/**
 *
 * @author hexaredecimal
 */
public class AccessEvents {

	public static void compileAndRender(ActionEvent e) {
		var ed = EditorWindow.getSelectedEditor();
		if (ed == null) {
			return;
		}

		if (ed.file == null) {
			EditorWindow.current_file.setText("Cannot compiler unsaved code");
			return;
		}

		var file = ed.file.toString();
		var code = ed.textArea.getText();
		CanvasFrame.the().compile(() -> Compiler.compile(file, code));
		AccessFrame.writeSuccess("Compilation started: ");
	}

	public static void compile(ActionEvent e) {
		var ed = EditorWindow.getSelectedEditor();
		if (ed == null) {
			return;
		}

		if (ed.file == null) {
			EditorWindow.current_file.setText("Cannot compiler unsaved code");
			return;
		}

		var code = ed.textArea.getText();
		AccessFrame.writeSuccess("Compilation started: ");
		Compiler.out = AccessFrame.AccessFrameOutputStream.out;
	}

}
