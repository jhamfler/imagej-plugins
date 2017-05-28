import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import java.awt.image.*;

public class Class_41d implements PlugIn {

	public static ImagePlus imp;

    public void run(String arg) {
        if (IJ.versionLessThan("1.46p"))
           return;

		if(!runDialog()) return;

        ImagePlus boats = (ImagePlus) imp.clone();

//        ImagePlus imp = IJ.openImage("http://imagej.nih.gov/ij/images/cardio.dcm.zip");
        ImageProcessor ip = imp.getProcessor();
        ip = ip.crop();
//        ip.setRoi(142, 132, 654, 616);
        ip.setRoi(0, 0, imp.getDimensions()[0], imp.getDimensions()[1]);
//        int width = ip.getWidth()/2;
//        int height = ip.getHeight()/2;
//        ip = ip.resize(width, height, true);
//        ip.setColor(Color.red);
//        ip.setFont(new Font("SansSerif",Font.PLAIN,28));
//        ip.drawString("Transparent\nImage\nOverlay", 0, 40);
        ImageRoi imageRoi = new ImageRoi(0, 0, ip);
        imageRoi.setZeroTransparent(true);

//        ImagePlus boats = IJ.openImage("http://imagej.nih.gov/ij/images/boats.gif");
        Overlay overlay = new Overlay(imageRoi);
        boats.setOverlay(overlay);
        boats.setRoi(imageRoi);
        boats.show();
		while (true)
        for (int a=0; a<=360; a++) {
           imageRoi.setAngle(a);
           boats.draw();
           IJ.wait(20);
        }
    }

	boolean runDialog() {
	    int[] windowList = WindowManager.getIDList();
	    if(windowList==null) {
        	IJ.noImage();
	        return false;
	    }

    	String[] windowTitles = new String[windowList.length];
    	for (int i = 0; i < windowList.length; i++) {
    	    ImagePlus imp1 = WindowManager.getImage(windowList[i]);
    	    if (imp != null)
    	        windowTitles[i] = imp1.getShortTitle();
    	    else
    	        windowTitles[i] = "untitled";
    	}

	    GenericDialog gd = new GenericDialog("41a");
	    gd.addChoice("Bild:", windowTitles, windowTitles[0]);
		gd.showDialog();

	    if (gd.wasCanceled())
	        return false;
	    else {
	        imp = WindowManager.getImage(windowList[gd.getNextChoiceIndex()]);
	        return true;
	    }
	}
}
