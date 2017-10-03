package dumbStuff;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;


public class Main 
{
	static String title = "Maze stuff I guess";
	static int fps = 60;
	static int tsize, w, h;
	static boolean play = true;
	static int cols = 100;
	static int rows = 50;
	static JFrame frame;
	static Graphics g;
	static BufferStrategy bs;
	static Canvas canvas;
	static Cell grid[][] = new Cell[cols][rows];
	static ArrayList<Cell> stack = new ArrayList<Cell>();
	static ArrayList<Cell> solve = new ArrayList<Cell>();
	static Cell current;
	static Random rand = new Random();
	static boolean solved;
	static boolean gend;
	static boolean sf;
	static int gx;
	static int gy;
	static int sx;
	static int sy;
	public static void main(String args[]) throws InterruptedException
	{
		w = 1600;
		h = 800;
		tsize = h/rows<w/cols?h/rows:w/cols;
		createDisplay();
		initRender();
		genInit();
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		double timePerTick = 1;//1000000000/fps;
		while(play)
		{
			now = System.nanoTime();
			delta += (now-lastTime)/timePerTick;
			lastTime = now;
			if(delta >= 1)
			{
				tick();
				render();
				delta--;
			}	
		}
		frame.dispose();
	}
	public static void genInit()
	{
		stack.clear();
		solve.clear();
		for(int i = 0; i<cols; i++)
		{
			for(int j = 0; j<rows; j++)
			{
				grid[i][j] = new Cell(i, j);
			}
		}
		sx = 0;
		sy = 0;
		current = grid[sx][sy];
		stack.add(current);
		solve.add(current);
		current.v = true;
		solved = false;
		gend = true;
		gx = cols-1;
		gy = rows-1;
		initRender();
	}
	public static void solInit()
	{
		sf = false;
		stack.clear();
		solve.clear();
		for(int i = 0; i<cols; i++)
		{
			for(int j = 0; j<rows; j++)
			{
				grid[i][j].v = false;
			}
		}
		current = grid[sf?sx:gx][sf?sy:gy];
		stack.add(current);
		solve.add(current);
		current.v = true;
		solved = false;
		gend = false;
	}
	public static void tick()
	{
		if(gend)
		{
			Cell m = current.unVneighbors(grid, rows, cols);
			if(m.x>-1 &&m.y>-1)
			{
				current = m;
				current.v = true;
				stack.add(current);
				if(!solved)
					solve.add(current);
			}
			else if(stack.size()>1)
			{
				stack.remove(stack.size()-1);
				if(!solved)
					solve.remove(solve.size()-1);
				current = stack.get(stack.size()-1);
			}
			if(solved && current.x==sx && current.y==sy)
			{
				render();
				gend = false;
				solInit();
			}
			else if(!solved && current.x==gx && current.y==gy)
				solved = true;//*/
		}
		else
		{
			Cell m = current.opWneighbors(grid, rows, cols);
			if(m.x>-1 &&m.y>-1)
			{
				current = m;
				current.v = true;
				stack.add(current);
			}
			else if(stack.size()>1)
			{
				stack.remove(stack.size()-1);
				current = stack.get(stack.size()-1);
			}
			if(!solved && current.x==(sf?gx:sx) && current.y==(sf?gy:sy))
			{
				solved = true;//*/
				gend = true;
				genInit();
			}
		}
	}
	public static void initRender()
	{
		bs = canvas.getBufferStrategy();
		if(bs == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.clearRect(0, 0, w, h);
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0,  0,  w, h);
		
		bs.show();
		g.dispose();
	}
	public static void render()
	{	
		bs = canvas.getBufferStrategy();
		if(bs == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		Cell c = current;
		for(int i = -1; i<2; i++)
		{
			for(int j = -1; j<2; j++)
			{
				if((current.x>0 || i>-1) && (current.x<cols-1 || i<1) && (current.y>0 || j>-1) && (current.y<rows-1 || j<1))
					c = grid[current.x+i][current.y+j];
				else
					continue;
				if(solve.contains(c))
				{
					if(c.x==gx && c.y==gy)
						g.setColor(new Color(0, 0, 100));
					else if(c.x==sx && c.y==sy)
						g.setColor(new Color(0, 0, 100));
					else
						g.setColor(gend?new Color(0, 0, 255):new Color(0, 255, 255));
					g.fillRect(tsize*c.x, tsize*c.y, tsize, tsize);
				}
				else if(stack.contains(c) && gend)
				{
					if(solve.contains(c))
						continue;
					g.setColor(new Color(255, 0, 0));
					g.fillRect(tsize*c.x, tsize*c.y, tsize, tsize);
				}
				if(i==0 && j==0)
				{
					g.setColor(gend?new Color(255, 255, 255):new Color(255, 255, 0));
					g.fillRect(tsize*c.x, tsize*c.y, tsize, tsize);
				}
				else if(c.v && !stack.contains(c) && !solve.contains(c))
				{
					g.setColor(gend?new Color(0, 255, 0):Color.DARK_GRAY);
					g.fillRect(tsize*c.x, tsize*c.y, tsize, tsize);
				}
				g.setColor(Color.BLACK);
				if(c.walls[0] && c.v)//top
					g.drawLine(c.x*tsize, c.y*tsize, (c.x+1)*tsize, c.y*tsize);
				if(c.walls[1] && c.v)//left
					g.drawLine(c.x*tsize, c.y*tsize, c.x*tsize, (c.y+1)*tsize);
			}
		}
		bs.show();
		g.dispose();
	}
	public static void createDisplay()
	{
		frame = new JFrame(title);
		frame.setSize(w, h);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(w, h));
		canvas.setMaximumSize(new Dimension(w, h));
		canvas.setMinimumSize(new Dimension(w, h));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
	}
}
