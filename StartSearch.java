import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author rdong26
 *This class determines cupid's route
 */
public class StartSearch {
	private static final String[][] String = null;
	private Map targetMap;
	private int numArrows;
	private int inertia;
	private int direction;
	
	/**
	 * 
	 * @param filename --> The name of the file with cupid's map
	 */
	public StartSearch(String filename) {
		try {
			targetMap = new Map(filename);
		} catch (InvalidMapException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The main method tries to find a path from cupid to the targets
	 * @param args --> The name of the file with cupid's route
	 */
	public static void main (String[] args) {
		if (args.length < 1) {
			System.out.println("You must provide the name of the input file");
			System.exit(0);
		}
		String mapFileName = args[0];
		if (args.length > 1) {
			int maxPathLength = Integer.parseInt(args[1]);
		}
	
		StartSearch obj = new StartSearch(mapFileName);
		obj.numArrows = obj.targetMap.quiverSize();
		obj.inertia=0;
		int targetsFound = 0;
		int cellsTravelled = 0;
		int backtrack=0;
		boolean restart=false;
		ArrayStack<MapCell> mapArray = new ArrayStack<MapCell>();
		
		//while cupid still has arrows
		while (obj.numArrows > 0) {
			mapArray.push(obj.targetMap.getStart());
			mapArray.peek().markInStack();
			MapCell currCell = mapArray.peek();
			currCell = obj.nextCell(currCell);
			restart=false;
			while (restart==false) {
				//while there is a cell
				if (currCell !=null) {
					mapArray.push(currCell);
					cellsTravelled+=1;
					//if the cell is the target cell
					if (currCell.isTarget()) {
						mapArray.peek().markInStack();
						targetsFound+=1;
						for (int i=0;i<cellsTravelled-1;i++) {
							mapArray.pop();
						}
						mapArray.peek().markOutStack();
						mapArray.pop();
						mapArray.pop();
						obj.numArrows-=1;
						obj.inertia=0;
						backtrack=0;
						cellsTravelled=0;
						restart=true;	
					//if the cell is not the target cell
					}else {
						currCell = obj.nextCell(currCell);
					}
				//if the cell is null
				} else {
					//if the maximum backtrack hsa been reached
					if ((backtrack == 3)&&(obj.inertia<3)) {
						cellsTravelled-=1;
						for (int i=0;i<cellsTravelled;i++) {
							mapArray.pop();
						}
						mapArray.peek().markOutStack();
						mapArray.pop();
						mapArray.pop();
						cellsTravelled=0;
						obj.numArrows-=1;
						obj.inertia=0;
						restart=true;
					//if the inertia has been reached and cannot turn
					}else if (obj.inertia>=3) {
						mapArray.peek().markInStack();
						for (int i=1;i<cellsTravelled;i++) {
							mapArray.pop();
						}
						mapArray.peek().markOutStack();
						mapArray.pop();
						mapArray.pop();
						obj.numArrows-=1;
						obj.inertia=0;
						backtrack=0;
						cellsTravelled=0;
						restart=true;
					} else {
						//if the cell is the starting cell
						if (mapArray.peek()==obj.targetMap.getStart()) {
							obj.numArrows-=1;
							cellsTravelled=0;
							mapArray.pop();
							restart=true;
						//backtracking
						} else {
							backtrack+=1;
							cellsTravelled-=1;
							mapArray.pop();
							if ((mapArray.peek().isCrossPath())||mapArray.peek().isVerticalPath()) {
								obj.direction=0;
							} else {
								obj.direction=1;
							}
							currCell = obj.nextCell(mapArray.peek());
						}
					}
					
				}
			}
		}
		System.out.print(targetsFound);
	}
	
	/**
	 * 
	 * @param cell --> The cell the arrow is currently at
	 * @return MapCell next cell the arrow will go to or null if none available
	 */
	public MapCell nextCell(MapCell cell) {
		//go in same direction
		//cross path to cross path
		if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(direction).isCrossPath())) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		//cross path to straight path
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isCrossPath()) && ((direction==0||direction==2)&&(cell.getNeighbour(direction).isVerticalPath()))) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isCrossPath()) && ((direction==1||direction==3)&&(cell.getNeighbour(direction).isHorizontalPath()))) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		//vertical to vertical
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isVerticalPath()) && (cell.getNeighbour(direction).isVerticalPath())) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		//vertical to cross
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isVerticalPath()) && (cell.getNeighbour(direction).isCrossPath())) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		//horizontal to horizontal
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isHorizontalPath()) && (cell.getNeighbour(direction).isHorizontalPath())) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		//horizontal to cross
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false)&&(cell.isHorizontalPath()) && (cell.getNeighbour(direction).isCrossPath())) {
			cell.getNeighbour(direction).markInStack();
			inertia+=1;
			return cell.getNeighbour(direction);
		//if it is the startcell
		//if startcell is crosspath
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(0).isCrossPath())) {
			cell.getNeighbour(0).markInStack();
			direction=0;
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(1).isCrossPath())) {
			cell.getNeighbour(1).markInStack();
			direction=1;
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(2).isCrossPath())) {
			cell.getNeighbour(2).markInStack();
			direction=2;
			return cell.getNeighbour(2);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(3).isCrossPath())) {
			cell.getNeighbour(3).markInStack();
			direction=3;
			return cell.getNeighbour(3);
		//if it is the target
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false) && (cell.getNeighbour(direction).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isVerticalPath())&&((direction==0)||(direction==2))))) {
			cell.getNeighbour(direction).markTarget();
			return cell.getNeighbour(direction);
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false) && (cell.getNeighbour(direction).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isHorizontalPath())&&((direction==1)||(direction==3))))) {
			cell.getNeighbour(direction).markTarget();
			return cell.getNeighbour(direction);
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false) && (cell.getNeighbour(direction).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isVerticalPath())&&((direction==0)||(direction==2))))) {
			cell.getNeighbour(direction).markTarget();
			return cell.getNeighbour(direction);
		}else if ((cell.getNeighbour(direction)!=null) && (cell.getNeighbour(direction).isBlackHole()==false) && (cell.getNeighbour(direction).isMarked()==false) && (cell.getNeighbour(direction).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isHorizontalPath())&&((direction==1)||(direction==3))))) {
			cell.getNeighbour(direction).markTarget();
			return cell.getNeighbour(direction);
		//if startcell is vertical or horizontalpath
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(0).isVerticalPath())) {
			cell.getNeighbour(0).markInStack();
			direction=0;
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(1).isHorizontalPath())) {
			cell.getNeighbour(1).markInStack();
			direction=1;
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(2).isVerticalPath())) {
			cell.getNeighbour(2).markInStack();
			direction=2;
			return cell.getNeighbour(2);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false)&&(cell.isStart()) && (cell.getNeighbour(3).isHorizontalPath())) {
			cell.getNeighbour(3).markInStack();
			direction=3;
			return cell.getNeighbour(3);
		//if next cell is target
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false) && (cell.getNeighbour(0).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isVerticalPath())&&((direction==0)||(direction==2))))) {
			cell.getNeighbour(0).markTarget();
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false) && (cell.getNeighbour(1).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isHorizontalPath())&&((direction==1)||(direction==3))))) {
			cell.getNeighbour(1).markTarget();
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false) && (cell.getNeighbour(2).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isVerticalPath())&&((direction==0)||(direction==2))))) {
			cell.getNeighbour(2).markTarget();
			return cell.getNeighbour(2);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false) && (cell.getNeighbour(3).isTarget()) && ((cell.isStart())||(cell.isCrossPath())||((cell.isHorizontalPath())&&((direction==1)||(direction==3))))) {
			cell.getNeighbour(3).markTarget();
			return cell.getNeighbour(3);
		//cross path to cross path
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(0).isCrossPath())) {
			cell.getNeighbour(0).markInStack();
			direction=0;
			inertia=0;
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(1).isCrossPath())) {
			cell.getNeighbour(1).markInStack();
			direction=1;
			inertia=0;
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(2).isCrossPath())) {
			cell.getNeighbour(2).markInStack();
			direction=2;
			inertia=0;
			return cell.getNeighbour(2);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(3).isCrossPath())) {
			cell.getNeighbour(3).markInStack();
			direction=3;
			inertia=0;
			return cell.getNeighbour(3);
		//cross path to straight path
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(0).isVerticalPath())) {
			cell.getNeighbour(0).markInStack();
			direction=0;
			inertia=0;
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(1).isHorizontalPath())) {
			cell.getNeighbour(1).markInStack();
			direction=1;
			inertia=0;
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(2).isVerticalPath())) {
			cell.getNeighbour(2).markInStack();
			direction=2;
			inertia=0;
			return cell.getNeighbour(2);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false)&&(cell.isCrossPath()) && (cell.getNeighbour(3).isHorizontalPath())) {
			cell.getNeighbour(3).markInStack();
			direction=3;
			inertia=0;
			return cell.getNeighbour(3);
		//vertical to vertical
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false)&&(cell.isVerticalPath()) && (cell.getNeighbour(0).isVerticalPath())) {
			cell.getNeighbour(0).markInStack();
			direction=0;
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false)&&(cell.isVerticalPath()) && (cell.getNeighbour(2).isVerticalPath())) {
			cell.getNeighbour(2).markInStack();
			direction=2;
			return cell.getNeighbour(2);
		//vertical to cross
		}else if ((inertia<3)&&(cell.getNeighbour(0)!=null) && (cell.getNeighbour(0).isBlackHole()==false) && (cell.getNeighbour(0).isMarked()==false)&&(cell.isVerticalPath()) && (cell.getNeighbour(0).isCrossPath())) {
			cell.getNeighbour(0).markInStack();
			direction=0;
			return cell.getNeighbour(0);
		}else if ((inertia<3)&&(cell.getNeighbour(2)!=null) && (cell.getNeighbour(2).isBlackHole()==false) && (cell.getNeighbour(2).isMarked()==false)&&(cell.isVerticalPath()) && (cell.getNeighbour(2).isCrossPath())) {
			cell.getNeighbour(2).markInStack();
			direction=2;
			return cell.getNeighbour(2);
		//horizontal to horizontal
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false)&&(cell.isHorizontalPath()) && (cell.getNeighbour(1).isHorizontalPath())) {
			cell.getNeighbour(1).markInStack();
			direction=1;
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false)&&(cell.isHorizontalPath()) && (cell.getNeighbour(3).isHorizontalPath())) {
			cell.getNeighbour(3).markInStack();
			direction=3;
			return cell.getNeighbour(3);
		//horizontal to cross
		}else if ((inertia<3)&&(cell.getNeighbour(1)!=null) && (cell.getNeighbour(1).isBlackHole()==false) && (cell.getNeighbour(1).isMarked()==false)&&(cell.isHorizontalPath()) && (cell.getNeighbour(1).isCrossPath())) {
			cell.getNeighbour(1).markInStack();
			direction=1;
			return cell.getNeighbour(1);
		}else if ((inertia<3)&&(cell.getNeighbour(3)!=null) && (cell.getNeighbour(3).isBlackHole()==false) && (cell.getNeighbour(3).isMarked()==false)&&(cell.isHorizontalPath()) && (cell.getNeighbour(3).isCrossPath())) {
			cell.getNeighbour(3).markInStack();
			direction=3;
			return cell.getNeighbour(3);
		}else {
			return null;
		}
	}
}

