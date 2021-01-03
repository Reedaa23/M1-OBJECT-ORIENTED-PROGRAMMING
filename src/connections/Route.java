package connections;
import java.util.*;
import be.kuleuven.cs.som.annotate.*;
import exceptions.IllegalAllIdentificationsCreatedException;
import exceptions.IllegalAverageSpeedException;
import exceptions.IllegalSpeedLimitException;
import exceptions.IllegalStructureException;

/**
 * A class of routes involving a number of segments of
 * roads.
 * 
 * @invar  The start location of each route must be a valid start location for any
 *         route.
 *       | isValidStartLocation(getStartLocation())
 * @invar  Each route must have proper segments.
 *       | hasProperSegments()
 * 
 */
public class Route extends Segment{
	
	/**
	 * Variable registering the start location of this route.
	 */
	private final double[] startLocation;
	
	/**
	 * ArrayList registering the segments of this route.
	 */
	private final ArrayList<Segment> segments = new ArrayList<Segment>();
    /**
     * Variable registering whether or not this route has been
     * terminated.
     */
    private boolean isTerminated = false;
    /**
     * Variable registering the routes with segments to which this route belongs. 
     */
    private final ArrayList<Route> listOfRoutes = new ArrayList<Route>();
	
	/**
	 * 
	 * @param startLocation
	 * 		  The new start location for this new route.
	 * @param segments
	 * 		  The new segments to add for this new route in the order they're given.
	 * @pre   The given start location must be a valid start location for a
	 *        route.
	 *      | isValidStartLocation(startLocation)
	 * @post  The start location of this new route is equal to the given start
	 * 		  location.
	 *      | new.getStartLocation == new double[] {startLocation[0],startLocation[1]}
	 * @post  The sequence of segments of this new route contains the given segments.
	 * 		| for (Segment segment : segments):
	 * 		| 	new.getSegments.contains(segment)
	 * @post  This route is added to the routes of each road of the given segments if any.
	 * 		| for (Segment segment : segments):
	 * 	    |  if(segment instanceof Road)
	 * 		| 	then (segment.getRoutes.contains(this))
	 * @post  This route has a proper sequence of segments.
	 * 		| 	hasProperSegments()
	 * @effect The given segments are added to the sequence of segments of this route in the
	 * 		   order they're given.
	 * 		|  for(Segment segment : segments)
	 * 		|  	addSegment(segment)
	 * 
	 */
	@Raw
    public Route(double[] startLocation, Segment...segments) {
		assert isValidStartLocation(startLocation);
		this.startLocation=startLocation;
		for(Segment segment : segments) {
				if(segment instanceof Road) {
					Road road = (Road) segment;
					road.addRoute(this);
				}
				addSegment(segment);
		}
    }
	
	/**
	 * Check whether the given start location is a valid start location for
	 * any route.
	 *  
	 * @param  startLocation
	 *         The start location to check.
	 * @return True if and only if the length of the given start location is 2 and if the given latitude and longitude of the given start location are both between 0 and the maximum of degrees.
	 *       | result == (startLocation.length==2) && (startLocation[0] >= Road.getMinDegrees()) && (startLocation[0]<= Road.getMaxDegrees()) && (startLocation[1] >= Road.getMinDegrees()) && (startLocation[1]<= Road.getMaxDegrees())
	*/
	public static boolean isValidStartLocation(double[] startLocation) {
		return (startLocation.length==2) && (startLocation[0] >= Road.getMinDegrees()) && (startLocation[0]<= Road.getMaxDegrees()) && (startLocation[1] >= Road.getMinDegrees()) && (startLocation[1]<= Road.getMaxDegrees());
	}
	
    /**
     * Check whether this route is already terminated.
     */
    @Basic @Raw
    public boolean isTerminated() {
        return this.isTerminated;
    }

    /**
     * Terminate this route.
     *
     * @post   This route is terminated.
     *       | new.isTerminated()
     * @post   All segments belonging to this route
     *         upon entry, have been terminated.
     *       | for each segment in segments:
     *       |     (new segment).isTerminated()
	 * @throws IllegalAllIdentificationsCreatedException
	 * 		   The given identification is equal to one of the previous created identifications.
	 * 		 | allIdentificationsCreated.contains(identification)
	 * @throws IllegalStructureException
	 *         The given identification is not a valid identification for any
	 *         road.
	 *       | ! isValidIdentification(getIdentification())
	 * @throws IllegalSpeedLimitException
	 *         The given speed limit is not a valid speed limit for any
	 *         road.
	 *       | ! isValidSpeedLimit(getSpeedLimit())
	 * @throws IllegalAverageSpeedException
	 *         The given average speed is not a valid average speed for any
	 *         road.
	 *       | ! isValidAverageSpeed(getAverageSpeed())
     */
    @Override
    public void terminate() throws IllegalStructureException, IllegalAllIdentificationsCreatedException, IllegalAverageSpeedException, IllegalSpeedLimitException {
        if (!isTerminated()) {
            Collection<Segment> elements = segments;
            Collection<Segment> elementsClone = new ArrayList<Segment>(
                elements);
            for (Segment theSegment : elementsClone) {
                theSegment.terminate();
            }
        this.isTerminated = true;
        }
    }

    
	/**
	 * Return the sequence of segments of this route.
	 */
	@Basic @Raw
	public ArrayList<Segment> getSegments() {
		ArrayList<Segment> clone = new ArrayList<Segment>();
		for(int i=0;i<segments.size();i++) {
			clone.add(segments.get(i));
		}
		return clone;
	}
	
	/**
	 * Return the segment at the given index.
	 * 
	 * @param  index
	 * @return The segment at the given index.
	 * 		 | result == 
	 * 		 | segments.get(index)
	 * 		
	 */
	@Basic @Raw
	public Segment getSegmentAt(int index) {
		return this.segments.get(index);
	}

    /**
     * Check whether this route has the given segment as one
     * of its segments.
     * 
     * @param	segment
     * 			The segment to check.
     * @return	True if and only if the given segment is effective and that the sequence of segments of this route contain the given segment.
     * @throws	NullPointerException
     * 			The given segment is not effective.
     * 		  | segment == null
     */
    @Basic @Raw
    public boolean hasAsSegment(@Raw
    Segment segment) {
        try {
            return segments.contains(segment);
        }
        catch (NullPointerException exc) {
            assert (segment == null);
            return false;
        }
    }

    /**
     * Check whether this route can have the given segment
     * as the last segment of its sequence of segments.
     * 
     * @param   segment
     *          The segment to check.
     * @return  If the sequence of segments of this route is non-empty, true if and only 
     * 			if the given segment is effective and that if the given segment is a
     *          road, it can have this route as one of its routes and it has a mutual end point
     *          with the last road of the sequence of segments of this route.
     *          If the given segment is a route, true if and only if its start location is equal to the end location of this route.
     *          If the sequence of segments of this route is empty, true if and only if the road
     *          has the start location as an end point.
     *        | result ==
	 *        | if(segments.size()==0 && segment instanceOf Road)
	 *        |		then ((road != null) && (road.canHaveAsRoute(this)) && ((road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1]) || (road.getEndPoint2()[0]==startLocation[0] && road.getEndPoint2()[1]==startLocation[1])))
	 *        | else if(segments.size()!=0 segment instanceOf Route)
	 *        |     then ((route != null) && !route.hasAsSubSegment(this) && (route.getStartLocation()[0]==startLocation[0] && route.getStartLocation()[1]==startLocation[1]))
	 *        | else if(segment instanceOf Road)
	 *        | 	then ((road != null) && (road.canHaveAsRoute(this)) && ((road.getEndPoint1()[0]==getEndLocation()[0] && road.getEndPoint1()[1]==getEndLocation()[1]) || (road.getEndPoint2()[0]==getEndLocation()[0] && road.getEndPoint2()[1]==getEndLocation()[1])))
	 *        | else if(segment instanceOf Route)
	 *        | 	then ((route != null) && !route.hasAsSubSegment(this) && (route.getStartLocation()[0]==getEndLocation()[0] && route.getStartLocation()[1]==getEndLocation()[1]))
     */
    @Raw
    public boolean canHaveAsSegment(Segment segment) {
    	if(segment instanceof Road) {
    		Road road = (Road) segment;
	    	if(segments.size()==0) {
	    		if(road instanceof OneWayRoad) {
		    		return (road != null) && !road.hasAsSubSegment(this) && (road.canHaveAsRoute(this)) && (road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1]);
	    		}
	    		else {
		    		return (road != null) && !road.hasAsSubSegment(this) && (road.canHaveAsRoute(this)) && ((road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1]) || (road.getEndPoint2()[0]==startLocation[0] && 
		    				road.getEndPoint2()[1]==startLocation[1]));
	    		}
	    	}
	    	else {
	    		if(road instanceof OneWayRoad) {
		    		return (road != null) && !road.hasAsSubSegment(this) && (road.canHaveAsRoute(this)) && ((road.getEndPoint1()[0]==getEndLocation()[0] && road.getEndPoint1()[1]==getEndLocation()[1]));
	    		}
	    		else {
		    		return (road != null) && !road.hasAsSubSegment(this) && (road.canHaveAsRoute(this)) && ((road.getEndPoint1()[0]==getEndLocation()[0] && road.getEndPoint1()[1]==getEndLocation()[1]) || (road.getEndPoint2()[0]==getEndLocation()[0] && road.getEndPoint2()[1]==getEndLocation()[1]));
	    		}
	    	}
    	}
    	else {
    		Route route = (Route) segment;
    		if(segments.size()==0) {
    			return (route != null) && !route.hasAsSubSegment(this) && (route.getStartLocation()[0]==startLocation[0] && route.getStartLocation()[1]==startLocation[1]);
    		}
    		else {
    			return (route != null) && !route.hasAsSubSegment(this) && (route.getStartLocation()[0]==getEndLocation()[0] && route.getStartLocation()[1]==getEndLocation()[1]);
    		}
    	}
    }
    

    /**
     * Check whether this route has a proper sequence of segments.
     * 
     * @return  True if and only if this route has at least one
     * 			segment, and if for all segments in this route,
     *          this route can have that segment as one of its
     *          segments, if that segment
     *          references this route as one of its routes if it is a road, 
     *          if that segment is the first segment of this route
     *          and one if its end points is equal to the start
     *          location of this route if it is a road and equal its start location is equal to the
     *          start location of this route if it is a route, if this route has more than
     *          one segment and besides the last segment every segment
     *          in its sequence of segments has one mutual end point with the 
     *          next one, and if this route has more than one 
     *          segment and besides the first segment every segment
     *          has one mutual end point with the previous one.
     *        | (for each road in segments || road in segment in segments):
     *        |    road!=null && road.canHaveAsRoute(this) &&
     *        |    road.getRoutes().contains(this) &&
     *        |		if (road==segments.get(0))
     *        |			then road.getEndPoint1()==getStartLocation() || road.getEndPoint2()==getStartLocation()
     *        | 	if (segments.size()>1 && road!=segments.get(segments.size()))
     *        | 		then road.getEndPoint1()==segments.get(segments.indexOf(road)+1).getStartLocation() || road.getEndPoint2()==segments.get(segments.indexOf(road)+1).getStartLocation()
     *        |		if (segments.size()>1 && road!=segments.get(0))
     *        |			then road.getEndPoint1()==segments.get(segments.indexOf(road)-1).getEndLocation() || road.getEndPoint2()==segments.get(segments.indexOf(road)-1).getEndStartLocation()
     */
    public boolean hasProperSegments() {
    	if(segments.size()==0)
    		return false;
    	if(segments.size()==1) {
    		if(segments.get(0) instanceof Road) {
    			Road road = (Road) segments.get(0);
	        	if(!road.canHaveAsRoute(this))
	        		return false;
	            if (!road.getRoutes().contains(this))
	                return false;
    			if(road instanceof OneWayRoad) {
    				if(road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1]) {
    					return true;
    				}
    				return false;
    			}
    			else {
    				if((road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1]) || (road.getEndPoint2()[0]==startLocation[0] && road.getEndPoint2()[1]==startLocation[1])) {
    					return true;
    				}
    				return false;
    			}
    		}
    		else {
    			Route route = (Route) segments.get(0);
    			if(route.getStartLocation()[0]==startLocation[0] && route.getStartLocation()[1]==startLocation[1] 
    					&& route.getEndLocation()[0]==getEndLocation()[0] && route.getEndLocation()[1]==getEndLocation()[1] && route.hasProperSegments()) {
    				return true;
    			}
    			return false;	
    		}	
    	}
    	else {
    		int i=0;
    		double [] common = new double[] {};
    		double [] common1 = new double[] {};
    		double [] common2 = new double[] {};
    		double [] common3 = new double[] {};
	        for (Segment segment : segments) {
	        	if(segment instanceof Road) {
	        		Road road = (Road) segment;
		        	if(!road.canHaveAsRoute(this))
		        		return false;
		            if (!road.getRoutes().contains(this))
		                return false;
		        	if(i==0 && ((road.getEndPoint1()[0]!=getStartLocation()[0] || road.getEndPoint1()[1]!=getStartLocation()[1]) && (road.getEndPoint2()[0]!=getStartLocation()[0] || road.getEndPoint2()[1]!=getStartLocation()[1])))
		        		return false;
		        	if(i>0 && segments.get(i-1) instanceof Road) {
		        		Road theRoad2 = (Road) segments.get(i-1);
		        		if(theRoad2.getEndPoint1()[0]==road.getEndPoint1()[0] && theRoad2.getEndPoint1()[1]==road.getEndPoint1()[1])
		        			common=road.getEndPoint1();
		        		else if(theRoad2.getEndPoint2()[0]==road.getEndPoint1()[0] && theRoad2.getEndPoint2()[1]==road.getEndPoint1()[1])
		        			common=road.getEndPoint1();
		        		else if(theRoad2.getEndPoint1()[0]==road.getEndPoint2()[0] && theRoad2.getEndPoint1()[1]==road.getEndPoint2()[1])
		        			common=road.getEndPoint2();
		        		else if(theRoad2.getEndPoint2()[0]==road.getEndPoint2()[0] && theRoad2.getEndPoint2()[1]==road.getEndPoint2()[1])
		        			common=road.getEndPoint2();
		        		if(road instanceof OneWayRoad) {
		        			if(theRoad2 instanceof OneWayRoad) {
					            if((road.getEndPoint1()[0]!=theRoad2.getEndPoint2()[0] || road.getEndPoint1()[1]!=theRoad2.getEndPoint2()[1]))
					                return false;
		        			}
		        			else {
		        				if (((road.getEndPoint1()[0]!=theRoad2.getEndPoint1()[0] || road.getEndPoint1()[1]!=theRoad2.getEndPoint1()[1]) && common[0]==theRoad2.getEndPoint1()[0] && common[1]==theRoad2.getEndPoint1()[1] && common[0]==road.getEndPoint1()[0] && common[1]==road.getEndPoint1()[1]) && 
					            		((road.getEndPoint1()[0]!=theRoad2.getEndPoint2()[0] || road.getEndPoint1()[1]!=theRoad2.getEndPoint2()[1]) && common[0]==theRoad2.getEndPoint2()[0] && common[1]==theRoad2.getEndPoint2()[1] && common[0]==road.getEndPoint1()[0] && common[1]==road.getEndPoint1()[1]))
		        				return false; 
		        			}
		        		}
		        		else {
		        			if(theRoad2 instanceof OneWayRoad) {
					            if (((road.getEndPoint1()[0]!=theRoad2.getEndPoint2()[0] || road.getEndPoint1()[1]!=theRoad2.getEndPoint2()[1]) && common[0]==theRoad2.getEndPoint2()[0] && common[1]==theRoad2.getEndPoint2()[1] && common[0]==road.getEndPoint1()[0] && common[1]==road.getEndPoint1()[1]) &&
					            		((road.getEndPoint2()[0]!=theRoad2.getEndPoint2()[0] || road.getEndPoint2()[1]!=theRoad2.getEndPoint2()[1]) && common[0]==theRoad2.getEndPoint2()[0] && common[1]==theRoad2.getEndPoint2()[1] && common[0]==road.getEndPoint2()[0] && common[1]==road.getEndPoint2()[1]))
					                return false;
		        			}
		        			else {
					            if (((road.getEndPoint1()[0]!=theRoad2.getEndPoint1()[0] || road.getEndPoint1()[1]!=theRoad2.getEndPoint1()[1]) && common[0]==theRoad2.getEndPoint1()[0] && common[1]==theRoad2.getEndPoint1()[1] && common[0]==road.getEndPoint1()[0] && common[1]==road.getEndPoint1()[1]) && 
					            		((road.getEndPoint1()[0]!=theRoad2.getEndPoint2()[0] || road.getEndPoint1()[1]!=theRoad2.getEndPoint2()[1]) && common[0]==theRoad2.getEndPoint2()[0] && common[1]==theRoad2.getEndPoint2()[1] && common[0]==road.getEndPoint1()[0] && common[1]==road.getEndPoint1()[1]) && 
					            		((road.getEndPoint2()[0]!=theRoad2.getEndPoint1()[0] || road.getEndPoint2()[1]!=theRoad2.getEndPoint1()[1]) && common[0]==theRoad2.getEndPoint1()[0] && common[1]==theRoad2.getEndPoint1()[1] && common[0]==road.getEndPoint2()[0] && common[1]==road.getEndPoint2()[1]) &&
					            		((road.getEndPoint2()[0]!=theRoad2.getEndPoint2()[0] || road.getEndPoint2()[1]!=theRoad2.getEndPoint2()[1]) && common[0]==theRoad2.getEndPoint2()[0] && common[1]==theRoad2.getEndPoint2()[1] && common[0]==road.getEndPoint2()[0] && common[1]==road.getEndPoint2()[1]))
		        				return false;
		        			}
		        		}
		        	}
		        	else if(i>0 && segments.get(i-1) instanceof Route){
		        		Route theRoute2 = (Route) segments.get(i-1);
		        		if(theRoute2.getEndLocation()[0]==road.getEndPoint1()[0] && theRoute2.getEndLocation()[1]==road.getEndPoint1()[1])
		        			common1=road.getEndPoint1();
		        		else if(theRoute2.getEndLocation()[0]==road.getEndPoint2()[0] && theRoute2.getEndLocation()[1]==road.getEndPoint2()[1])
		        			common1=road.getEndPoint2();
		        		if(road instanceof OneWayRoad) {
			        		if((theRoute2.getEndLocation()[0]!=road.getEndPoint1()[0] || theRoute2.getEndLocation()[1]!=road.getEndPoint1()[1]))
			        			return false;
		        		}
		        		else {
			        		if(((theRoute2.getEndLocation()[0]!=road.getEndPoint1()[0] || theRoute2.getEndLocation()[1]!=road.getEndPoint1()[1]) && common1[0]==road.getEndPoint1()[0] && common1[1]==road.getEndPoint1()[1]) &&
			        				((theRoute2.getEndLocation()[0]!=road.getEndPoint2()[0] || theRoute2.getEndLocation()[1]!=road.getEndPoint2()[1]) && common1[0]==road.getEndPoint2()[0] && common1[1]==road.getEndPoint2()[1])) {
			        			Segment[] segmentsTest = new Segment[theRoute2.getNbSegments()];
			        			for(int p=0;p<theRoute2.getNbSegments();p++) {
			        				segmentsTest[p]=theRoute2.getSegmentAt(p);
			        			}
			        			Route test = new Route(theRoute2.getStartLocation(),segmentsTest);
			        			test.addSegment(road);
				        		if(!test.hasProperSegments())
				        			return false;
			        		}
		        		}
		        	}
		        	if(i==segments.size()-1) {
		        		if(getEndLocation()[0]==road.getEndPoint1()[0] && getEndLocation()[1]==road.getEndPoint1()[1])
		        			common2=road.getEndPoint1();
		        		else if(getEndLocation()[0]==road.getEndPoint2()[0] && getEndLocation()[1]==road.getEndPoint2()[1]) {
		        			common2=road.getEndPoint2();
		        		}
			        	if(road instanceof OneWayRoad) {
			        		if(road.getEndPoint2()[0]!=getEndLocation()[0] || road.getEndPoint2()[1]!=getEndLocation()[1]) {
			        			return false;
			        		}	
			        	}
			        	else if(road instanceof TwoWayRoad) {
			        		if(((road.getEndPoint1()[0]!=getEndLocation()[0] || road.getEndPoint1()[1]!=getEndLocation()[1]) && common2[0]==road.getEndPoint1()[0] && common2[1]==road.getEndPoint1()[1])  && 
			        				((road.getEndPoint2()[0]!=getEndLocation()[0] || road.getEndPoint2()[1]!=getEndLocation()[1]) && common2[0]==road.getEndPoint2()[0] && common2[1]==road.getEndPoint2()[1])) {
			        			return false;
			        		}
			        	}
		        	}
	        	}
	        	else {
	        		Route route = (Route) segment;
	        		if(i==0 && (route.getStartLocation()[0]!=getStartLocation()[0] || route.getStartLocation()[1]!=getStartLocation()[1] || !route.hasProperSegments())) {
	        			return false;
	        		}
	        		if(i>0) {
		        		if(segments.get(i-1) instanceof Road) {
		        			Road theRoad = (Road) segments.get(i-1);
			        		if(getStartLocation()[0]==theRoad.getEndPoint1()[0] && getStartLocation()[1]==theRoad.getEndPoint1()[1])
			        			common3=theRoad.getEndPoint1();
			        		else if(getStartLocation()[0]==theRoad.getEndPoint2()[0] && getStartLocation()[1]==theRoad.getEndPoint2()[1])
			        			common3=theRoad.getEndPoint2();
			        		if(theRoad instanceof OneWayRoad) {
			        			if((route.getStartLocation()[0]!=theRoad.getEndPoint2()[0] || route.getStartLocation()[1]!=theRoad.getEndPoint2()[1] || !route.hasProperSegments()))
			        				return false;
			        		}
			        		else if(theRoad instanceof TwoWayRoad) {
			        			if(((route.getStartLocation()[0]!=theRoad.getEndPoint2()[0] || route.getStartLocation()[1]!=theRoad.getEndPoint2()[1] || !route.hasProperSegments()) && common3[0]==theRoad.getEndPoint2()[0] && common3[1]==theRoad.getEndPoint2()[1]) && 
			        					((route.getStartLocation()[0]!=theRoad.getEndPoint1()[0] || route.getStartLocation()[1]!=theRoad.getEndPoint1()[1] || !route.hasProperSegments()) && common3[0]==theRoad.getEndPoint1()[0] && common3[1]==theRoad.getEndPoint1()[1]))
			        				return false;
			        		}
			        		else {
			        			Route theRoute = (Route) segments.get(i-1);
			        			if((route.getStartLocation()[0]!=theRoute.getEndLocation()[0] || route.getStartLocation()[1]!=theRoute.getEndLocation()[1] || !route.hasProperSegments()))
			        				return false;
			        		}
		        		}
	        		}
	        		if(i==segments.size()-1) {
	        			if((route.getEndLocation()[0]!=getEndLocation()[0] || route.getEndLocation()[1]!=getEndLocation()[1] || !route.hasProperSegments())) {
	        				return false;
	        			}	
	        		}
	        	}
	        	i++;
	        }
    	}
        return true;
    }

    /**
     * Return the number of segments in this route.
     * 
     * @return  The total number of segments collected in this route.
     *        | result ==
     *        |   card({segment:segments | hasAsSegment(segment)})
     */
    public int getNbSegments() {
        return segments.size();
    }
    
    /**
     * Return the total length of this route.
     * 
     * @return  The total length of this route.
     * 		  |	result == 
     * 		  | sum({{connection in getSegments(): connection.getLength()}})
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid.
     * 		|  !new.ProperSegments()
     */
    public int getTotalLength() throws IllegalStateException {
		if(!hasProperSegments()) {
			throw new IllegalStateException();
		}
    	int sum = 0;
    	for (Segment segment : segments) {
    		if(segment instanceof Road) {
    			Road road = (Road) segment;
    			sum=sum+road.getLength();
    		}
    		else if(segment instanceof Route) {
    			Route route = (Route) segment;
    			sum=sum+route.getTotalLength();
    		}
    	}
    	return sum;
    } 
    
	/**
	 * Return the start location of this route.
	 */
	@Basic @Raw
	public double[] getStartLocation() {
		return this.startLocation;
	}
	
	/**
	 * Return the end location of this route.
	 * 
	 * @return The end location of this route.
	 * 		 | if(segments.size()==0)
	 * 		 |	then result == null
	 * 		 | else
	 * 		 | 	if(segments.get(segments.size()-1).getEndPoint1()[0]==segments.get(segments.size()-1).getEndPoint2()[0] && 
	 *		 |		segments.get(segments.size()-1).getEndPoint1()[1]==segments.get(segments.size()-1).getEndPoint2()[1])
	 *		 |		then result == segments.get(segments.size()-1).getEndPoint1()
     *       |  else
     *       |		if(segments.size()==1)
     *       |			if(segments.get(0).getEndPoint1()[0]==startLocation[0] && segments.get(0).getEndPoint1()[1]==startLocation[1]) 
     *       |				then result == segments.get(0).getEndPoint2()
     *       |			else 
     *       | 				then result == segments.get(0).getEndPoint1()
     *       |		else
     *       |			if((segments.get(segments.size()-1).getEndPoint1()[0]==segments.get(segments.size()-2).getEndPoint1()[0] && 
	 *		 |			segments.get(segments.size()-1).getEndPoint1()[1]==segments.get(segments.size()-2).getEndPoint1()[1]) || 
	 *		 |				(segments.get(segments.size()-1).getEndPoint1()[0]==segments.get(segments.size()-2).getEndPoint2()[0] && 
	 *		 |				segments.get(segments.size()-1).getEndPoint1()[1]==segments.get(segments.size()-2).getEndPoint2()[1]))
     *       |				then result == segments.get(segments.size()-1).getEndPoint2()
     *       | 			else
     *       |				then result == segments.get(segments.size()-1).getEndPoint1()
     *       
	 */
	@Basic @Raw
	public double[] getEndLocation() {
		if(segments.size()==0) {
			return null;
		}
		else {
			if(segments.get(segments.size()-1) instanceof Road) {
				Road road = (Road) segments.get(segments.size()-1);
				if(road.getEndPoint1()[0]==road.getEndPoint2()[0] && 
						road.getEndPoint1()[1]==road.getEndPoint2()[1]) {
					return road.getEndPoint1();
				}
				else {
					if(segments.size()==1) {
						if(segments.get(0) instanceof Road) {
							Road theRoad1 = (Road) segments.get(0);
							if(theRoad1.getEndPoint1()[0]==startLocation[0] && theRoad1.getEndPoint1()[1]==startLocation[1]) {
								return theRoad1.getEndPoint2();
							}
							else {
								return theRoad1.getEndPoint1();
							}
						}
						else {
							Route theRoute1 = (Route) segments.get(0);
							return theRoute1.getEndLocation();
						}
					}
					else {
						if(segments.get(segments.size()-2) instanceof Road) {
							Road theRoad2 = (Road) segments.get(segments.size()-2);
							if(road instanceof OneWayRoad)
								return road.getEndPoint2();
							else {
								if(theRoad2 instanceof OneWayRoad) {
									if(road.getEndPoint1()[0]==theRoad2.getEndPoint2()[0] && 
											road.getEndPoint1()[1]==theRoad2.getEndPoint2()[1]) {
										return road.getEndPoint2();
									}
									else {
										return road.getEndPoint1();
									}
								}
								else {	
									if(road.getEndPoint1()[0]==theRoad2.getEndPoint1()[0] && 
											road.getEndPoint1()[1]==theRoad2.getEndPoint1()[1] && (road.getEndPoint2()[0]!=theRoad2.getEndPoint2()[0] || 
											road.getEndPoint2()[1]!=theRoad2.getEndPoint2()[1])) {
										return road.getEndPoint2();
									}
									else if(road.getEndPoint1()[0]==theRoad2.getEndPoint1()[0] && 
											road.getEndPoint1()[1]==theRoad2.getEndPoint1()[1]) {
										Segment[] segmentsTest = new Segment[segments.size()-1];
										for(int i=0;i<segments.size()-1;i++) {
											segmentsTest[i]=segments.get(i);
										}
										Route test = new Route(startLocation, segmentsTest);
										if(test.getEndLocation()[0]==road.getEndPoint1()[0] && test.getEndLocation()[1]==road.getEndPoint1()[1])
											return road.getEndPoint2();
										else {
											return road.getEndPoint1();
										}
									}
									else if(road.getEndPoint2()[0]==theRoad2.getEndPoint1()[0] && 
											road.getEndPoint2()[1]==theRoad2.getEndPoint1()[1] && (road.getEndPoint1()[0]!=theRoad2.getEndPoint2()[0] || 
											road.getEndPoint1()[1]!=theRoad2.getEndPoint2()[1])){
										return road.getEndPoint1();
									}
									else if(road.getEndPoint2()[0]==theRoad2.getEndPoint1()[0] && 
											road.getEndPoint2()[1]==theRoad2.getEndPoint1()[1]) {
										Segment[] segmentsTest = new Segment[segments.size()-1];
										for(int i=0;i<segments.size()-1;i++) {
											segmentsTest[i]=segments.get(i);
										}
										Route test = new Route(startLocation, segmentsTest);
										if(test.getEndLocation()[0]==road.getEndPoint1()[0] && test.getEndLocation()[1]==road.getEndPoint1()[1])
											return road.getEndPoint2();
										else {
											return road.getEndPoint1();
										}
									}
									else if(road.getEndPoint1()[0]==theRoad2.getEndPoint2()[0] && 
											road.getEndPoint1()[1]==theRoad2.getEndPoint2()[1] && (road.getEndPoint2()[0]!=theRoad2.getEndPoint1()[0] || 
											road.getEndPoint2()[1]!=theRoad2.getEndPoint1()[1])) {
										return road.getEndPoint2();
									}
									else if(road.getEndPoint1()[0]==theRoad2.getEndPoint2()[0] && 
											road.getEndPoint1()[1]==theRoad2.getEndPoint2()[1]) {
										Segment[] segmentsTest = new Segment[segments.size()-1];
										for(int i=0;i<segments.size()-1;i++) {
											segmentsTest[i]=segments.get(i);
										}
										Route test = new Route(startLocation, segmentsTest);
										if(test.getEndLocation()[0]==road.getEndPoint1()[0] && test.getEndLocation()[1]==road.getEndPoint1()[1])
											return road.getEndPoint2();
										else {
											return road.getEndPoint1();
										}
									}
									else if(road.getEndPoint2()[0]==theRoad2.getEndPoint2()[0] && 
											road.getEndPoint2()[1]==theRoad2.getEndPoint2()[1] && (road.getEndPoint1()[0]!=theRoad2.getEndPoint1()[0] || 
											road.getEndPoint1()[1]!=theRoad2.getEndPoint1()[1])){
										return road.getEndPoint1();
									}
									else {
										Segment[] segmentsTest = new Segment[segments.size()-1];
										for(int i=0;i<segments.size()-1;i++) {
											segmentsTest[i]=segments.get(i);
										}
										Route test = new Route(startLocation, segmentsTest);
										if(test.getEndLocation()[0]==road.getEndPoint1()[0] && test.getEndLocation()[1]==road.getEndPoint1()[1])
											return road.getEndPoint2();
										else {
											return road.getEndPoint1();
										}
									}
								}
							}
						}
						else {
							Route theRoute2 = (Route) segments.get(segments.size()-2);
							if(road instanceof OneWayRoad) {
								return road.getEndPoint2();
							}
							else {
								if(theRoute2.getEndLocation()[0]==road.getEndPoint1()[0] && theRoute2.getEndLocation()[1]==road.getEndPoint1()[1]) {
									return road.getEndPoint2();
								}
								else {
									return road.getEndPoint1();
								}
							}
						}
					}
				}
			}
			else {
				Route route = (Route) segments.get(segments.size()-1);
				return route.getEndLocation();
			}
			
		}
	}
	
	/**
	 * Check if this route is traversable.
	 * 
	 * @return True if and only if all roads in the sequence of segments of this route 
	 * 		   are unblocked in the direction of the route, from the start location
	 * 		   to the end location.
	 * 		 | result == 
	 * 		 | for (Road road : segments || Road road : segment : segments)
	 * 		 |  if(road==segments.get(0))
	 * 		 |		if(road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1])
	 * 		 |			then road.getBlockedForth()==false
	 * 		 |		if(road.getEndPoint2()[0]==startLocation[0] && road.getEndPoint2()[1]==startLocation[1])
	 * 		 |			then road.getBlockedOpposite()==false
	 * 		 |  else
	 * 		 | 		if(road.getEndPoint1()==segments.get(segments.indexOf(road)-1).getEndPoint1() || road.getEndPoint1()==segments.get(segments.indexOf(road)-1).getEndPoint2())
	 * 		 |   		then road.getBlockedForth()==false
	 * 		 |  	if(road.getEndPoint2()==segments.get(segments.indexOf(road)-1).getEndPoint1() || road.getEndPoint2()==segments.get(segments.indexOf(road)-1).getEndPoint2())
	 * 		 |  		then road.getBlockedOpposite()==false
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid.
     * 		|  !new.ProperSegments()
     * 
	 */
	public boolean isTraversable() throws IllegalStateException {
		if(!hasProperSegments()) {
			throw new IllegalStateException();
		}
		for(Segment segment : segments) {
			if(segment instanceof Route) {
				Route route = (Route) segment;
				if(!route.hasProperSegments()) {
					throw new IllegalStateException();
				}
			}
		}
		for (Segment segment : segments) {
			if(segment instanceof Road) {
				Road road = (Road) segment;
				if(road==segments.get(0)) {
					if(road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1]) {
						if(road.getBlockedForth())
							return false;
					}
					if(road.getEndPoint2()[0]==startLocation[0] && road.getEndPoint2()[1]==startLocation[1]) {
						if(road.getBlockedOpposite())
							return false;
					}
				}
				else {
					if(segments.get(segments.indexOf(road)-1) instanceof Road){
						Road theRoad = (Road) segments.get(segments.indexOf(road)-1);
						if((road.getEndPoint1()[0]==theRoad.getEndPoint1()[0] && road.getEndPoint1()[1]==theRoad.getEndPoint1()[1]) || 
								(road.getEndPoint1()[0]==theRoad.getEndPoint2()[0] && road.getEndPoint1()[1]==theRoad.getEndPoint2()[1])) {
							if(road.getBlockedForth())
								return false;
						}
						if((road.getEndPoint2()[0]==theRoad.getEndPoint1()[0] && road.getEndPoint2()[1]==theRoad.getEndPoint1()[1]) || 
								(road.getEndPoint2()[0]==theRoad.getEndPoint2()[0] && road.getEndPoint2()[1]==theRoad.getEndPoint2()[1])) {
							if(road.getBlockedOpposite()) {
								return false;
							}
						}
					}
					else {
						Route theRoute = (Route) segments.get(segments.indexOf(road)-1);
						if(road.getEndPoint1()[0]==theRoute.getEndLocation()[0] && road.getEndPoint1()[1]==theRoute.getEndLocation()[1]) {
							if(road.getBlockedForth())
								return false;
						}
						if(road.getEndPoint2()[0]==theRoute.getEndLocation()[0] && road.getEndPoint2()[1]==theRoute.getEndLocation()[1]) {
							if(road.getBlockedOpposite()) {
								return false;
							}
						}
					}
				}
			}
			else {
				Route route = (Route) segment;
				if(!route.isTraversable()) {
					return false;
				}
			}
				
		}
		return true;
			
	}

	/**
	 * Return the sequence of locations visited by a route when traveled from the start location
	 * to the end location.
	 * 
	 * @return The sequence of locations visited by a route when traveled from the start location 
	 * 		   to the end location.
	 * 	     | for(Road road : segments || Road road : segment : segments)
	 * 		 | 	result.contains(road.getEndPoint1()) && result.contains(road.getEndPoint2())
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid.
     * 		|  !new.ProperSegments()
	 */
	public ArrayList<double[]> getLocationsVisited() throws IllegalStateException {
		if(!hasProperSegments() && getNbSegments()!=0) {
			throw new IllegalStateException();
		}
		ArrayList<double[]> locations = new ArrayList<double[]>();
		if(segments.size()==0) {
			locations.add(startLocation);
			return locations;
		}
		if(segments.size()==1) {
			locations.add(startLocation);
			locations.add(getEndLocation());
			return locations;
		}
		double [] lastAdded = new double[] {};
		int i=0;
		int j=0;
		for (Segment segment : segments) {
			if(segment instanceof Road) {
				Road road = (Road) segment;
				if(road.getEndPoint1()[0]==startLocation[0] && road.getEndPoint1()[1]==startLocation[1] && i==0) {
					locations.add(startLocation);
					locations.add(road.getEndPoint2());
					lastAdded=road.getEndPoint2();
					j++;
				}
				else if(road.getEndPoint2()[0]==startLocation[0] && road.getEndPoint2()[1]==startLocation[1] && i==0) {
					locations.add(startLocation);
					locations.add(road.getEndPoint1());
					lastAdded=road.getEndPoint1();
					j++;	
				}
				if(i>0 && segments.get(i-1) instanceof Road) {
					Road theRoad = (Road) segments.get(i-1);
					if((road.getEndPoint2()[0]!=lastAdded[0] || road.getEndPoint2()[1]!=lastAdded[1]) && i>0 && i!=segments.size()-1 && j<2 && ((road.getEndPoint2()[0]==theRoad.getEndPoint1()[0] && road.getEndPoint2()[1]==theRoad.getEndPoint1()[1]) ||
							(road.getEndPoint2()[0]==theRoad.getEndPoint2()[0] && road.getEndPoint2()[1]==theRoad.getEndPoint2()[1]))) {
						locations.add(road.getEndPoint2());
						lastAdded=road.getEndPoint2();
						j++;
					}
					else if((road.getEndPoint1()[0]!=lastAdded[0] || road.getEndPoint1()[1]!=lastAdded[1]) && i>0 && i!=segments.size()-1 && j<2 && ((road.getEndPoint1()[0]==theRoad.getEndPoint1()[0] && road.getEndPoint1()[1]==theRoad.getEndPoint1()[1]) || 
							(road.getEndPoint1()[0]==theRoad.getEndPoint2()[0] && road.getEndPoint1()[1]==theRoad.getEndPoint2()[1]))) {
						locations.add(road.getEndPoint1());
						lastAdded=road.getEndPoint1();
						j++;
					}
				}
				else if(i>0 && segments.get(i-1) instanceof Route){
					Route theRoute= (Route) segments.get(i-1);
					if((road.getEndPoint2()[0]!=lastAdded[0] || road.getEndPoint2()[1]!=lastAdded[1]) && i>0 && i!=segments.size()-1 && j<2 && (road.getEndPoint2()[0]==theRoute.getEndLocation()[0] && road.getEndPoint2()[1]==theRoute.getEndLocation()[1])) {
						locations.add(road.getEndPoint2());
						lastAdded=road.getEndPoint2();
						j++;
					}
					else if((road.getEndPoint1()[0]!=lastAdded[0] || road.getEndPoint1()[1]!=lastAdded[1]) && i>0 && i!=segments.size()-1 && j<2 && (road.getEndPoint1()[0]==theRoute.getEndLocation()[0] && road.getEndPoint1()[1]==theRoute.getEndLocation()[1])) {
						locations.add(road.getEndPoint1());
						lastAdded=road.getEndPoint1();
						j++;
					}			
				}
				if(i==segments.size()-1 && road.getEndPoint1()[0]==getEndLocation()[0] && road.getEndPoint1()[1]==getEndLocation()[1] && segments.get(i-1) instanceof Road) {
					locations.add(road.getEndPoint2());
					locations.add(road.getEndPoint1());
					lastAdded=road.getEndPoint1();
				}
				else if(i==segments.size()-1 && road.getEndPoint2()[0]==getEndLocation()[0] && road.getEndPoint2()[1]==getEndLocation()[1] && segments.get(i-1) instanceof Road) {
					locations.add(road.getEndPoint1());
					locations.add(road.getEndPoint2());	
					lastAdded=road.getEndPoint2();
				}
				else if(i==segments.size()-1 && road.getEndPoint1()[0]==getEndLocation()[0] && road.getEndPoint1()[1]==getEndLocation()[1] && segments.get(i-1) instanceof Route) {
					locations.add(road.getEndPoint1());
					lastAdded=road.getEndPoint1();
				}
				else if(i==segments.size()-1 && road.getEndPoint2()[0]==getEndLocation()[0] && road.getEndPoint2()[1]==getEndLocation()[1] && segments.get(i-1) instanceof Route) {
					locations.add(road.getEndPoint2());	
					lastAdded=road.getEndPoint2();
				}
			}
			else {
				Route route = (Route) segment;
				if(i==0) {
					for(int z=0;z<=route.getLocationsVisited().size()-1;z++) {	
						locations.add(route.getLocationsVisited().get(z));
					}
					lastAdded=locations.get(locations.size()-1);
				}
				else {
					for(int z=1;z<=route.getLocationsVisited().size()-1;z++) {	
						locations.add(route.getLocationsVisited().get(z));
					}
					lastAdded=locations.get(locations.size()-1);
				}
			}
			j=0;
			i++;
		}
		return locations;
	}
	
    /**
     * Add the given segment to the sequence of segments registered in
     * this route as the last segment.
     * 
     * @param   segment
     *          The segment to be added.
     * @pre     The given segment is effective.
     *        | segment != null
     * @pre     This route can have the given segment as the last segment of its sequence of segments.
     * 		  |	canHaveAsSegment(segment)
     * @post    The given segment is registered as one of the
     *          segments for this route.
     *        | new.hasAsSegment(segment)
     * @post    The size of the sequence of segments of this route is incremented by 1.
     *        | new.getNbSegments()==getNbSegments()+1
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid anymore.
     * 		|  !new.ProperSegments()
     */
    void addSegment(@Raw
    Segment segment) throws IllegalStateException{
        assert segment != null;
        assert canHaveAsSegment(segment);
        try {
        	segments.add(segment);
	        if(segment instanceof Route) {
	        	Route route=(Route) segment;
	        	route.listOfRoutes.add(this);
	        }
	        else {
	        	Road road=(Road) segment;
	        	road.addRoute(this);
	        }
        	if(!hasProperSegments()) {
        		throw new IllegalStateException();
        	}
        } catch (IllegalStateException exc) {
        	assert false;
        }
    }

    /**
     * Remove the first occurrence of the given segment from the sequence of segments registered in
     * this route.
     * 
     * @param   segment
     *          The segment to be removed.
     * @pre     The given segment is registered as one of the segments
     *          for this route.
     *        | hasAsSegment(segment)
     * @post    If the sequence of segments of this route contained exactly one time the given segment,
     * 			this route no longer has the given segment as one of its segments.
     * 		  | if(Collections.frequency(segments,segment)==1)
     *        | 	then !new.hasAsSegment(segment)
     * @post    The size of the sequence of segments of this route is decremented by 1.
     * 	 	  | new.getNbSegments()==getNbSegments()-1
     * @post   All segments registered at an index beyond the index at
	 *         which the removed segment was registered, are shifted
	 *         one position to the left.
	 *       | for each I,J in 1..getNbSegments():
	 *       |   if ( (getSegmentAt(I) == getSegmentAt(segments.indexOf(segment))) and (I < J) )
	 *       |     then new.getSegmentAt(J-1) == getSegmentAt(J)
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid anymore.
     * 		|  !new.ProperSegments()
     */
    void removeSegment(@Raw
    Segment segment) throws IllegalStateException {
        assert hasAsSegment(segment);
        try {
        	if(segment instanceof Road) {
        		Road road = (Road) segment;
        		segments.remove(segment);
        		road.removeRoute(this);
        	}
        	else {
        		Route route = (Route) segment;
        		segments.remove(segment);
        		route.listOfRoutes.remove(this);
        	}
        	if(!hasProperSegments() && getNbSegments()!=0) {
        		throw new IllegalStateException();
        	}
        } catch(IllegalStateException exc) {
        	assert false;
        }
    } 

    /**
     * Remove the segment at the given index from the sequence of segments registered in
     * this route.
     * 
     * @param   index
     *          The position of the segment to be removed from the sequence of segments of this route.
     * @pre     The given index is a valid index for the sequence of segments of this route.
     * 		  | index<segments.size() && index>=0
     * @post    If the sequence of segments of this route contained exactly one time the segment at the given index,
     * 			this route no longer has the segment at the given index as one of its segments.
     * 		  | if(Collections.frequency(segments,segments.get(index))==1)
     *        | 	then !new.hasAsSegment(segments.get(index))
     * @post    The size of the sequence of segments of this route is decremented by 1.
     * 	 	  | new.getNbSegments()==getNbSegments()-1
     * @post   All segments registered at an index beyond the index at
	 *         which the removed segment was registered, are shifted
	 *         one position to the left.
	 *       | for each I,J in 1..getNbSegments():
	 *       |   if ( (getSegmentAt(I) == getSegmentAt(index)) and (I < J) )
	 *       |     then new.getSegmentAt(J-1) == getSegmentAt(J)
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid anymore.
     * 		|  !new.ProperSegments()
     */
    void removeSegment(@Raw
    int index) throws IllegalStateException {
        assert (index<segments.size() && index >=0);
        try {
        	if(segments.get(index) instanceof Road) {
        		Road road = (Road) segments.get(index);
        		segments.remove(segments.get(index));
        		road.removeRoute(this);
        	}
        	else {
        		Route route = (Route) segments.get(index);
        		segments.remove(segments.get(index));
        		route.listOfRoutes.remove(this);
        	}
        	if(!hasProperSegments() && getNbSegments()!=0) {
        		throw new IllegalStateException();
        	}
        } catch(IllegalStateException exc) {
        	assert false;
        }
    }
    
    /**
     * Replace the first occurrence of the given old segment in the sequence of segments 
     * of this route by the given new segment, keeping the same position.
     * 
     * @param index
     * 		  The index of the segment to be replaced in the segments of this route.
     * @param newSegment
     * 		  The segment to be added to the segments of this route instead of the segment at the given index.
     * @pre   The given index is a valid index for the sequence of segments of this route.
     * 		| index<segments.size() && index >=0
     * @pre   The segment at the given index and the given new segment has the same end points or
     * 		  opposite end points.
     * 		| (segments.get(index).getEndPoint1()==newSegment.getEndPoint1() || segments.get(index).getEndPoint1()==newSegment.getEndPoint2())
     *      | && (segments.get(index).getEndPoint2()==newSegment.getEndPoint1() || segments.get(index).getEndPoint2()==newSegment.getEndPoint2()) 
     * @pre   The given new segment must be effective
     * 		| newSegment != null
     * @post  If the sequence of segments of this route contained exactly one time the segment at the given index,
     * 		  this route no longer has the segment at the given index as one of its segments.
     * 		| if(Collections.frequency(segments,segments.get(index))==1)
     *      | 	then !new.hasAsSegment(segments.get(index))
     * @post  The given new segment is registered as one of the
     *        segments for this route.
     *      | new.hasAsSegment(newSegment)
     * @Throws IllegalStateException
     * 		   The segments of this route are not valid anymore.
     * 		|  !new.ProperSegments()
     */
    void changeSegment(@Raw
    int index, 	Segment newSegment) throws IllegalStateException {
    	assert index<segments.size() && index>=0;
        assert newSegment != null;
    	try {
        	if(segments.get(index) instanceof Road) {
        		Road road = (Road) segments.get(index);
        		segments.set(index,newSegment);
        		road.removeRoute(this);
    	        if(newSegment instanceof Route) {
    	        	Route theRoute=(Route) newSegment;
    	        	theRoute.listOfRoutes.add(this);
    	        }
    	        else {
    	        	Road theRoad=(Road) newSegment;
    	        	theRoad.addRoute(this);
    	        }
        	}
        	else {
        		Route route = (Route) segments.get(index);
        		segments.set(index,newSegment);
        		route.listOfRoutes.remove(this);
    	        if(newSegment instanceof Route) {
    	        	Route theRoute=(Route) newSegment;
    	        	theRoute.listOfRoutes.add(this);
    	        }
    	        else {
    	        	Road theRoad=(Road) newSegment;
    	        	theRoad.addRoute(this);
    	        }
        	}
    		if(!hasProperSegments()) {
    			throw new IllegalStateException();
    		}
    	} catch(IllegalStateException exc) {
    		assert false;
    	}
    } 

	/**
	 * Check whether this route has the given segment
	 * as one of its subsegments.
	 *
	 * @return True if and only if the given segment is the same
	 *         segment as this route, or if the segments of this route
	 *         contain the given segment.
	 *       | result ==
	 *       |     (segment == this)
	 *       |  || (segments.contains(segment))
	 */
	@Override
	public boolean hasAsSubSegment(Segment segment) {
		if(segment instanceof Route) {
			if (segment == this)
				return true;
			if(segments.contains(segment))
				return true;
		}
		return false;
	}
	
}
