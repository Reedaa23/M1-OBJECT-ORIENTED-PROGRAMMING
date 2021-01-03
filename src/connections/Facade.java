package connections;

import connections.Segment;
import connections.Road;
import connections.OneWayRoad;
import connections.TwoWayRoad;
import connections.Route;
import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;
import facadeIndividuals.ModelException;

/**
 * A facade to enable a collection of tests.
 * 
 * The facade is an interface, a concept that we have not covered in the course.
 * An interface in Java is very similar to an abstract class. It groups definitions
 * of abstract and non-abstract methods. By definition, all methods in an interface
 * are public, even if they are not explicitly qualified public. Methods with an
 * implementation in an interface are called default methods, and must have the
 * keyword default for that purpose. 
 * 
 * The facade assumes that you have at least a class Road and a class Route.
 * If these classes are in some package (which they should be),
 * you must add import statements in the beginning of this file. In our solution,
 * these classes are all in a package named connections, hence the import statements
 * in the supplied version of the facade.
 * 
 * The facade is used by the test suite, and is some kind of intermediary
 * between the tests and your code. You must implement each method using
 * the code that you have written. To get some idea of how to do this,
 * the facade includes the implementation of the methods getRoadIdentification
 * and changeRoadIdentification in view of our solution.
 * Typically, you invoke the corresponding method from your code, you catch
 * any kind of exception that invocation may throw, and throw a new ModelException
 * as a signal that the execution was not successful.
 */
public interface Facade {

	/****************
	 * Road methods *
	 ****************/

	/**
	 * Return a new two-way road with the given identification, given start location, given
	 * end location, given length, given speed limit, given average speed, and with no delay nor
	 * blockage in the traveling direction.
	 */
	default Road createOneWayRoad(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit,
			float averageSpeed) throws ModelException {
		try {
			return new OneWayRoad(identification, endPoint1, endPoint2, length, speedLimit,
				averageSpeed);
		} catch (IllegalAllIdentificationsCreatedException exc) {
			throw new ModelException();
		} catch (IllegalStructureException exc) {
			throw new ModelException();
		} catch (IllegalSpeedLimitException exc) {
			throw new ModelException();
		} catch (IllegalAverageSpeedException exc) {
			throw new ModelException();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Return a new one-way road with the given identification, given end points, given length,
	 * given speed limit, given average speed, and with no delays in both directions nor
	 * with blockages in both directions.
	 */
	default Road createTwoWayRoad(String identification, double[] endPoint1, double[] endPoint2, int length, float speedLimit,
			float averageSpeed) throws ModelException {
		try {
			return new TwoWayRoad(identification, endPoint1, endPoint2, length, speedLimit, 
					averageSpeed);
		} catch (IllegalAllIdentificationsCreatedException exc) {
			throw new ModelException();
		} catch (IllegalStructureException exc) {
			throw new ModelException();
		} catch (IllegalSpeedLimitException exc) {
			throw new ModelException();
		} catch (IllegalAverageSpeedException exc) {
			throw new ModelException();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}


	/**
	 * Terminate the given road.
	 */
	default void terminateRoad(Road road) throws ModelException {
		try {
			road.terminate();
		} catch (IllegalAllIdentificationsCreatedException exc) {
			throw new ModelException();
		} catch (IllegalStructureException exc) {
			throw new ModelException();
		} catch (IllegalSpeedLimitException exc) {
			throw new ModelException();
		} catch (IllegalAverageSpeedException exc) {
			throw new ModelException();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Check whether the given road is terminated.
	 */
	default boolean isTerminatedRoad(Road road) throws ModelException {
		try {
			if(road.isTerminated())
				return true;
			else 
				return false;
		} catch (Throwable exc) {
			throw new ModelException();
		} 
	}

	/**
	 * Return the identification of the given road.
	 */
	default String getRoadIdentification(Road road) throws ModelException {
		try {
			return road.getIdentification();
		} catch (Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Change the identification of the given road to the given identification.
	 */
	default void changeRoadIdentification(Road road, String newIdentification) throws ModelException {
		try {
			road.setIdentification(newIdentification);
		} catch (IllegalStructureException exc) {
			throw new ModelException();
		} catch(IllegalAllIdentificationsCreatedException exc) {
			throw new ModelException();
		} catch (Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Return the end points of the given road.
	 *   The method returns an array of length 2. Both elements, in turn, are arrays of
	 *   length 2 containing the latitude followed by the longitude as two values of
	 *   type double.
	 */
	default double[][] getEndPoints(Road road) throws ModelException {
		try {
			return road.getEndPoints();
		} catch (Throwable exc) {
			throw new ModelException();
		} 
	}
	
	/**
	 * Return the start locations of the given road.
	 *   The method returns an array of length 1 or 2, with its elements in the same
	 *   order as the elements of the array returned by getEndPoints.
	 */
	default double[][] getStartLocations(Road road) throws ModelException {
		double[][] startLocations=new double[road.getValidStartLocations().size()][2];
		int j=0;
		for(double[] i:road.getValidStartLocations()) {
			startLocations[j]=i;
			j++;
		}
		return startLocations;
	}

	/**
	 * Return the end locations of the given road.
	 *   The method returns an array of length 1 or 2, with its elements in the same
	 *   order as the elements of the array returned by getEndPoints.
	 */
	default double[][] getEndLocations(Road road) throws ModelException {
		double[][] endLocations=new double[road.getValidEndLocations().size()][2];
		int j=0;
		for(double[] i:road.getValidEndLocations()) {
			endLocations[j]=i;
			j++;
		}
		return endLocations;
	}

	/**
	 * Return the length of the given road.
	 */
	default int getRoadLength(Road road) throws ModelException {
		try {
			return road.getLength();
		} catch (Throwable exc) {
			throw new ModelException();
		} 
	}

	/**
	 * Change the length of the given road to the given length.
	 */
	default void changeRoadLength(Road road, int newLength) throws ModelException {
		try {
			road.setLength(newLength);
		} catch (Throwable exc) {
			throw new ModelException();
		} 
	}

	/**
	 * Return the speed limit for the given road.
	 */
	default float getRoadSpeedLimit(Road road) throws ModelException {
		try {
			return road.getSpeedLimit();
		} catch (Throwable exc) {
			throw new ModelException();
		} 
	}
	
	/**
	 * Change the speed limit of the given road to the given speed limit.
	 */
	default void changeRoadSpeedLimit(Road road, float newSpeedLimit) throws ModelException {
		try {
			road.setSpeedLimit(newSpeedLimit);
		} catch (IllegalSpeedLimitException exc) {
			throw new ModelException();
		} catch (IllegalAverageSpeedException exc) {
			throw new ModelException();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Return the average speed for the given road.
	 */
	default float getRoadAverageSpeed(Road road) throws ModelException {
		try {
			return road.getAverageSpeed();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Change the average speed of the given road to the given average speed.
	 */
	default void changeRoadAverageSpeed(Road road, float newAverageSpeed) throws ModelException {
		try {
			road.setAverageSpeed(newAverageSpeed);
		} catch (IllegalAverageSpeedException exc) {
			throw new ModelException();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Get the current delay in the direction from the first end point to the second
	 * end point, if directionForth is true, and in the opposite direction if it 
	 * is false.
	 */
	default float getRoadDelayinDirection(Road road, boolean directionForth) throws ModelException {
		try {
			if(directionForth)
				return road.getCurrentDelayForth();
			else
				return road.getCurrentDelayOpposite();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Set the current delay in the direction from the first end point to the second
	 * end point to the given delay, if directionForth is true, and to the given delay
	 * in the opposite direction if directionForth is false.
	 */
	default void changeRoadDelayinDirection(Road road, float delay, boolean directionForth) throws ModelException {
		try {
			if(directionForth) {
				road.setCurrentDelayForth(delay);
			}
			else {
				road.setCurrentDelayOpposite(delay);
			}
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Check whether the given road is blocked in the in the direction from the first
	 * end point to the second end point, if directionForth is true, and in the opposite
	 * direction if it  is false.
	 */
	default boolean getRoadIsBlocked(Road road, boolean directionForth) throws ModelException {
		try {
			if(directionForth) {
				return road.getBlockedForth();
			}
			else {
				return road.getBlockedOpposite();
			}
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/**
	 * Set the blocked state of the given road in the direction from the first end point
	 * to the second end point according to the given flag, if directionForth is true,
	 * and according to the given flag in the opposite direction if directionForth is false.
	 */
	default void changeRoadBlockedState(Road road, boolean flag, boolean directionForth) throws ModelException {
		try {
			if(directionForth) {
				road.setBlockedForth(flag);
			}
			else {
				road.setBlockedOpposite(flag);
			}
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

	/*****************
	 * Route methods *
	 *****************/
	
	/**
	 * Return a new route with given start location and given segments.
	 */
	default Route createRoute(double[] startLocation, Object... segments) throws Exception {
		try {
			Segment[] theSegments = new Segment[segments.length];
			int i=0;
			for(Object segment1 : segments) {
				Segment segment2 = (Segment) segment1;
				theSegments[i]=segment2;
				i++;
			}
			return new Route(startLocation,theSegments);
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Return the start location of the given route.
	 */
	default double[] getRouteStartLocation(Route route) throws ModelException {
		try {
			return route.getStartLocation();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Return the segments of the given route.
	 */
	default Object[] getRouteSegments(Route route) throws ModelException {
		try {
			Segment[] routeSegments=new Segment[route.getSegments().size()];
			for(int i=0;i<route.getSegments().size();i++) {
				routeSegments[i]=route.getSegments().get(i);
			}
			return routeSegments;
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Add the given road at the end of the sequence of segments of the given route.
	 */
	default void addRouteSegment(Route route, Object segment) throws ModelException {
		try {
			Segment theSegment = (Segment) segment;
			route.addSegment(theSegment);
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Remove the segment at the given index from the sequence of segments of the given route.
	 */
	default void removeRouteSegment(Route route, int index) throws ModelException {
		try {
			route.removeSegment(index);
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Return the total length of the given route.
	 */
	default int getRouteTotalLength(Route route) throws ModelException {
		try {
			return route.getTotalLength();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Check whether the given route is traversable from its start location
	 * to its end location.
	 */
	default boolean isRouteTraversable(Route route) throws ModelException {
		try {
			return route.isTraversable();
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}
	
	/**
	 * Check whether the given route is traversable from its start location
	 * to its end location.
	 */
	default double[][] getAllLocations(Route route) throws ModelException {
		try {
			double[][] allLocations=new double[route.getLocationsVisited().size()][];
			for(int i=0;i<route.getLocationsVisited().size();i++) {
				allLocations[i]=route.getLocationsVisited().get(i);
			}
			return allLocations;
		} catch(Throwable exc) {
			throw new ModelException();
		}
	}

}
