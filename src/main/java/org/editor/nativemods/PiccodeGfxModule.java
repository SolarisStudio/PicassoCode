package org.editor.nativemods;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import org.editor.CanvasFrame;
import org.piccode.rt.Context;
import org.piccode.rt.PiccodeException;
import org.piccode.rt.PiccodeNumber;
import org.piccode.rt.PiccodeObject;
import org.piccode.rt.PiccodeReference;
import org.piccode.rt.PiccodeString;
import org.piccode.rt.PiccodeUnit;
import org.piccode.rt.PiccodeValue;
import org.piccode.rt.PiccodeValue.Type;
import org.piccode.rt.modules.NativeFunctionFactory;

/**
 *
 * @author hexaredecimal
 */
public class PiccodeGfxModule {

	public static void addFunctions() {
		NativeFunctionFactory.create("get_gfx", List.of(), (args, namedArgs, frame) -> {
			var gfx = CanvasFrame.gfx;
			return new PiccodeReference(gfx);
		}, null);

		NativeFunctionFactory.create("gfx_from_rect", List.of("ctx", "x", "y", "w", "h"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var x = namedArgs.get("x");
			var y = namedArgs.get("y");
			var w = namedArgs.get("w");
			var h = namedArgs.get("h");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			PiccodeValue.verifyType(caller, x, Type.NUMBER);
			PiccodeValue.verifyType(caller, y, Type.NUMBER);
			PiccodeValue.verifyType(caller, w, Type.NUMBER);
			PiccodeValue.verifyType(caller, h, Type.NUMBER);

			var obj = (PiccodeReference) _ctx;
			var _gfx = obj.deref();
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}

			var gfx = (Graphics2D) _gfx;
			var _x = (int) (double) ((PiccodeNumber) x).raw();
			var _y = (int) (double) ((PiccodeNumber) y).raw();
			var _w = (int) (double) ((PiccodeNumber) w).raw();
			var _h = (int) (double) ((PiccodeNumber) h).raw();

			var gfx2 = (Graphics2D) gfx.create(_x, _y, _w, _h);
			return new PiccodeReference(gfx2);
		}, null);
		NativeFunctionFactory.create("gfx_from", List.of("ctx"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			var obj = (PiccodeReference) _ctx;
			var _gfx = obj.deref();
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}

			var gfx = (Graphics2D) _gfx;

			var gfx2 = (Graphics2D) gfx.create();
			return new PiccodeReference(gfx2);
		}, null);
		
		NativeFunctionFactory.create("gfx_drop", List.of("ctx"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			var obj = (PiccodeReference) _ctx;
			var _gfx = obj.deref();
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}

			var gfx = (Graphics2D) _gfx;
			gfx.dispose();
			return new PiccodeUnit();
		}, null);
	}

}
