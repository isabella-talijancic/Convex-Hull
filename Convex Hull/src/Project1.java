/**
 * 
 * In this project, we will compute the convex hull of a given set of n points in two dimensions 
 * (i.e., each point has an x-coordinate and a y-coordinate) using a Divide and Conquer algorithm.
 * 
 * Output has been redirected to output.txt and designated input will be input.csv
 * 
 * UTSA CS 3343 - Project 1
 * Spring 2023
 * 
 * Authors:
 * @author Isabella Taliancic (juu530)
 * @author Amalia Taliancic (fwn783)
 * 
 */

//imports
import java.util.*;
import java.io.*;

/**
 * Project1 is a Java class containing a main method to run the program when completed.
 */
public class Project1 
{
    public static void main( String[] args ) throws Exception 
    {
    	/**
    	 * Start by reading in input from "data/input.csv" and using line.split to parse
    	 * through x and y coordinates as values within Array List
    	 */
        BufferedReader buffRead = new BufferedReader( new FileReader( "input.csv" ) );
        
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        
        String line;
        
        while ( ( line = buffRead.readLine() ) != null ) 
        {
            String[] readCoordinates = line.split( "," );
            
            double x = Double.parseDouble( readCoordinates[0] );
            double y = Double.parseDouble( readCoordinates[1] );
            
            xCoordinates.add(x);
            yCoordinates.add(y);
        }
        
       //closing file
        buffRead.close();

        /**
         * With hull integer list, will return list of points forming the convex hull
         */
        List<Integer> hull = convexHull( xCoordinates, yCoordinates );
        //System.out.println(hull); Testing
        writeFile( "output.txt", hull );
    }
    
    /**
     * Coordinates class and constructor for setting x1 and y1 variables
     */
    static class coordinates 
    { 
    	public int x1;
    	public int y1;

    	public coordinates( int x2, int y2 ) 
    	{
    	    this.x1 = x2;
    	    this.y1 = y2;
    	}
    }

    /*
     * writeFile() takes in filename as a String and a List of integers called hull 
     * in order to write output to file (output.txt) while throwing IOException
     */
    private static void writeFile( String filename, List<Integer> hull ) throws IOException 
    {
        BufferedWriter writing = null;
        try 
        {
            writing = new BufferedWriter( new FileWriter( filename ) );
            
            for ( int indexPoint : hull ) 
            {
                writing.write( indexPoint + "\n" );
            }
        } 
        catch (IOException e) 
        {
            throw e;
        } 
        finally 
        {
            if ( writing != null ) 
            {
                writing.close();
            }
        }
    }

    /**
     * convexHull() takes in doubles xCoordinates and yCoordinates. First, method finds size of 
     * inputs -- if less than three, indices are returned and if otherwise, convex hull is 
     * therefore possible and computed
     */
	public static List<Integer> convexHull( List<Double> xCoordinates, List<Double> yCoordinates ) 
	{
        int size = xCoordinates.size();
        
        List<Integer> hull = new ArrayList<>();

        if ( size < 3 ) 
        {
            for ( int i = 0; i < size; i++ ) 
            {
                hull.add(i);
            }
            return hull;
        }

        int minimum = 0;
        int maximum = size - 1;
        
        double minimumYCoord = yCoordinates.get( minimum ), maximumYCoord = yCoordinates.get( maximum );

        while ( minimum < maximum ) 
        {
            int middle;
            middle = minimum + (maximum - minimum) / 2;
            
            if ( yCoordinates.get( middle ) < minimumYCoord ) 
            {
                minimumYCoord = yCoordinates.get( middle );
                minimum = middle;
            } 
            else if ( yCoordinates.get( middle ) > maximumYCoord ) 
            {
                maximumYCoord = yCoordinates.get( middle );
                maximum = middle;
            } 
            else 
            {
                break;
            }
        }

        divCqr( xCoordinates, yCoordinates, minimum, maximum, -1, hull );
        divCqr( xCoordinates, yCoordinates, maximum, minimum, 1, hull );

        return hull;
    }

	/**
     * distance() takes in doubles representing both x and y coordinates of 3 points while finding area 
     * of the triangle they form together; by using integer clockSign, we can determine direction  
     * ( i.e. + suggests clockwise vs. - suggests counter-clockwise )
     * 
     * Depending on whether or not the distance is positive, that if
     * the points are arranged in counter-clockwise / clockwise order
     */
    public static double distance( double ax, double ay, double bx, double by, double cx, double cy, int clockSign ) 
    {
    	/**
    	 * Use of Shoelace formula to calculate area of triangle
    	 */
        double distance = ( bx - ax ) * ( cy - ay ) - ( cx - ax ) * ( by - ay );
        
        if ( clockSign * distance < 0 ) 
        {
            return -distance;
        } 
        else 
        {
            return distance;
        }
    }
	
	/**
	 * divCqr finds whether or not said coordinates are collinear and if not, makes recursive calls 
	 * to split themselves; if so, they are added to the hull
	 * 
	 * Parameters include the x and y coordinates, minimum and maximum x-coordinates, and clockSign to identify  
	 * which direction points are given via clock/counter-clockwise order
	 */
    public static void divCqr( List<Double> xCoordinates, List<Double> yCoordinates, int minimum, int maximum, int clockSign, List<Integer> hull ) 
    {
        boolean isItCollinear = true;

        int indexPoint = -1;
        
        double maximumDistance = 0;
       
        int i; 
        for ( i = minimum + 1; i < maximum; i++ ) 
        {
        	/**
        	 * getting parameters
        	 */
            double distance = distance( xCoordinates.get(minimum), yCoordinates.get(minimum), xCoordinates.get(maximum), yCoordinates.get(maximum), xCoordinates.get(i), yCoordinates.get(i), clockSign );

            if ( distance > 0 ) 
            {
                isItCollinear = false;
                
                if ( distance > maximumDistance ) 
                {
                    indexPoint = i;
                    maximumDistance = distance;
                }
            }
        }

        if ( isItCollinear ) {
        	
        	int j;
            for ( j = minimum + 1; j < maximum; j++ ) 
            {
                hull.add( j );
            }
            
            return;
        }

        divCqr(xCoordinates, yCoordinates, minimum, indexPoint, clockSign, hull);
        /**
         *  indexPoint is added to hull list
         */
        hull.add(indexPoint);
        divCqr(xCoordinates, yCoordinates, indexPoint, maximum, clockSign, hull);
    }
}