import java.lang.Math.*;
import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;

/** This a prototype ImageJ plug-in. */
public class Class_32h implements PlugIn {

	public void run(String arg) {
		int w = 400, h = 400;
		int r=0,g=0,b=0;
		int rotshift=16, gruenshift=8, blaushift=0;
		int farbwert=0;

		ImageProcessor ip = new ColorProcessor(w, h);
		ip.setColor(new Color(255, 255, 255));
		int[] pixels = (int[])ip.getPixels();
		int i=0, j=0, x, y, abstand=20;


        GenericDialog gd = new GenericDialog("Kreise");
        gd.addNumericField("Width of Bars:", abstand, 0);
        gd.showDialog();
        if (gd.wasCanceled()) return;
        abstand = (int) gd.getNextNumber();

		abstand=1;
		for (i = 2*w; i > 0; i--) {
			for (j = 0; j < abstand; j++) {
				ip.drawOval(-400, -400, i-abstand-j, i-abstand-j);
			}
			i-=abstand;
			abstand++;
		}

		new ImagePlus("32h", ip).show();
	 }

}

