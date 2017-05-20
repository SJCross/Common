package wbif.sjx.common.HighContent.Module;

import ij.process.LUT;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Object.HCParameterCollection;
import wbif.sjx.common.Object.RandomLUT;


/**
 * Created by sc13967 on 03/05/2017.
 */
public class ShowObjects extends HCModule {
    public final static String INPUT_OBJECTS = "Input objects";
    public final static String TEMPLATE_IMAGE = "Template image";
    public final static String USE_GROUP_ID = "Use group ID";

    @Override
    public String getTitle() {
        return "Show objects";
    }

    @Override
    public void execute(HCWorkspace workspace, boolean verbose) {
        // Loading objects
        HCName inputObjectName = parameters.getValue(INPUT_OBJECTS);
        HCObjectSet inputObjects = workspace.getObjects().get(inputObjectName);

        // Getting parameters
        boolean useGroupID = parameters.getValue(USE_GROUP_ID);

        HCImage templateImage;
        if (parameters.getParameter(TEMPLATE_IMAGE) == null) {
            templateImage = null;

        } else {
            HCName templateImageName = parameters.getValue(TEMPLATE_IMAGE);
            templateImage = workspace.getImages().get(templateImageName);

        }

        // Converting objects to an image
        HCImage image = new ObjectImageConverter().convertObjectsToImage(inputObjects,templateImage,useGroupID);
        image.getImagePlus().setTitle(inputObjectName.getName());

        // Creating a random colour LUT and assigning it to the image (maximising intensity range to 0-255)
        LUT randomLUT = new RandomLUT().getLUT();
        image.getImagePlus().getProcessor().setLut(randomLUT);
        image.getImagePlus().getProcessor().setMinAndMax(0,255);

        // Showing the image
        image.getImagePlus().show();

    }

    @Override
    public HCParameterCollection initialiseParameters() {
        HCParameterCollection parameters = new HCParameterCollection();

        parameters.addParameter(new HCParameter(this,INPUT_OBJECTS, HCParameter.INPUT_OBJECTS,null));
        parameters.addParameter(new HCParameter(this,TEMPLATE_IMAGE, HCParameter.INPUT_IMAGE,null));
        parameters.addParameter(new HCParameter(this,USE_GROUP_ID,HCParameter.BOOLEAN,true));

        return parameters;

    }

    @Override
    public HCParameterCollection getActiveParameters() {
        return parameters;
    }

    @Override
    public HCMeasurementCollection addActiveMeasurements() {
        return null;
    }
}