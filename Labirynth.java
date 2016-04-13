import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Labirynth class. Defines game field(maze). And functions for creating said maze.
 *
 */
class Labirynth
{
	/** Size of labyrinth*/
	int size;								/** Actual labyrinth*/		
	Cell[][] maze;
	/**
	 * Basic Constructor
	 * @param n size
	 * @param data Database object
	 */
	public Labirynth(int n,Database data)
	{
		size = n;
		maze = new Cell[n][n];
		build_maze(n/2,n/2,n,-1);	
		extra_paths(n);
		create_exit(n,data);
		create_events(n,data);
	}
	/**
	 * Function that recursively build labyrinth.
	 * @param x x coordinate of maze that we are currently building
	 * @param y y coordinate of maze that we are currently building
	 * @param n size of labyrinth
	 * @param p side from which generator came to current cell while generating labyrinth, -1 when generator just started
	 */
	void build_maze(int x, int y, int n,int p)
	{	
		maze[x][y] = new Cell();
		if(p != -1)maze[x][y].paths[p] = true;
		//losowe przeszukiwanie nie utworzonych scierzek
		int tab[] = random_array();
		for(int i=0; i < 4; i++)
		{
			switch(tab[i])
			{
				case 0: if(y < (n-1)) 	if(maze[x][y+1] == null) 	{maze[x][y].paths[0] = true; build_maze(x,y+1,n,1);} break; // going north
				case 1: if(y > 0) 		if(maze[x][y-1] == null)	{maze[x][y].paths[1] = true; build_maze(x,y-1,n,0);} break; // going south
				case 2: if(x > 0) 		if(maze[x-1][y] == null)	{maze[x][y].paths[2] = true; build_maze(x-1,y,n,3);} break; // going west -zachod
				case 3: if(x < (n-1)) 	if(maze[x+1][y] == null)	{maze[x][y].paths[3] = true; build_maze(x+1,y,n,2);} break; // going east - wschod
			}
		}
		
		
	}
	/**
	 * Creates random events inside labyrinth. (To Do: create diffrent types of events beside discovering items)
	 * @param n labyrinth size
	 * @param data Database object
	 */
	void create_events(int n, Database data)
	{
		Random rand = new Random();
		int l = n*n/8; // liczba eventow(itemow)
		for(int i = 0; i < l; i++)
		{
			int x = rand.nextInt(n);
			int y = rand.nextInt(n);
			int r = rand.nextInt(3);
			int p;
			int g = rand.nextInt(2);
			boolean t;
			if(g == 0)  t = false;
			else  t = true;
			switch(r)
			{
			case 0: maze[x][y].event.add(new Loot_Item(new Weapon(data.weapon_list.get(rand.nextInt(23)+1)),false)); break;
			case 1: maze[x][y].event.add(new Loot_Item(new Armor(data.armor_list.get(rand.nextInt(10))),false)); break;
			case 2: maze[x][y].event.add(new Loot_Item(new Shield(data.shield_list.get(rand.nextInt(6))),false)); break;
			}
			
		}

		
	}
	/**
	 * Creates exit event inside labyrinth
	 * @param n labyrinth size
	 * @param data Database object
	 */
	void create_exit(int n, Database data)
	{
		Random randomGenerator = new Random();
		int random = randomGenerator.nextInt(4);
		int x,y;
		switch(random)
		{
			case 0: x = 0; y = randomGenerator.nextInt(n); break;
			case 1: x = n-1; y = randomGenerator.nextInt(n); break;
			case 2: y = 0; x = randomGenerator.nextInt(n); break;
			default: y = n-1; x = randomGenerator.nextInt(n); break;
		}
		maze[x][y].event.add(data.event_list.get(1));
	}
	/**
	 * Maze generator creates very simple labyrinth, that can be solved just by sticking to one side. Also it's not generating enough intersection.
	 * This function fixes that, by adding extra patch "breakes wall" between 2 paths)
	 * @param n size
	 */
	void extra_paths(int n)
	{
		Random rand = new Random();
		int i,x,y,z;
		double g = (n*n)/7;
		for(i=0; i<g; i++)
		{
			x = rand.nextInt(n-2)+1;
			y = rand.nextInt(n-2)+1;
			z = rand.nextInt(4);
			switch(z)
			{
				case 0:	maze[x][y].paths[0] = true; maze[x][y+1].paths[1] = true; break;
				case 1: maze[x][y].paths[1] = true; maze[x][y-1].paths[0] = true; break;
				case 2: maze[x][y].paths[2] = true; maze[x-1][y].paths[3] = true; break;
				default:maze[x][y].paths[3] = true; maze[x+1][y].paths[2] = true; 
						
			}
		}
	}
	/**
	 * Used by labyrinth builder. Randomizes direction of generating new cells
	 */
	int[] random_array()
	{
		
		//generacja tablicy 0-3
		int[] tab = new int[4]; 
		for(int i = 0; i<4; i++) tab[i] = i;
		//zamiana miejsc w tablicy
		
		Random rand = new Random();
		int temp,temp2;
		for (int i=0; i<4; i++) {
			temp2 = rand.nextInt(4);
		    temp = tab[i];
		    tab[i] = tab[temp2];
		    tab[temp2] = temp;
		}
		
		return tab;
	}
}
/**
 * 
 *	Single cell class.
 */
class Cell
{
	/** Defines in which direction player can go from this cell */
	public boolean[] paths;				/** List of events inside this cell */
	public List <Event> event;			/** If true, player already visited this cell */
	public boolean visited;
	public Cell()
	{
		event = new ArrayList<Event>();
		visited = false;
		paths = new boolean[4];
		paths[0] = false; paths[1] = false; paths[2] = false; paths[3] = false;  // 0- north, 1- south, 2- west, 3-east
		
	}
}