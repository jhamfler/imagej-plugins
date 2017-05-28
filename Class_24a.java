import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import java.util.ArrayList;
import ij.plugin.filter.PlugInFilter;

public class Class_24a implements PlugInFilter {

	static int nFrames = 2;
	ImagePlus fgIm;
	ImagePlus bgIm;

public int setup(String arg, ImagePlus imp) {
return DOES_8G;}

boolean runDialog() {
	// get list of open images
	int[] windowList = WindowManager.getIDList();
	if(windowList==null){
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

	GenericDialog gd = new GenericDialog("Differenz");
	gd.addChoice("1. Bild:", windowTitles, windowTitles[0]);
	gd.addChoice("2. Bild:", windowTitles, windowTitles.length>1 ? windowTitles[1]:windowTitles[0]);
	//gd.addNumericField("Frames:", nFrames, 0);
	gd.showDialog();
	if (gd.wasCanceled())
		return false;
	else {
		int img1Index = gd.getNextChoiceIndex();
		fgIm = WindowManager.getImage(windowList[img1Index]);
		int img2Index = gd.getNextChoiceIndex();
		bgIm = WindowManager.getImage(windowList[img2Index]);
		return true;
	}
}

    public void run(ImageProcessor ip) {
	if (runDialog()) {
		int w,h;
		w = (bgIm.getWidth() > fgIm.getWidth()) ? bgIm.getWidth() : fgIm.getWidth();
		h = (bgIm.getHeight() > fgIm.getHeight()) ? bgIm.getHeight() : fgIm.getHeight();

		// prepare foreground image
		ImageProcessor fgIp = fgIm.getProcessor().convertToByte(false);
		ImageProcessor bgIp = bgIm.getProcessor().convertToByte(false);

		// create image
		ImagePlus movie = NewImage.createByteImage("Differenz",w,h,1,1);
		ByteBlitter blitter = new ByteBlitter((ByteProcessor)movie.getProcessor());
        blitter.copyBits(fgIp,0,0,Blitter.ADD);
		blitter.copyBits(bgIm.getProcessor(),0,0,Blitter.SUBTRACT);

		// display movie (image stack)
		movie.show();
	}
    }
}

