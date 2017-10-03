package dumbStuff;
import java.util.ArrayList;
import java.util.Random;

public class Cell {
	public int x;
	public int y;
	public boolean v;
	static Random rand = new Random();
	public boolean walls[] = {true, true};//, false, false};//top left bottom right
	public Cell(int x_, int y_)
	{
		x = x_;
		y = y_;
		v = false;
	}
	public Cell unVneighbors(Cell g[][], int rows, int cols)
	{
		ArrayList<Cell> n = new ArrayList<Cell>();
		if(x>0 && !g[x-1][y].v)//left
			n.add(g[x-1][y]);
		if(x<cols-1 && !g[x+1][y].v)//right
			n.add(g[x+1][y]);
		if(y>0 && !g[x][y-1].v)//up
			n.add(g[x][y-1]);
		if(y<rows-1 && !g[x][y+1].v)//down
			n.add(g[x][y+1]);
		if(n.size()>0)
		{
			Cell m = n.get(rand.nextInt(n.size()));
			adjWalls(m);
			return m;
		}
		else
			return new Cell(-1, -1);
	}
	public Cell opWneighbors(Cell g[][], int rows, int cols)
	{
		ArrayList<Cell> n = new ArrayList<Cell>();
		if(x>0  && !g[x-1][y].v && !walls[1])//left
			n.add(g[x-1][y]);
		if(y>0 && !g[x][y-1].v && !walls[0])//up
			n.add(g[x][y-1]);
		if(x<cols-1 && !g[x+1][y].v && !g[x+1][y].walls[1])//right
			n.add(g[x+1][y]);
		if(y<rows-1 && !g[x][y+1].v && !g[x][y+1].walls[0])//down
			n.add(g[x][y+1]);
		if(n.size()>0)
		{
			Cell m = n.get(0);
			return m;
		}
		else
			return new Cell(-1, -1);
	}
	void adjWalls(Cell oth)
	{
		int xd = x-oth.x;
		if(xd==-1)
			oth.walls[1] = false;
		else if(xd==1)
			walls[1] = false;
		int yd = y-oth.y;
		if(yd==-1)
			oth.walls[0] = false;
		else if(yd==1)
			walls[0] = false;
	}
}
