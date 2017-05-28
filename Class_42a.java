import ij.plugin.filter.PlugInFilter;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.plugin.filter.MaximumFinder;
import ij.plugin.filter.EDM;
import ij.*;
import ij.gui.GenericDialog;
import ij.gui.DialogListener;
import ij.process.*;
import java.awt.*;
import ij.plugin.filter.ParticleAnalyzer;
import ij.measure.ResultsTable;
import ij.plugin.frame.RoiManager;
import ij.measure.Measurements;


/** This ImageJ plugin does Watershed segmentation of the EDM, similar to
  * Process>Binary>Watershed but with adjustable sensitivity.
  * The ImageJ Process>Binary>Watershed algorithm has a tolerance of 0.5.
  *
  * 2012-May-08 Michael Schmid
  */

public class Class_42a implements ExtendedPlugInFilter, DialogListener {
    private final static int FLAGS = DOES_8G | PARALLELIZE_STACKS;
    private static double toleranceS = 1.;
    private double tolerance = 1.;
    private int nPasses = 30;
    private int pass = 0;
    private boolean background255;
    private boolean interrupted;        /*whether watershed segmentation has been interrupted by the user */
    private MaximumFinder maxFinder = new MaximumFinder();        /*we use only one MaximumFinder (nicer progress bar)*/
	private ImagePlus thisimp;
    int width, height, xmax, ymax;

    /** Setup of the PlugInFilter. Returns the flags specifying the capabilities and needs
     * of the filter.
     *
     * @param arg	Defines type of filter operation
     * @param imp	The ImagePlus to be processed
     * @return		Flags specifying further action of the PlugInFilterRunner
     */
    public int setup(String arg, ImagePlus imp) {
        if (imp!=null && !imp.getProcessor().isBinary()) {
            IJ.error("Binary Image required");
            return DONE;
        }
        return FLAGS;
    }

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) {
		thisimp = imp;
        boolean invertedLut = imp.isInvertedLut();
		Prefs.blackBackground = true;
        background255 = (invertedLut && Prefs.blackBackground) ||
                (!invertedLut && !Prefs.blackBackground);
        width = imp.getWidth();
        height = imp.getHeight();
        xmax = width - 1;
        ymax = height - 1;
        return IJ.setupDialog(imp, FLAGS);
    }

    public boolean dialogItemChanged(GenericDialog gd, AWTEvent e) {
        tolerance = gd.getNextNumber();
        if (gd.invalidNumber() || tolerance<=0)
            return false;
        interrupted = false;
        return true;
    }

    public void run(ImageProcessor ip) {
		int erosionen = 0, muenzen = 0;

		// WATERSHED
        if (interrupted) return;
        int backgroundValue = background255 ? (byte)255 : 0;
        FloatProcessor floatEdm = new EDM().makeFloatEDM(ip, backgroundValue, false);
        ByteProcessor newIp = maxFinder.findMaxima(floatEdm, tolerance, ImageProcessor.NO_THRESHOLD, MaximumFinder.SEGMENTED, false, true);
        if (newIp == null) {  //segmentation cancelled by user?
            interrupted = true;
            return;
        }
        drawSegmentationLines(ip, backgroundValue, newIp);
        ip.setBinaryThreshold();

		// loop until no coin is left
		do {

		// COUNT PARTICLES
		ImagePlus plus = IJ.getImage();
//		ResultsTable rt = ResultsTable.getResultsTable();
//		if(rt==null)
		ResultsTable rt = new ResultsTable();
		RoiManager manager = RoiManager.getInstance();
		//if(manager==null) manager = new RoiManager();

		ParticleAnalyzer analyser = new ParticleAnalyzer(0, Measurements.AREA, rt, 1, Double.MAX_VALUE, 0, 1);

		analyser.analyze(plus);
		//rt.updateResults();
		//rt.show("Results");
		muenzen = rt.size();


		// ERODE
		// pixels with an EDM value < radius will be set to background
/*		double radius = 1;
		boolean fromEdge = true;
        int width = ip.getWidth();
        int height = ip.getHeight();
        byte[] bPixels = (byte[])ip.getPixels();
        float[] fPixels = (float[])floatEdm.getPixels();
        Rectangle roiRect = ip.getRoi();
        for (int y=roiRect.y; y<roiRect.y+roiRect.height; y++)
            for (int x=roiRect.x, p=x+y*width; x<roiRect.x+roiRect.width; x++,p++)
                if (fPixels[p] <= radius)
                    bPixels[p] = (byte)backgroundValue;
*/
		ip.dilate(); // opposite of erode because of inverted LUT
		erosionen++;
		System.out.println("Erosionen: " + erosionen + "\tMünzen: " + muenzen);
		plus.repaintWindow();
		} while (muenzen > 0);
    }

    /** Draw the segmentation lines in the original image.
     *  Note that segmentation has eliminated particles with radius less than
     *  the tolerance.  Leave these small particles untouched. */
    private void drawSegmentationLines(ImageProcessor ip, int backgroundValue, ByteProcessor segmentedIp) {
        byte[] origPixels = (byte[])ip.getPixels();
        byte[] segmPixels = (byte[])segmentedIp.getPixels();
        for (int p=0; p<origPixels.length; p++)
            if (segmPixels[p] == 0) origPixels[p] = (byte)backgroundValue;
    }

    /* whether a neighbor of pixel x, y in direction 'd' is inside the image */
    private boolean isWithin(int x, int y, int direction) {
        switch(direction) {
            case 0: return (y>0);
            case 1: return (x<xmax && y>0);
            case 2: return (x<xmax);
            case 3: return (x<xmax && y<ymax);
            case 4: return (y<ymax);
            case 5: return (x>0 && y<ymax);
            case 6: return (x>0);
            case 7: return (x>0 && y>0);
        }
        return false;   /* to make the compiler happy :-) */
    }

    /** This method is called by ImageJ to set the number of calls to run(ip)
     *	corresponding to 100% of the progress bar. We transfer it to the
     *  MaximumFinder since it will need most of the processing time */
    public void setNPasses (int nPasses) {
        maxFinder.setNPasses(nPasses);
    }

}
