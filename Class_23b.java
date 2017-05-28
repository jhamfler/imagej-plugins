import ij.*;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import java.awt.*;

public class Class_23b implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL+DOES_STACKS+SUPPORTS_MASKING;
    }

    public void run(ImageProcessor ip) {
        // 2° nach rechts drehen
        //rad: 0,0349066
		ip.rotate(2.0);
    }

}

