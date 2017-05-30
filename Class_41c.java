import java.lang.Math.*;
import java.awt.*;
import ij.*;
import ij.gui.*;
import ij.process.*;
import ij.plugin.PlugIn;
import ij.process.ImageConverter;

public class Class_41c implements PlugIn {
	public int breite;
	public static ImagePlus imp;

	public void run(String arg) {
		if (!runDialog()) return;

		int w = imp.getDimensions()[0];
		int h = imp.getDimensions()[1];
		int r = breite;

		// RGB
		ImageConverter ic = new ImageConverter(imp);
		ic.convertToRGB();

		// Viertelkreise
		Overlay o = new Overlay();
		o.setFillColor(Color.GREEN);
		Roi kreis;

		for (int i=0; r<w; i++) { // breite
			kreis = new OvalRoi(-r, h-r, r*2, r*2);
			kreis.setStrokeColor(Color.green);
			kreis.setStrokeWidth(1);
			o.add(kreis);
			r=breite*i;
		}

		imp.setOverlay(o);
		imp.updateAndDraw();
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
	    gd.addNumericField("Abstand:", 10, 0);
		gd.showDialog();

	    if (gd.wasCanceled())
	        return false;
	    else {
	        imp = WindowManager.getImage(windowList[gd.getNextChoiceIndex()]);
			breite = (int) gd.getNextNumber();
	        return true;
	    }
	}

}
