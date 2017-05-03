package wbif.sjx.common.HighContent.Object;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by steph on 30/04/2017.
 */
public class HCObject {
    // Coordinate dimensions 0-4 are reserved for X,Y,C,Z,T
    public static final int X = 0;
    public static final int Y = 1;
    public static final int C = 2;
    public static final int Z = 3;
    public static final int T = 4;

    private HashMap<Integer, ArrayList<Double>> coordinates = new HashMap<>();
    private HCObject parent = null;
    private ArrayList<HCObject> children = new ArrayList<HCObject>();


    // PUBLIC METHODS

    /**
     * Projects xy coordinates into a single plane.  Duplicates of xy coordinates at different heights are removed.
     * @return HashMap containing x and y coordinates as double[].  Integer key corresponds to dimension (X=0, Y=1)
     */
    public HashMap<Integer, ArrayList<Double>> getZProjectedCoordinates() {
        ArrayList<Double> x = coordinates.get(X);
        ArrayList<Double> y = coordinates.get(Y);

        // All coordinate pairs will be stored in a HashMap, which will prevent coordinate duplication.  The keys will
        // correspond to the 2D index, for which we need to know the maximum x coordinate
        double maxX = Double.MIN_VALUE;
        for (double currX:x) {
            if (currX > maxX) {
                maxX =currX;
            }
        }

        // Running through all coordinates, adding them to the HashMap
        HashMap<Double, Integer> projCoords = new HashMap<Double, Integer>();
        for (int i=0;i<x.size();i++) {
            Double key = y.get(i)*maxX + x.get(i);
            projCoords.put(key,i);
        }

        // Creating arrays to store the projected x and y coordinates
        ArrayList<Double> xOut = new ArrayList<>(projCoords.size());
        ArrayList<Double> yOut = new ArrayList<>(projCoords.size());

        int iter = 0;
        for (Double key:projCoords.keySet()) {
            int i = projCoords.get(key);

            xOut.set(iter,x.get(i));
            yOut.set(iter,y.get(i));

            iter++;
        }

        // Adding the projected coordinates to the output HashMap.  The HashMap structure is the same as the standard
        // coordinate array model
        HashMap<Integer,ArrayList<Double>> projCoordsOut = new HashMap<>();
        projCoordsOut.put(X,xOut);
        projCoordsOut.put(Y,yOut);

        return projCoordsOut;

    }

    public void addCoordinate(int dim, double coordinate) {
        coordinates.computeIfAbsent(dim, k -> new ArrayList<>());
        coordinates.get(dim).add(coordinate);

    }

    public void removeCoordinate(int dim, double coordinate) {
        coordinates.get(dim).remove(coordinate);

    }

    @Override
    public String toString() {
        return  "HCObject with "+coordinates.size()+" dimensions and "+coordinates.values().iterator().next().size()+
                " coordinate points";

    }

    // GETTERS AND SETTERS

    public void setCoordinate(ArrayList<Double> coordinateList, int dim) {
        coordinates.put(dim, coordinateList);

    }

    public ArrayList<Double> getCoordinate(int dim) {
        return coordinates.get(dim);

    }

    public HashMap<Integer, ArrayList<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(HashMap<Integer, ArrayList<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    public HCObject getParent() {
        return parent;
    }

    public void setParent(HCObject parent) {
        this.parent = parent;
    }

    public ArrayList<HCObject> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<HCObject> children) {
        this.children = children;
    }

    public void addChild(HCObject child) {
        children.add(child);
    }

    public void removeChild(HCObject child) {
        children.remove(child);
    }
}
