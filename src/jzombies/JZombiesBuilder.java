package jzombies;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;


	public class JZombiesBuilder implements ContextBuilder<Object> {
		
		@Override
		public Context build(Context<Object> context){
			context.setId("jzombies");
			
		
			ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
			ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, new SimpleCartesianAdder<>(), new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);
			
			GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
			
			Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, 50,50));
			
			// Ziele erzeugen
			Ziel ziel = new Ziel(space, grid);
			context.add(ziel);
			space.moveTo(ziel,  (int) 10, (int) 15);
			grid.moveTo(ziel,  (int) 10, (int) 15);
			
			ziel = new Ziel(space, grid);
			context.add(ziel);
			space.moveTo(ziel,  (int) 10, (int) 35);
			grid.moveTo(ziel,  (int) 10, (int) 35);
			
			ziel = new Ziel(space, grid);
			context.add(ziel);
			space.moveTo(ziel,  (int) 30, (int) 45);
			grid.moveTo(ziel,  (int) 30, (int) 45);
			
			// Boten erzeugen
			int energy = 1;
			Human bote = new Human(space, grid, energy);
			context.add(bote);
			space.moveTo(bote, (int) 5, (int) 5);
			grid.moveTo(bote, (int) 5, (int) 5);
			
			// Boten die Ziele mitteilen
			ArrayList<Ziel> ziele = new ArrayList<Ziel>();
			for (Object obj : context) {
				if (obj instanceof Ziel) {
					ziele.add((Ziel)obj);
				}
			}
			
			bote.addZiele(ziele);
			
			// Roboter erzeugen
			Roboter bot = new Roboter(space, grid);
			context.add(bot);
			space.moveTo(bot, (int) 4, (int)4);
			grid.moveTo(bot, (int) 4, (int)4);
			
			return context;
		}

	}


