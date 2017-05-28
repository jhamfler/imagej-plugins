import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;

/** This a prototype ImageJ plug-in. */
public class Class_32d implements PlugIn {

	public void run(String arg) {
		int w = 400, h = 400;
		int r=0,g=0,b=0;
		int rotshift=16, gruenshift=8, blaushift=0;
		int farbwert=0;

		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[])ip.getPixels();
		int i=0, j=0, x, y, streifenanzahl=4;

		double breite=w/streifenanzahl;
		double inkrement=255/breite;

		for (y=0; y<h; y++) {
			for (x=0; x<w; x++) {
				pixels[i++]=(farbwert << rotshift) | (farbwert << gruenshift) | (farbwert << blaushift);
				farbwert=((int)(x*inkrement))%256;
			}
			farbwert=0;
		}

		new ImagePlus("32d", ip).show();
	 }

}

