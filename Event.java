
/**
 * Defines events. Only "special" events should be defined as objects of event class. Other "specialized" should be defines as Loot_Item, Encounter or others extended classes. Events can be chained, one after another.
 *
 */
public class Event {
	/**If true, than even is automatic launched, when player sets foot inside of cell with this event. Other wise, it's either special event, or can be launched by player "looking around" */
	boolean automatic;  /**gui description of event */
	String description;
	Event event;
	Event(String d, boolean a, Event e)
	{
		automatic = a;
		description = d;
		event = e;
	}
	Event(String d, boolean a)
	{
		automatic = a;
		description = d;
	}
	Event(String d)
	{
		description = d;
		
	}
	Event( boolean a)
	{
		automatic = a;
	}
	Event(Event a)
	{
		automatic = a.automatic;
		description = a.description;
	}
}
/**Event that grants item */
class Loot_Item extends Event
{
	Items item;	
	Loot_Item(Items it, boolean auto)
	{
		super(auto);
		description = "You found " + it.name;
		item = it;
		automatic = auto;
	}
	Loot_Item(String d,Items it, boolean auto)
	{
		super(d,auto);
		description = d;
		item = it;
		automatic = auto;
	}
	Loot_Item(Loot_Item e)
	{
		super(e);
		description =  e.description;
		if(e.item.getClass() == Armor.class) item = new Armor((Armor) e.item);
		else if(e.item.getClass() == Weapon.class) item = new Weapon((Weapon) e.item);
		else item = new Items(e.item);
		automatic = e.automatic;
	}
}
