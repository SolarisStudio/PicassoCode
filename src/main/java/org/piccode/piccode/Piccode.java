package org.piccode.piccode;

import org.editor.AccessFrame;
import org.editor.EditorWindow;
import org.editor.nativemods.PiccodeGfxModule;
import org.piccode.backend.Compiler;
import org.piccode.piccodescript.ErrorAsciiKind;

/**
 *
 * @author hexaredecimal
 */
public class Piccode {

	public static void main(String[] args) {
		Compiler.exitOnError = false;
		Compiler.errorKind = ErrorAsciiKind.EMACS_COMP_STYLE;
		Compiler.out = AccessFrame.AccessFrameOutputStream.out;
		initializeNativeAppModules();
		EditorWindow.the();
	}

	private static void initializeNativeAppModules() {
		Compiler.addNativeFunctions(PiccodeGfxModule::addFunctions);
	}
}
