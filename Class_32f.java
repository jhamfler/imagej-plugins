import java.lang.Math.*;
import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;

/** This a prototype ImageJ plug-in. */
public class Class_32f implements PlugIn {

	public void run(String arg) {
		int w = 400, h = 400;
		int r=0,g=0,b=0;
		int rotshift=16, gruenshift=8, blaushift=0;
		int farbwert=0;

		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[])ip.getPixels();
		int i=0, j=0, x, y, breite=20;

		GenericDialog gd = new GenericDialog("Differenz");
		gd.addNumericField("Breite:", breite, 0);
		gd.showDialog();
		breite = (int) gd.getNextNumber();

		for (y=0; y<h; y++) {
			if (y%breite == 0) farbwert^=255;
			for (x=0; x<w; x++) {
				if (x%breite == 0) farbwert^=255;
				pixels[i++]=(farbwert << rotshift) | (farbwert << gruenshift) | (farbwert << blaushift);
			}
		}

		new ImagePlus("32f", ip).show();
	 }

}

