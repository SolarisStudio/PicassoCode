package org.editor.nativemods;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.List;
import org.piccode.rt.Context;
import org.piccode.rt.PiccodeException;
import org.piccode.rt.PiccodeReference;
import org.piccode.rt.PiccodeValue;
import org.piccode.rt.PiccodeValue.Type;
import org.piccode.rt.modules.NativeFunctionFactory;

/**
 *
 * @author hexaredecimal
 */
public class PiccodeFilterModule {

	public static void addFunctions() {
		NativeFunctionFactory.create("filter_apply", List.of("filter", "image"), (args, namedArgs, frame) -> {
			var _filter = namedArgs.get("filter");
			var image = namedArgs.get("image");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _filter, Type.REFERENCE);
			PiccodeValue.verifyType(caller, image, Type.REFERENCE);

			var obj = (PiccodeReference) _filter;
			var img = (PiccodeReference) image;
			var _filter_object = obj.deref();
			var _buffered_image = img.deref();
			
			if (!(_filter_object instanceof BufferedImageOp)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Filter is not a correct object.");
			}

			if (!(_buffered_image instanceof BufferedImage)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Expected a buffer image. Found " + _buffered_image);
			}


			var filter = (BufferedImageOp) _filter_object;
			var _image = (BufferedImage) _buffered_image;

			var result = filter.filter(_image, null);
			
			return new PiccodeReference(result);
		}, null);
	
	
	}

}
