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
			var obj = Context.getObject(gfx.hashCode());
			if (obj == null) {
				Context.allocate(gfx);
				return makeObj(gfx);
			}
			return makeObj(gfx);
		}, null);

		NativeFunctionFactory.create("draw_line", List.of("ctx", "x1", "y1", "x2", "y2"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var _x1 = namedArgs.get("x1");
			var _y1 = namedArgs.get("y1");
			var _x2 = namedArgs.get("x2");
			var _y2 = namedArgs.get("y2");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, _ctx, Type.OBJECT);
			PiccodeValue.verifyType(caller, _x1, Type.NUMBER);
			PiccodeValue.verifyType(caller, _y1, Type.NUMBER);
			PiccodeValue.verifyType(caller, _x2, Type.NUMBER);
			PiccodeValue.verifyType(caller, _y2, Type.NUMBER);

			var obj = (PiccodeObject) _ctx;
			var map = obj.obj;
			if (!map.containsKey("hash")) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not an object");
			}
			
			var _hash = map.get("hash");
			PiccodeValue.verifyType(caller, _hash, Type.NUMBER);
			var hash = (int) (double) ((PiccodeNumber) _hash).raw();
			var _gfx = Context.getObject(hash);
			if (_gfx == null) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not allocated");
			}
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}

			var gfx = (Graphics2D) _gfx;
			var x1 = (int) (double) ((PiccodeNumber) _x1).raw();
			var y1 = (int) (double) ((PiccodeNumber) _y1).raw();
			var x2 = (int) (double) ((PiccodeNumber) _x2).raw();
			var y2 = (int) (double) ((PiccodeNumber) _y2).raw();

			gfx.setColor(Color.black);
			gfx.drawRect(x1, y1, x2, y2);
			return obj;
		}, null);
		
	}

	private static PiccodeValue makeObj(Graphics2D obj) {
		var _obj = new HashMap<String, PiccodeValue>();
		_obj.put("hash", new PiccodeNumber(obj.hashCode()));
		_obj.put("class", new PiccodeString(obj.getClass().getName()));
		return new PiccodeObject(_obj);
	}
}
