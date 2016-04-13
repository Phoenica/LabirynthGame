import java.util.ArrayList;
import java.util.List;

/**
 *	Class that defines enemy object.
 */
public class Enemy {
	/** Enemy name*/ 
	String Name;										/** Damage ability list*/ 
	List <Abilities> dps_skills;						/** Deffensive ability list*/ 
	List <Abilities> deffensive_skills;					/** List of ultimate abilities*/ 
	List <Abilities> ulti_skills;						/** List of abilities used only when player is stunned */ 
	List <Abilities> on_stun_skills;					/** List of enemy's effect*/ 
	List <Ability_Effect> effects;						/** Enemy's Hitpoints*/ 
	double hitpoints;									/** Enemy's stamina*/ 
	int stamina;										/** Enemy's energy*/ 
	double fatigue;										/** Enemy's weapon damage*/ 
	double weapon_damage;								/** Enemy's Strength*/ 
	double attack_power;								/** Enemy's agility*/ 
	int speed;											/** Enemy's armor*/ 
	int armor;											/** Enemy's stamina he gains per level by scaling with player's level*/ 
	int stamina_per_lv;									/** Enemy's strenght he gains per level by scaling with player's level*/ 
	double attack_power_per_lv;							/** Enemy's armor he gains per level by scaling with player's level*/ 
	int armor_per_lv;									/** Enemy's agility he gains per level by scaling with player's level*/ 
	int speed_per_lv;									/** Enemy's weapon damage he gains per level by scaling with player's level*/ 
	double weapon_damage_per_lv;						/** Enemy experience reward*/ 
	double exp;											/** Enemy gold reward*/ 
	double gold;										/** Enemy AI behavior(1 - offensive, 2 - balanced, 3 - defensive)*/ 
	int behaviour; 
	/**
	 * Basic constructor. "per level" variables are filled automatically
	 * @param n name
	 * @param ap attack power / strenght
	 * @param st stamina
	 * @param sp speed
	 * @param arm armor
	 * @param wp weapon damage
	 * @param ex experience 
	 * @param g gold
	 */
	public Enemy(String n, int ap, int st,  int sp,  int arm, double wp, int ex, int g)
	{
		effects = new ArrayList <Ability_Effect>();
		dps_skills = new ArrayList <Abilities>();
		deffensive_skills = new ArrayList <Abilities>();
		ulti_skills = new ArrayList <Abilities>();
		on_stun_skills = new ArrayList <Abilities>();
		Name = n;
		stamina = st;
		attack_power = ap;
		speed = sp;
		armor = arm;
		hitpoints = st*20; 
		fatigue = 100;
		behaviour = 2;
		weapon_damage = wp;
		stamina_per_lv = stamina / 4;
		attack_power_per_lv = attack_power / 4;
		speed_per_lv = speed / 4;
		armor_per_lv = armor/2;
		weapon_damage_per_lv = weapon_damage/6;
		gold = g;
		exp = ex;
	}
	/**
	 * Almost basic copy constructor. It takes into consideration player level(to scale up enemy stats) and sets up new behavior.
	 * @param e enemy
	 * @param level player's level
	 * @param b behavior
	 */
	public Enemy(Enemy e, int level,int b)
	{
		effects = e.effects;
		dps_skills = e.dps_skills;
		deffensive_skills = e.deffensive_skills;
		ulti_skills = e.ulti_skills;
		on_stun_skills = e.on_stun_skills;
		Name = e.Name;
		stamina = e.stamina+e.stamina_per_lv*(level-1);
		attack_power = e.attack_power+(e.attack_power_per_lv*(level-1));
		speed = e.speed+e.speed_per_lv*(level-1);
		armor = e.armor+e.armor_per_lv*(level-1);
		hitpoints = stamina * 20;
		fatigue = 100;
		weapon_damage = e.weapon_damage+e.weapon_damage_per_lv*(level-1)/2;
		exp = e.exp + e.exp*(level-1)/2;
		gold = e.gold + e.gold*(level-1)/3;
		behaviour = b;
		attack_power_per_lv = e.armor_per_lv;
		speed_per_lv = e.speed_per_lv;
		stamina_per_lv = e.speed_per_lv;
		armor_per_lv = e.armor_per_lv;
		weapon_damage_per_lv = e.weapon_damage_per_lv;
	}
}

