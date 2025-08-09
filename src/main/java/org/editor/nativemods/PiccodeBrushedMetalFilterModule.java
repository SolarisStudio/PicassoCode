package org.editor.nativemods;

import com.jhlabs.image.BrushedMetalFilter;
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
public class PiccodeBrushedMetalFilterModule {

	public static void addFunctions() {
		NativeFunctionFactory.create("brush_metal_new", List.of(), (args, namedArgs, frame) -> {
			var bmFilter = new BrushedMetalFilter();
			return new PiccodeReference(bmFilter);
		}, null);

		NativeFunctionFactory.create("brush_metal_set_rad", List.of("filter", "rad"), (args, namedArgs, frame) -> {
			var _filter = namedArgs.get("filter");
			var rad = namedArgs.get("rad");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _filter, Type.REFERENCE);
			PiccodeValue.verifyType(caller, rad, Type.NUMBER);

			var obj = (PiccodeReference) _filter;
			var _filter_object = obj.deref();
			if (!(_filter_object instanceof BrushedMetalFilter)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Filter is not a correct object.");
			}

			var filter = (BrushedMetalFilter) _filter_object;
			var radius = (int) (double) ((PiccodeNumber) rad).raw();

			filter.setRadius(radius);
			
			return new PiccodeReference(_filter_object);
		}, null);

		NativeFunctionFactory.create("brush_metal_set_amount", List.of("filter", "amount"), (args, namedArgs, frame) -> {
			var _filter = namedArgs.get("filter");
			var amount = namedArgs.get("amount");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _filter, Type.REFERENCE);
			PiccodeValue.verifyType(caller, amount, Type.NUMBER);

			var obj = (PiccodeReference) _filter;
			var _filter_object = obj.deref();
			if (!(_filter_object instanceof BrushedMetalFilter)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Filter is not a correct object.");
			}

			var filter = (BrushedMetalFilter) _filter_object;
			var _amount = (int) (double) ((PiccodeNumber) amount).raw();

			filter.setRadius(_amount);
			
			return new PiccodeReference(_filter_object);
		}, null);
	
		NativeFunctionFactory.create("brush_metal_set_shine", List.of("filter", "shine"), (args, namedArgs, frame) -> {
			var _filter = namedArgs.get("filter");
			var shine = namedArgs.get("shine");

			var ctx = frame == null
							? Context.top
							: Context.getContextAt(frame);
			var caller = ctx.getTopFrame().caller;

			PiccodeValue.verifyType(caller, _filter, Type.REFERENCE);
			PiccodeValue.verifyType(caller, shine, Type.NUMBER);

			var obj = (PiccodeReference) _filter;
			var _filter_object = obj.deref();
			if (!(_filter_object instanceof BrushedMetalFilter)) {
				throw new PiccodeException(caller.file, caller.line, caller.column, "Filter is not a correct object.");
			}

			var filter = (BrushedMetalFilter) _filter_object;
			var _shine = (int) (double) ((PiccodeNumber) shine).raw();

			filter.setRadius(_shine);
			
			return new PiccodeReference(_filter_object);
		}, null);
	
	}

}
