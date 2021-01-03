package connections;

import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalLengthException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;

/**
 * A class illustrating how to manipulate roads and routes by
 * means of the methods offered by their classes. 
 *  
 */
public class RoadRouteUsage {

	public static void main(String[] args) throws IllegalAllIdentificationsCreatedException, IllegalStructureException, IllegalLengthException, IllegalSpeedLimitException, IllegalAverageSpeedException {
		// Instantiate four distinct locations
		double[] location1 = new double [] {3.4,8.6};
		double[] location2 = new double [] {13.8,22.0};
		double[] location3 = new double [] {45.3,18.1};
		double[] location4 = new double [] {27.9,20.6};
		
		// Creation of the roads connecting the locations, including the country road
		Road road1 = new OneWayRoad("A1",location1,location2,10000,33.0F,28.0F);
		Road countryRoad = new TwoWayRoad("N3",location2,location3,2000,23.0F,8.0F);
		Road road3 = new TwoWayRoad("A23",location3,location4,7000,33.0F,28.0F);
		
		// Creation of the parallel road to the country road
		Road motorway = new TwoWayRoad("N1",location2,location3,4000,25.0F,20.F);
		
		// Display the values of all properties of countryRoad
			System.out.println("The identification of the country road is : " + countryRoad.getIdentification()+".");
			System.out.println("The first end point's coordinates of the country road are : {" + countryRoad.getEndPoint1()[0] + "," + countryRoad.getEndPoint1()[1] + "}.");
			System.out.println("The second end point's coordinates of the country road are : {" + countryRoad.getEndPoint2()[0] + "," + countryRoad.getEndPoint2()[1] + "}.");
			System.out.println("The length of the country road is : " + countryRoad.getLength()+" meters.");
			System.out.println("The current delay on the direction from the first end point to the second end point of the country road is : " + countryRoad.getCurrentDelayForth()+" seconds.");
			System.out.println("The current delay on the direction from the second end point to the first end point of the country road is : " + countryRoad.getCurrentDelayOpposite()+" seconds.");
			System.out.println("The speed limit of the country road is : " + countryRoad.getSpeedLimit()+" meters per second.");
			System.out.println("The average speed of tthe country road is : " + countryRoad.getAverageSpeed()+" meters per second.");
			System.out.println("Is the country road blocked on the direction from the first end point to the second end point ? " + countryRoad.getBlockedForth()+".");
			System.out.println("Is the country road blocked on the direction from the second end point to the first end point ? " + countryRoad.getBlockedOpposite()+".");
		
		// Display the values of all properties of motorway
			System.out.println("************************************************************************************************************************");
			System.out.println("The identification of the motorway is : " + motorway.getIdentification()+".");
			System.out.println("The first end point's coordinates of the motorway are : {" + motorway.getEndPoint1()[0] + "," + motorway.getEndPoint1()[1] + "}.");
			System.out.println("The second end point's coordinates of the motorway are : {" + motorway.getEndPoint2()[0] + "," + motorway.getEndPoint2()[1] + "}.");
			System.out.println("The length of the motorway is : " + motorway.getLength()+" meters.");
			System.out.println("The current delay on the direction from the first end point to the second end point of the motorway is : " + motorway.getCurrentDelayForth()+" seconds.");
			System.out.println("The current delay on the direction from the second end point to the first end point of the motorway is : " + motorway.getCurrentDelayOpposite()+" seconds.");
			System.out.println("The speed limit of the motorway is : " + motorway.getSpeedLimit()+" meters per second.");
			System.out.println("The average speed of the motorway is : " + motorway.getAverageSpeed()+" meters per second.");
			System.out.println("Is the motorway blocked on the direction from the first end point to the second end point ? " + motorway.getBlockedForth()+".");
			System.out.println("Is the motorway blocked on the direction from the second end point to the first end point ? " + motorway.getBlockedOpposite()+".");
		
		// Calculate the required cumulative that is needed to traverse each of the two parallel roads and display it
		float time1 = (countryRoad.getLength()/countryRoad.getAverageSpeed());
		float time2 = (motorway.getLength()/motorway.getAverageSpeed());
			System.out.println("************************************************************************************************************************");
			System.out.println("The required cumulative time that is needed to traverse the country road is : " + time1 + " seconds.");
			System.out.println("The required cumulative time that is needed to traverse the motorway is : " + time2 + " seconds.");
		
		// Creating of the route involving all four locations
		Route route = new Route(location1,road1,motorway,road3);
		
		// Display the total length and the visited locations of the route
			System.out.println("************************************************************************************************************************");
			System.out.println("The total length of the route is : " + route.getTotalLength() + " meters.");
			System.out.println("The locations visited by the route are : ");
			
			for(double[] location : route.getLocationsVisited()) {
				System.out.println("{" + location[0] + "," + location[1] + "}");
			}

		// Block the motorway and display if the route is still traversable or not
		motorway.setBlockedForth(true);
		
			System.out.println("************************************************************************************************************************");
			System.out.println("Is the route still traversable ? " + route.isTraversable() + ".");
			
			
	}

}
