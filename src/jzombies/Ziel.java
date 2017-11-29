package jzombies;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class Ziel {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private boolean visited;
	
	public Ziel(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
		visited = false;
	}
	
	public void visited() {
		visited = true;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
}
