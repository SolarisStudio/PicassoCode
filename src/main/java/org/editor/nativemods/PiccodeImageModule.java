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
public class PiccodeImageModule {

	public static void addFunctions() {

		NativeFunctionFactory.create("image_new", List.of("w", "h"), (args, namedArgs, frame) -> {
			var w = namedArgs.get("w");
			var h = namedArgs.get("h");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, w, Type.NUMBER);
			PiccodeValue.verifyType(caller, h, Type.NUMBER);

			var _w = (int) (double) ((PiccodeNumber) w).raw();
			var _h = (int) (double) ((PiccodeNumber) h).raw();

			var image = new BufferedImage(_w, _h, BufferedImage.TYPE_INT_ARGB);
			return new PiccodeReference(image);
		}, null);
		
		NativeFunctionFactory.create("image_new_typed", List.of("w", "h", "type"), (args, namedArgs, frame) -> {
			var w = namedArgs.get("w");
			var h = namedArgs.get("h");
			var type = namedArgs.get("type");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, w, Type.NUMBER);
			PiccodeValue.verifyType(caller, h, Type.NUMBER);
			PiccodeValue.verifyType(caller, type, Type.NUMBER);

			var _w = (int) (double) ((PiccodeNumber) w).raw();
			var _h = (int) (double) ((PiccodeNumber) h).raw();
			var _type = (int) (double) ((PiccodeNumber) type).raw();

			var image = new BufferedImage(_w, _h, _type);
			return new PiccodeReference(image);
		}, null);
	
		NativeFunctionFactory.create("image_get_context", List.of("img"), (args, namedArgs, frame) -> {
			var img = namedArgs.get("img");

			var ctx = frame == null ? 
					Context.top
					: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;
			
			PiccodeValue.verifyType(caller, img, Type.REFERENCE);

			var _buffered_image = ((PiccodeReference)img).deref();

			if (!(_buffered_image instanceof BufferedImage)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Expected a buffer image. Found " + _buffered_image);
			}

			var bufferedmage = (BufferedImage) _buffered_image;
			var gfx = bufferedmage.createGraphics();
			return new PiccodeReference(gfx);
		}, null);
	
	}

}
