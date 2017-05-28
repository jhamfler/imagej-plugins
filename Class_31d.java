import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;
import ij.gui.*;


public class Class_31d implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {
		if (IJ.versionLessThan("1.37j"))
			return DONE;
		else
			return DOES_ALL+DOES_STACKS+SUPPORTS_MASKING;
	}

	public void run(ImageProcessor ip) {
		int anzahl=1;
		GenericDialog gd = new GenericDialog("Differenz");
		gd.addNumericField("Grauwerte:", anzahl, 1);
		gd.showDialog();
		if (gd.wasCanceled()) return;
		anzahl = (int) gd.getNextNumber();

		Rectangle r = ip.getRoi();
		int wert, teiler;
		for (int y=r.y; y<(r.y+r.height); y++)
			for (int x=r.x; x<(r.x+r.width); x++) {
				wert = ip.get(x,y);
				teiler = 255/anzahl;
				if (wert < teiler*anzahl)
					ip.set(x, y,  wert/teiler   *teiler);
				else
					ip.set(x, y, (wert/teiler-1)*teiler);
			}
	}

}

