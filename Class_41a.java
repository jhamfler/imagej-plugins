import java.lang.Math.*;
import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;
import ij.process.ImageConverter;

public class Class_41a implements PlugIn {
	public static int [] p = new int [8];
	public static ImagePlus imp;

	public void run(String arg) {
		int w = 400, h = 400;
		int r=0,g=0,b=0;
		int rotshift=16, gruenshift=8, blaushift=0;
		int farbwert=0;
/*
		ImageProcessor ip = new ColorProcessor(w, h);
		ip.setColor(new Color(255, 255, 255));
		int[] pixels = (int[])ip.getPixels();
		int i=0, j=0, x, y, abstand=20;
*/
		if (!runDialog()) return;

		// RGB
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToRGB();

		// gelbe Punkte und Text
		ImageProcessor ip = imp.getProcessor();
		Overlay o = new Overlay();
		o.drawNames(true);
		o.drawLabels(true);
		o.setFillColor(Color.YELLOW);
//		ip.setColor(Color.YELLOW);

		for (int i=0; i<8; i=i+2) {
//			ip.drawOval(p[i], p[i+1], 2, 2);
			o.add(new Roi(p[i], p[i+1], 2, 2), "" + i/2);
			o.add(new TextRoi(p[i], p[i+1], "" + i/2));
//			imp.setOverlay(new Roi(p[i], p[i+1], 2, 2), Color.YELLOW, 2, Color.YELLOW);
		}
		imp.setOverlay(o);
		imp.updateAndDraw();
//		new ImagePlus("41a", ip).show();
	 }

	boolean runDialog() {
	    int[] windowList = WindowManager.getIDList();
	    if(windowList==null) {
        	IJ.noImage();
	        return false;
	    }

    	String[] windowTitles = new String[windowList.length];
    	for (int i = 0; i < windowList.length; i++) {
    	    ImagePlus imp = WindowManager.getImage(windowList[i]);
    	    if (imp != null)
    	        windowTitles[i] = imp.getShortTitle();
    	    else
    	        windowTitles[i] = "untitled";
    	}

	    GenericDialog gd = new GenericDialog("41a");
	    gd.addChoice("Bild:", windowTitles, windowTitles[0]);
	    gd.addNumericField("p1x", 20, 0);
	    gd.addNumericField("p1y", 30, 0);
	    gd.addNumericField("p2x", 70, 0);
	    gd.addNumericField("p2y", 30, 0);
	    gd.addNumericField("p3x", 70, 0);
	    gd.addNumericField("p3y", 100, 0);
	    gd.addNumericField("p4x", 100, 0);
	    gd.addNumericField("p4y", 45, 0);
		gd.showDialog();

	    if (gd.wasCanceled())
	        return false;
	    else {
	        int img1Index = gd.getNextChoiceIndex();
	        imp = WindowManager.getImage(windowList[img1Index]);
			for (int i=0; i<8; i++) {
				p[i]=(int) gd.getNextNumber();
			}
	        return true;
	    }
	}

}
