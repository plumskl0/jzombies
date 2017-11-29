/**
 * 
 */
package jzombies;


import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.*;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;


//bis inkl. Seite 7, next Human class
public class Roboter {
	private ContinuousSpace <Object> space ;
	private Grid <Object> grid ;
	private boolean moved;
	 
	
	public Roboter (ContinuousSpace <Object > space , Grid <Object > grid ) {
	 this.space = space ;
	 this.grid = grid;
	 }

	
	//@Watch (watcheeClassName = "jzombies.Bote", watcheeFieldNames = "moved", query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void step(){
		GridPoint pt = grid.getLocation(this);
		
		GridCellNgh<Human> nghCreator = new GridCellNgh<Human>(grid, pt, Human.class, 1, 1);
		
		List<GridCell<Human>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		
		GridPoint pointWithMostHumans = null;
		int maxCount = -1;
		for (GridCell<Human> cell : gridCells) {
			if (cell.size() > maxCount){
				pointWithMostHumans = cell.getPoint();
				maxCount = cell.size();
			}
		}
		
		moveTowards(pointWithMostHumans);
		infect();
	}
	
	public void moveTowards(GridPoint pt){
		if (!pt.equals(grid.getLocation(this))){
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			
			moved = true;
			
		}
	}
	
	public void infect (){
		GridPoint pt = grid.getLocation(this);
		List<Object> humans = new ArrayList<Object>();
		for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY())){
			if(obj instanceof Human){
				humans.add(obj);
			}
		}
		
		if (humans.size() > 0){
			int index = RandomHelper.nextIntFromTo(0, humans.size()-1);
			Object obj = humans.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			context.remove(obj);
			Roboter zombie = new Roboter(space, grid);
			context.add(zombie);
			space.moveTo(zombie, spacePt.getX(), spacePt.getY());
			grid.moveTo(zombie, pt.getX(), pt.getY());
			
			Network<Object> net = (Network<Object>)context.getProjection("infection network");
			net.addEdge(this, zombie);
			
			
			
		}
	}
}
