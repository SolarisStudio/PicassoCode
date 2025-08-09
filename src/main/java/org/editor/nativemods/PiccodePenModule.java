package org.editor.nativemods;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
public class PiccodePenModule {

	public static void addFunctions() {

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
			
			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			PiccodeValue.verifyType(caller, _x1, Type.NUMBER);
			PiccodeValue.verifyType(caller, _y1, Type.NUMBER);
			PiccodeValue.verifyType(caller, _x2, Type.NUMBER);
			PiccodeValue.verifyType(caller, _y2, Type.NUMBER);

			var obj = (PiccodeReference) _ctx;
			var _gfx = obj.deref();
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}

			var gfx = (Graphics2D) _gfx;
			var x1 = (int) (double) ((PiccodeNumber) _x1).raw();
			var y1 = (int) (double) ((PiccodeNumber) _y1).raw();
			var x2 = (int) (double) ((PiccodeNumber) _x2).raw();
			var y2 = (int) (double) ((PiccodeNumber) _y2).raw();

			gfx.drawLine(x1, y1, x2, y2);
			return obj;
		}, null);
		
		NativeFunctionFactory.create("draw_rect", List.of("ctx", "x", "y", "w", "h"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var x = namedArgs.get("x");
			var y = namedArgs.get("y");
			var w = namedArgs.get("w");
			var h = namedArgs.get("h");

			var ctx = frame == null ? 
					Context.top
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

			gfx.drawRect(_x, _y, _w, _h);
			return obj;
		}, null);
		
		NativeFunctionFactory.create("draw_round_rect", List.of("ctx", "x", "y", "w", "h", "aw", "ah"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var x = namedArgs.get("x");
			var y = namedArgs.get("y");
			var w = namedArgs.get("w");
			var h = namedArgs.get("h");
			var aw = namedArgs.get("aw");
			var ah = namedArgs.get("ah");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			PiccodeValue.verifyType(caller, x, Type.NUMBER);
			PiccodeValue.verifyType(caller, y, Type.NUMBER);
			PiccodeValue.verifyType(caller, w, Type.NUMBER);
			PiccodeValue.verifyType(caller, h, Type.NUMBER);
			PiccodeValue.verifyType(caller, aw, Type.NUMBER);
			PiccodeValue.verifyType(caller, ah, Type.NUMBER);

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
			var _aw = (int) (double) ((PiccodeNumber) aw).raw();
			var _ah = (int) (double) ((PiccodeNumber) ah).raw();

			gfx.drawRoundRect(_x, _y, _w, _h, _aw, _ah);
			return obj;
		}, null);
		
		NativeFunctionFactory.create("draw_oval", List.of("ctx", "x", "y", "w", "h"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var x = namedArgs.get("x");
			var y = namedArgs.get("y");
			var w = namedArgs.get("w");
			var h = namedArgs.get("h");

			var ctx = frame == null ? 
					Context.top
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

			gfx.drawOval(_x, _y, _w, _h);
			return obj;
		}, null);
		
		NativeFunctionFactory.create("draw_image", List.of("ctx", "img", "x", "y"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var _img = namedArgs.get("img");
			var x = namedArgs.get("x");
			var y = namedArgs.get("y");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			PiccodeValue.verifyType(caller, _img, Type.REFERENCE);
			PiccodeValue.verifyType(caller, x, Type.NUMBER);
			PiccodeValue.verifyType(caller, y, Type.NUMBER);

			var obj = (PiccodeReference) _ctx;
			var imgObj = (PiccodeReference) _img;
			var _gfx = obj.deref();
			var _image = imgObj.deref();
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}
			if (!(_image instanceof BufferedImage)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Image in not a correct object. Expected a BufferedImage but found" + _image);
			}

			var gfx = (Graphics2D) _gfx;
			var img = (BufferedImage) _image;
			var _x = (int) (double) ((PiccodeNumber) x).raw();
			var _y = (int) (double) ((PiccodeNumber) y).raw();

			gfx.drawImage(img, _x, _y, null);
			return obj;
		}, null);
		NativeFunctionFactory.create("draw_text", List.of("ctx", "text", "x", "y"), (args, namedArgs, frame) -> {
			var _ctx = namedArgs.get("ctx");
			var _text = namedArgs.get("text");
			var x = namedArgs.get("x");
			var y = namedArgs.get("y");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, _ctx, Type.REFERENCE);
			PiccodeValue.verifyType(caller, _text, Type.STRING);
			PiccodeValue.verifyType(caller, x, Type.NUMBER);
			PiccodeValue.verifyType(caller, y, Type.NUMBER);

			var obj = (PiccodeReference) _ctx;
			var _gfx = obj.deref();
			if (!(_gfx instanceof Graphics2D)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Context is not a correct object. Expected Graphics2D");
			}
			var gfx = (Graphics2D) _gfx;
			var _x = (int) (double) ((PiccodeNumber) x).raw();
			var _y = (int) (double) ((PiccodeNumber) y).raw();

			gfx.drawString(_text.toString(), _x, _y);
			return obj;
		}, null);
	}

}
