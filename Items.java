public class Items {
	/**Item name */
	public String name;								/**Item description */
	public String description;						/**Item gold value (not implemented) */
	int sell_price;									/**If item can stack (not implemented) */
	boolean stacks;									/**Current stack of item (not implemented) */
	int count;
	public Items(String n, String d, int s,boolean st)
	{
		stacks = st;
		name = n;
		description = d;
		sell_price = s;
		count = 1;
	}
	public Items(Items a)
	{
		stacks = a.stacks;
		name = a.name;
		description = a.description;
		sell_price = a.sell_price;
		count = a.count;
	}
	public String toString()
	{
		return description;
	}
}
class Weapon extends Items
{	
	/** Weapon damage*/
	double weapon_damage;					/**Sublass of weapon: 0 - two hand sword, 1 - spear, 2 - broad/sword, 3 - axe, 4 - mace, 5 - one hand sword, 6 - fist, 7 - just shield */
	int subclass;	// 0 - 2 hand sword, 1 - spear, 2 - broad/sword, 3 - axe, 4 - mace, 5 - onesword, 6 - fist, 7 - just shield
	public Weapon(String n, String d, int s, double wd, int sub, boolean st)
	{
		super(n, d, s,st);
		subclass = sub;
		weapon_damage = wd;
	}
	public Weapon(Weapon a)
	{
		super(a);
		weapon_damage = a.weapon_damage;
		subclass = a.subclass;
	}
	public String toString()
	{
		return "Weapon:"+ description;
	}
}
class Armor extends Items
{
	/** Armor*/
	int armor;
	public Armor(String n, String d, int s, int arm,boolean st)
	{
		super(n, d, s,st);
		armor = arm;
	}
	public Armor(Armor a)
	{
		super(a);
		armor = a.armor;
	}
	public String toString()
	{
		return "Armor:"+ description;
	}
}
class Shield extends Items
{
	/** Armor*/
	int armor;
	public Shield(String n, String d, int s, int arm,boolean st)
	{
		super(n, d, s,st);
		armor = arm;
	}
	public Shield(Shield a)
	{
		super(a);
		armor = a.armor;
	}
	public String toString()
	{
		return "Shield:"+ description;
	}
}