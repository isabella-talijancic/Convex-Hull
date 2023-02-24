/**
 * Project1 is a Java class containing a main method to run the program when completed.
 * 
 * UTSA CS 3343 - Project 1
 * Fall 2023
 * 
 * Authors:
 * - Isabella Taliancic (juu530)
 * - Amalia Taliancic (fwn783)
 */

import java.util.*;
import java.io.*;

public class Project1 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("data/input.csv"));
        List<Double> xCoords = new ArrayList<>();
        List<Double> yCoords = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] coords = line.split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            xCoords.add(x);
            yCoords.add(y);
        }
        br.close();

        List<Integer> hull = convexHull(xCoords, yCoords);
        System.out.println(hull);
        writeConvexHullToFile("data/output.txt", hull);
    }

    private static void writeConvexHullToFile(String filename, List<Integer> hull) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            for (int index : hull) {
                writer.write(index + "\n");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

	public static List<Integer> convexHull(List<Double> xCoords, List<Double> yCoords) {
        int n = xCoords.size();
        List<Integer> hull = new ArrayList<>();

        if (n < 3) {
            for (int i = 0; i < n; i++) {
                hull.add(i);
            }
            return hull;
        }

        int min = 0, max = n - 1;
        double minY = yCoords.get(min), maxY = yCoords.get(max);

        while (min < max) {
            int mid = min + (max - min) / 2;
            if (yCoords.get(mid) < minY) {
                minY = yCoords.get(mid);
                min = mid;
            } else if (yCoords.get(mid) > maxY) {
                maxY = yCoords.get(mid);
                max = mid;
            } else {
                break;
            }
        }

        divideAndConquer(xCoords, yCoords, min, max, -1, hull);
        divideAndConquer(xCoords, yCoords, max, min, 1, hull);

        return hull;
    }

    public static void divideAndConquer(List<Double> xCoords, List<Double> yCoords, int min, int max, int sign, List<Integer> hull) {
        boolean allCollinear = true;

        int index = -1;
        double maxDist = 0;
        for (int i = min + 1; i < max; i++) {
            double dist = distance(xCoords.get(min), yCoords.get(min), xCoords.get(max), yCoords.get(max), xCoords.get(i), yCoords.get(i), sign);
            if (dist > 0) {
                allCollinear = false;
                if (dist > maxDist) {
                    index = i;
                    maxDist = dist;
                }
            }
        }

        if (allCollinear) {
            for (int i = min + 1; i < max; i++) {
                hull.add(i);
            }
            return;
        }

        divideAndConquer(xCoords, yCoords, min, index, sign, hull);
        hull.add(index);
        divideAndConquer(xCoords, yCoords, index, max, sign, hull);
    }

    public static double distance(double ax, double ay, double bx, double by, double cx, double cy, int sign) {
        double dist = (bx - ax) * (cy - ay) - (cx - ax) * (by - ay);
        if (sign * dist < 0) {
            return -dist;
        } else {
            return dist;
        }
    }

    static class Point { 
    	public int x;
    	public int y;

    	public Point(int x2, int y2) {
    	    this.x = x2;
    	    this.y = y2;
    	}
 }
    
}