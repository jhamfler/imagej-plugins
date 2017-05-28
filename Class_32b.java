import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;

/** This a prototype ImageJ plug-in. */
public class Class_32b implements PlugIn {

	public void run(String arg) {
		int w = 400, h = 400;
		int r=0,g=0,b=0;
		int rotshift=16, gruenshift=8, blaushift=0;
		int farbwert=255;

		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[])ip.getPixels();
		int i = 0;

		for (int y = 0; y < h; y++) {
			if (y%20==0) farbwert^=255;
			for (int x = 0; x < w; x++) {
				pixels[i++] = (farbwert << rotshift) | (farbwert << gruenshift);
			}
		}
		new ImagePlus("32b", ip).show();
	 }

}

