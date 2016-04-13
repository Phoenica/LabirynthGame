/** Class that defines abilities. Any changes to current current abilities, should be made in database class*/
public class Abilities {
	/**	Ability name*/
	String name; 					/** Ability gui description*/
	String description;				/** Ability Ability target (opponent-true or yourself-false)*/
	boolean target;					/** Ability effect (special effect caused by ability)*/
	public Ability_Effect effect;	/** Ability Ability strenght based damage*/
	double base_Damage;				/** Ability weapon based damage*/
	double weapon_damage_mod; 		/** Ability armor-penetration modifier*/
	double armor_pen;				/** Ability critical strike chance*/
	double critical_strike;			/** Ability cooldown(how often ability can be used)*/
	int cooldown;					/** Ability cooldown counter(counts how much turns are left, till ability can be used afgain)*/
	int cooldown_timer;				/** Ability energy cost*/
	double stamina_used;			/** Ability priority(bigger priority, bigger chance that user will attack first)*/
	double priority; 				/** Determines if ability can be parried or not*/
	boolean  can_be_parried;
	/**
	 * @param n name
	 * @param d description
	 * @param dps base damage
	 * @param weap_mod weapon damage
	 * @param arp armor penetration chance 
	 * @param crit crit chance(default should be 5%)
	 * @param cd cooldown
	 * @param stam stamina cost
	 * @param t target(true - enemy, false - yourself)
	 * @param ef effect (can be null)
	 * @param p "priority"
	 * @param pp "can be parried"
	 */
	public Abilities(String n, String d, int dps,int weap_mod,int arp, int crit, int cd, int stam,boolean t, Ability_Effect ef, double p, boolean pp)
	{
		name = n;
		description = d;
		base_Damage = dps;
		weapon_damage_mod = weap_mod;
		armor_pen = arp;
		critical_strike = crit;
		cooldown = cd;
		stamina_used = stam;
		target = t;
		effect = ef;
		priority = p;
		can_be_parried =pp;
		cooldown_timer = 0;
		
	}
	public Abilities(Abilities another)
	{
		name = another.name;
		description = another.description;
		base_Damage = another.base_Damage;
		weapon_damage_mod = another.base_Damage;
		armor_pen = another.armor_pen;
		critical_strike = another.critical_strike;
		cooldown = another.cooldown;
		stamina_used = another.stamina_used;
		target = another.target;
		if(another.effect!= null)effect = new Ability_Effect(another.effect);
		priority = another.priority;
		can_be_parried = another.can_be_parried;
		cooldown_timer = 0;
	}
	
	
}
class Ability_Effect 
{
	/**Effect's name */
	String name;					/**Effect's chance to be applied to target */
	int apply_chance; 				/**Effect's duration(in turns)  */
	int duration;					/**Effect's bleed damage(or over time heal)(applied at end of turn)(damage is in % of remaining health)(positive number is damage, negative is heal) */
	int damage; 					/**Effect's energy damage*/
	int fatigue;					/**Determines if target will try to avoid attack(1- parry, 2- dodge, 3- block, 4- deflect) */
	int avoidance; 					/**If true, than target can't use any ability next turn*/
	boolean stun;
	/**
	 * 
	 * @param n Name
	 * @param chance apply chance
	 * @param d duration
	 * @param dmg bleed/heal over time
	 * @param f energy damage
	 * @param av avoidance
	 * @param s stun
	 */
	public Ability_Effect(String n, int chance, int d, int dmg, int f, int av, boolean s)
	{
		name = n;
		apply_chance = chance;
		duration = d;
		damage = dmg;	
		fatigue = f;
		avoidance = av;
		stun = s;
	}
	public Ability_Effect(Ability_Effect another)
	{
		name = another.name;
		apply_chance = another.apply_chance;
		duration = another.duration;
		damage = another.damage;
		fatigue = another.fatigue;
		avoidance = another.avoidance;
		stun = another.stun;
	}
}

// ability dmg = weapon_dmg * weapon_mod / 100 * str/10 + base_dmg/10 * str/10