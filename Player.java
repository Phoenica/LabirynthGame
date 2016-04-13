import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**Player class contains all important data in current session, such as player stats, gamefield(labirynth),and all items gathered by player.
 * It can be used as game save as well */
public class Player {
		
	/**Player name*/
		String name;									/**Determines who attacks first, and is used for calculating dodge chance*/
		double agility;									/**Determines how much hitpoints player will have, and how fast he regenerates energy in combat*/
		double stamina; 								/**Determines how powerfull player's attacks are*/
		double strenght; 								/**Player level*/
		int level;										/**Player atributes points - can be converter into agility/strenght/stamina*/
		int level_points;								/**Array of abilities, currently player has*/
		Abilities[] skills;								/**Player's experience points*/
		int experience; 								/**Player's current hitpoints*/
		double hitpoints;								/**Player's current energy*/
		double fatigue;									/**List of player's effects (like bleeding) - used for combat*/
		List <Ability_Effect> effects;					/**Player's weapon slot*/
		Weapon weapon1;									/**Player's armor slot*/
		Armor armor;									/**Player's shield slot*/
		Shield shield;									/**Player's gold*/
		int gold;										/**Player's items list*/
		List <Items> bag;								/**Data: direction which player is facing*/
		int faces_direction;							/**Data: x,y cordinates of player inside labirynth*/
		int loc_x, loc_y;								/**Data: Labirynth(game field)*/
		Labirynth labirynth;							/**Data: Enemies Defeated*/
		int killed;										/**Data: cells visited*/
		int steps;
		/**
		 * 
		 * @param a Player's name
		 * @param n Labirynth
		 * @param data Database object
		 */
		public Player(String a,Labirynth n, Database data)
		{
			//starting stats
			skills = new Abilities[10];
			skills[8] = new Abilities(data.ability_list.get(24));
			skills[9] = new Abilities(data.ability_list.get(25));
			name = a;
			stamina = 10;
			strenght = 10;
			agility = 10;
			//starting "points"
			gold = 0;
			hitpoints = 20 * stamina;
			fatigue = 100;
			experience = 0;
			effects = new ArrayList<Ability_Effect>();
			//game data
			weapon1 = data.weapon_list.get(1);	// rusty sword
			armor = data.armor_list.get(0);
			faces_direction = 0; // forward, right, behind, left
			loc_x=loc_y = n.size/2;
			labirynth = n;
			bag = new ArrayList<Items>();
			killed = 0;
			steps = 0;
			level = 1;
			level_points = 0;
		}
		/**
		 * Converts direction in which player is going (left,right) and direction of which palyer is facing, to actual direction (north/west)
		 * @param side direction (forward,right,behind,left)
		 * @return direction(north, east, sout, west )
		 */
		public String convert_move_maze(int side)
		{
			int a = (side + faces_direction)%4;
			switch(a)
			{
			case 0: return move_maze(0,1);//polnoc - north
			case 1: return move_maze(1,0);// wschod - east
			case 2: return move_maze(0,-1);// polodnie - south
			default: return move_maze(-1,0); //zachod west
			}
		}
		/**
		 * Used to disable / enable buttons of direction player can go. (Players can't go through wall, so if there is dead end in front of him, he can't go forward)
		 * @param side direction (forward,right,behind,left)
		 * @return
		 */
		public int convert_button(int side)
		{
			int a = (side + faces_direction)%4;
			switch(a)
			{
				case 0: return 0;
				case 1: return 3;
				case 2: return 1;
				default: return 2;
			}
			
		}
		/**
		 * Function that moves player through labirynth. It uses Cartesian coordinate system.
		 * @param x cordinate x
		 * @param y cordinate y
		 * @return 
		 */
		public String move_maze(int x, int y)
		{
			// funkcja przejdzie przez n pomieszcze, puki nie natrafimy na rozwidlenie
			// ify sprawdzaja, ktory ruch przedchwila wykonalismy
			// p - oznacza w ktora strone patrzy gracz, d oznacza ktore przejscie wzgledem siebie wybiera
		
			String output =  "You chose your patch and moved forward. ";
			boolean debug = false;
			do
			{

				//if(debug== true)break;
				//debug_move();
				labirynth.maze[loc_x][loc_y].visited = true;
				loc_x = loc_x + x;
				loc_y = loc_y + y;
				//if(count_paths() != 2) {break;	}
				if(y==1) faces_direction = 0;
				if(y==-1) faces_direction = 2;
				if(x==-1) faces_direction = 3;
				if(x==1) faces_direction = 1;
				steps++;
				break;
				//last_x = x;
				//last_y = y;
				//x = y = 0;
				/*
				if(labirynth.maze[loc_x][loc_y].paths[0] == true && last_y != -1) {y=1; faces_direction = 0;debug = true;} 	//going north
				else
				if(labirynth.maze[loc_x][loc_y].paths[1] == true && last_y != 1) {y =-1;faces_direction = 2;debug = true;}	//going south
				else
				if(labirynth.maze[loc_x][loc_y].paths[2] == true && last_x != 1) {x = -1;faces_direction = 3;debug = true;}	//goint west
				else
				if(labirynth.maze[loc_x][loc_y].paths[3] == true && last_x != -1) {x = 1;faces_direction = 1;debug = true;} 		//going east
				*/
				
				
			}while(true);
			if(count_paths() == 1) output+=" After going through room you made it into dead end.";
			else if(count_paths() > 2) output+="After going through room you you came across intersection";
			return output;
		}
		/**
		 * Calculates, in how many directions player can go
		 * @return number of paths aviable
		 */
		int count_paths()
		{
			int count=0;
			if(labirynth.maze[loc_x][loc_y].paths[0]) count++;
			if(labirynth.maze[loc_x][loc_y].paths[1]) count++;
			if(labirynth.maze[loc_x][loc_y].paths[2]) count++;
			if(labirynth.maze[loc_x][loc_y].paths[3]) count++;
			return count;
		}
		
}

