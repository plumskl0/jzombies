/**
 * 
 */
package jzombies;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

//ab Seite 8

public class Human {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	private GridPoint startPoint;
	private boolean moved;
	
	private ArrayList<Ziel> ziele;
	private Ziel aktZiel;
	
	
	public Human (ContinuousSpace <Object> space, Grid<Object> grid, int energy){
		this.space = space;
		this.grid = grid;
	}
	

	@ScheduledMethod(start = 1, interval = 1)
	public void run(){
		GridPoint pt = grid.getLocation(this);
		if (startPoint == null)
			startPoint = pt;
		System.out.println("Current Location:" + pt);
		if (ziele.size() == 0) {
			moveTowards(startPoint);
		} else if (aktZiel == null || aktZiel.isVisited()) {
			aktZiel = ziele.get(0);
		} else {
			moveTowards(grid.getLocation(aktZiel));
		}
	}

//movetowardsMethode noch
	
	public void moveTowards(GridPoint pt){
		if (!pt.equals(grid.getLocation(this))){
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(),(int)myPoint.getY());
			moved = true;
			checkArrived(pt);
		}
	}
	
	private void checkArrived(GridPoint pt) {
		GridCellNgh<Ziel> nghCreator = new GridCellNgh<Ziel>(grid, pt, Ziel.class, 1, 1);
		
		List<GridCell<Ziel>> gridCells = nghCreator.getNeighborhood(true);
		System.out.println(gridCells);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		
		for (GridCell<Ziel> cell : gridCells) {
			System.out.println("Cell Size: " + cell.size());
			if (cell.size() > 0) {
				aktZiel.visited();
				ziele.remove(0);
			}
		}
	}
	
	public void addZiele(ArrayList<Ziel> ziele) {
		this.ziele = ziele;
	}
}