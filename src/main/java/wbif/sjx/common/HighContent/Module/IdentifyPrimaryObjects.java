package wbif.sjx.common.HighContent.Module;

import fiji.threshold.Auto_Threshold;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import ij.process.ImageProcessor;
import inra.ijpb.binary.conncomp.FloodFillComponentsLabeling3D;
import inra.ijpb.segment.Threshold;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.MathFunc.ArrayFunc;

import java.util.ArrayList;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class IdentifyPrimaryObjects implements Module {
    public static final String INPUT_IMAGE = "Input image";
    public static final String OUTPUT_OBJECT = "Output object";
    public static final String MEDIAN_FILTER_RADIUS = "Median filter radius";
    public static final String THRESHOLD_MULTIPLIER = "Threshold multiplier";

    public void execute(Workspace workspace) {
        ParameterCollection parameters = workspace.getParameters();

        ImageName targetImageName = (ImageName) parameters.getParameter(this,INPUT_IMAGE);
        HCObjectName outputObjectName = (HCObjectName) parameters.getParameter(this,OUTPUT_OBJECT);
        double medFiltR = (double) parameters.getParameter(this,MEDIAN_FILTER_RADIUS);
        double thrMult = (double) parameters.getParameter(this,THRESHOLD_MULTIPLIER);

        // Getting image stack
        ImagePlus ipl = workspace.getImages().get(targetImageName).getImage();

        // Applying smoothing filter and threshold
        ipl.setStack(Filters3D.filter(ipl.getImageStack(), Filters3D.MEDIAN, (float) medFiltR, (float) medFiltR, (float) medFiltR));
        Auto_Threshold auto_threshold = new Auto_Threshold();
        Object[] results1 = auto_threshold.exec(ipl,"Otsu",true,false,true,true,false,true);
        ipl = Threshold.threshold(ipl,(Integer) results1[0]*thrMult,Integer.MAX_VALUE);
        FloodFillComponentsLabeling3D ffcl3D = new FloodFillComponentsLabeling3D();
        ipl.setStack(ffcl3D.computeLabels(ipl.getImageStack()));

        // Need to get coordinates and convert to a HCObject
        ArrayList<Integer> IDs = new ArrayList<>();
        ArrayList<HCObject> objects = new ArrayList<>(); //Local ArrayList of objects
        workspace.addObjects(outputObjectName,objects);

        ImageProcessor ipr = ipl.getProcessor();

        int h = ipl.getHeight();
        int w = ipl.getWidth();
        int d = ipl.getNSlices();

        int objID = 0;

        int ind = 0;
        for (int z=0;z<d;z++) {
            ipl.setSlice(z+1);
            for (int x=0;x<w;x++) {
                for (int y=0;y<h;y++) {
                    int ID = ipr.getPixel(x,y); //Pixel value

                    if (ID != 0) { //Corresponds to an object
                        if (ArrayFunc.contains(IDs,ID)) { //Already has an assigned "blob" object
                            int tempInd = IDs.indexOf(ID);
                            objects.get(tempInd).addCoordinate(HCObject.X,x);
                            objects.get(tempInd).addCoordinate(HCObject.Y,y);
                            objects.get(tempInd).addCoordinate(HCObject.Z,z);

                        } else { //First instance of detection
                            IDs.add(ind,ID);
                            objects.add(ind, new HCObject(objID++));
                            objects.get(ind).addCoordinate(HCObject.X,x);
                            objects.get(ind).addCoordinate(HCObject.Y,y);
                            objects.get(ind).addCoordinate(HCObject.Z,z);

                            ind++;

                        }
                    }
                }
            }
        }
    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        // Setting the input image stack name
        parameters.addParameter(this,"Primary object identification module",new ModuleTitle("Primary object identification"),true);
        parameters.addParameter(this,INPUT_IMAGE,new ImageName(""),false);
        parameters.addParameter(this,OUTPUT_OBJECT,new HCObjectName(""),false);
        parameters.addParameter(this,MEDIAN_FILTER_RADIUS,2d,true);
        parameters.addParameter(this,THRESHOLD_MULTIPLIER,1d,true);

    }
}
