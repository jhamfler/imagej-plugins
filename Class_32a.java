import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;

/** This a prototype ImageJ plug-in. */
public class Class_32a implements PlugIn {

	public void run(String arg) {
		int w = 400, h = 400;
		int r=0,g=0,b=0;
		int rots = 16, gruens = 8, blaus = 0;

		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[])ip.getPixels();
		int i = 0;

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				pixels[i++] = (255 << rots) | (255 << gruens) ;
			}
		}
		new ImagePlus("32a", ip).show();
	 }

}

