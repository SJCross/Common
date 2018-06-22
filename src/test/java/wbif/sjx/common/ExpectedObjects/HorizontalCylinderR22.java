package wbif.sjx.common.ExpectedObjects;

import java.util.List;

public class HorizontalCylinderR22 extends ExpectedObjects {
    @Override
    public List<Integer[]> getCoordinates5D() {
        return getCoordinates5D("/coordinates/HorizontalBinaryCylinder3D_R22_whiteBG_8bit.csv");
    }

    @Override
    public boolean is2D() {
        return false;
    }
}
