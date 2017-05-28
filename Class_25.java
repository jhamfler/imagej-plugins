import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import java.util.ArrayList;
import ij.plugin.filter.PlugInFilter;

public class Class_25 implements PlugInFilter {

	static int nFrames = 2;
	static int choice;
	static String[] choices = new String[]{"OR", "AND", "SUBTRACT", "DIFFERENCE"};
	static int [] blitterchoice = {Blitter.OR, Blitter.AND, Blitter.SUBTRACT, Blitter.DIFFERENCE};

	ImagePlus fgIm;
	ImagePlus bgIm;

public int setup(String arg, ImagePlus imp) {
	return DOES_8G;
}

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
	gd.addChoice("Operation:", choices, choices[0]);
	//gd.addNumericField("Frames:", nFrames, 0);
	gd.showDialog();
	if (gd.wasCanceled())
		return false;
	else {
		int img1Index = gd.getNextChoiceIndex();
		fgIm = WindowManager.getImage(windowList[img1Index]);
		int img2Index = gd.getNextChoiceIndex();
		bgIm = WindowManager.getImage(windowList[img2Index]);
		choice = gd.getNextChoiceIndex();

		return true;
	}
}

    public void run(ImageProcessor ip) {
	while (runDialog()) {
		int w,h;
		w = (bgIm.getWidth() > fgIm.getWidth()) ? bgIm.getWidth() : fgIm.getWidth();
		h = (bgIm.getHeight() > fgIm.getHeight()) ? bgIm.getHeight() : fgIm.getHeight();

		ImageProcessor fgIp = fgIm.getProcessor().convertToByte(false);
		ImageProcessor bgIp = bgIm.getProcessor().convertToByte(false);

		// Farbbild
		ImageProcessor cip = new ColorProcessor(w, h);
        int[] pixels = (int[])cip.getPixels();

		// OR - Bild
		ImagePlus imgor = NewImage.createByteImage("OR",w,h,1,1);
        ByteBlitter blitter = new ByteBlitter((ByteProcessor)imgor.getProcessor());
        blitter.copyBits(fgIp,0,0,Blitter.ADD);
		blitter.copyBits(bgIm.getProcessor(),0,0,Blitter.OR);

		// 1. Bild
		ImagePlus movie = NewImage.createByteImage("Verknüpfung",w,h,1,1);
		ByteBlitter blitter1 = new ByteBlitter((ByteProcessor)movie.getProcessor());
        blitter1.copyBits(fgIp,0,0,Blitter.ADD);

		// 2. Bild mit 1. verarbeiten
		blitter1.copyBits(bgIm.getProcessor(),0,0,blitterchoice[choice]);

		// Farbbild zusammenbauen
		int rotshift=16, gruenshift=8, blaushift=0;
		int farbwert=255, resultatfarbwert=255, i=0;
		for (int y=0; y<h; y++) {
            for (int x=0; x<w; x++) {
				resultatfarbwert=movie.getProcessor().get(x,y);
				farbwert		=imgor.getProcessor().get(x,y);
				if (resultatfarbwert != 0) {
					pixels[i++] = resultatfarbwert << gruenshift;
				} else	if (farbwert != 0) {
                	pixels[i++] = (farbwert << rotshift) | (farbwert << gruenshift) | (farbwert << blaushift);
				} else {
					i++;
				}
            }
		}

		movie.show();
		new ImagePlus("25", cip).show();
	}
    }
}

