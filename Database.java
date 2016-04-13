import java.util.ArrayList;
import java.util.List;

/**This class deffines objects such as items, abilities or enemies.
 *  All objects are divided into lists. All items are defined in constructor, and shouldn't be added to database object from any other place in program, or any other class.
 *   Object of this class preferably should be generated only once (at start of program),
 *    and shouldn't be used inside player class objects(as player class objects may serve as save files in future, and including database of inside player class objects, will render saves incompatible with future versions)*/

public class Database {
	/**List of all weapons */
	List <Weapon> weapon_list;									/**List of all armors */
	List <Armor> armor_list;									/**List of all shields */
	List <Shield> shield_list;									/**List of all other items */
	List <Items> item_list;										/**List of all Abilities*/
	List <Abilities> ability_list;								/**List of all enemies*/
	List <Enemy> enemy_list;									/**List of all Ability effects */
	List <Ability_Effect> effect_list;							/**List of events */
	List <Event> event_list;  									/**List of lists: Player gains abilities based on weapon he holds; this list contains lists of all abilities that are granted by each weapon; Each of weapon is next list: Sword, Spear, Shield&Broad, Axe, Hammer, one-hand-sword, fists, just Sheld; Each of those list contains pairs, first element is level when player gets ability, and second element of pair is ability he gains. */
	List <List <Pair<Integer,Abilities>>> ability_to_weapon;
	/**All new objects should be defines inside constructor */
	Database()
	{
		
		
		weapon_list = new ArrayList<Weapon>();
		//weak / normall quality
		// 0 - 2 hand sword, 1 - spear, 2 - broad/sword, 3 - axe, 4 - mace, 5 - onesword, 6 - fist, 7 - just shield
		weapon_list.add(0,new Weapon("Fists","You don't have weapon equiped",0,0,6,false)); //empty weapon slot
		weapon_list.add(1,new Weapon("Sharp Object","Better than nothing",5, 3,5,false));
		weapon_list.add(2,new Weapon("Rusty Sword","It doesn't look very strong",20, 4,5,false));
		weapon_list.add(3,new Weapon("Rusty GreatSword","",40, 5,0,false));
		weapon_list.add(4,new Weapon("Iron Sword","",80, 5,5,false));
		weapon_list.add(5,new Weapon("Iron Great Sword","",130, 7,0,false));
		weapon_list.add(6,new Weapon("Steel Sword"," ",135, 7,5,false));
		weapon_list.add(7,new Weapon("Steel Great Sword"," ",170, 9,0,false));
		weapon_list.add(8,new Weapon("Gladius"," ",200, 8,5,false));
		weapon_list.add(9,new Weapon("Claymore"," ",300, 11,0,false));

		weapon_list.add(10,new Weapon("Sharp Stick","Better than noothing",5, 2,1,false));
		weapon_list.add(11,new Weapon("Rusty Spear","It doesn't look very strong",10, 5,1,false));
		weapon_list.add(12,new Weapon("Iron Spear","",30, 6,1,false));
		weapon_list.add(13,new Weapon("Steel Spear"," ",70, 8,1,false));
		weapon_list.add(14,new Weapon("Partisan"," ",100, 10,1,false));
		
		weapon_list.add(15,new Weapon("Sharp Stick","Better than noothing",5, 3,3,false));
		weapon_list.add(16,new Weapon("Rusty Axe","It doesn't look very strong",20, 5,3,false));
		weapon_list.add(17,new Weapon("Iron Axe","",60, 7,3,false));
		weapon_list.add(18,new Weapon("Steel Axe"," ",225, 9,3,false));
		weapon_list.add(19,new Weapon("Battle Axe"," ",200, 11,3,false));
		
		weapon_list.add(20,new Weapon("Rock","Better than noothing",5, 1,4,false));
		weapon_list.add(21,new Weapon("Rusty Mace","It doesn't look very strong",20, 3,4,false));
		weapon_list.add(22,new Weapon("Iron Mace","",60, 4,4,false));
		weapon_list.add(23,new Weapon("Steel Mace"," ",225, 6,4,false));
		weapon_list.add(24,new Weapon("Morning Star"," ",200, 8,4,false));
		

		weapon_list.add(25,new Weapon("Excalibur"," ",1000, 15,0,false));
		weapon_list.add(26,new Weapon("Gae Bulg"," ",750, 13,1,false));
		weapon_list.add(27,new Weapon("Vajra"," ",750, 14,4,false));
		weapon_list.add(28,new Weapon("Enormous rock axe"," ",750, 14,3,false));
		

		armor_list = new ArrayList<Armor>();
		
		armor_list.add(0,new Armor("Clothes","Just your everyday clothes",0,0,false));	//empty armor slot
		armor_list.add(1,new Armor("Leather Armor","Just your everyday clothes",10,10,false));
		armor_list.add(2,new Armor("Heavy Leather Armor","Just your everyday clothes",20,20,false));
		armor_list.add(3,new Armor("Scale Armor","Just your everyday clothes",40,25,false));
		armor_list.add(4,new Armor("Laminar Armor","Just your everyday clothes",100,40,false));
		armor_list.add(5,new Armor("Heavy Scale Armor","Just your everyday clothes",60,45,false));
		armor_list.add(6,new Armor("Mail","Just your everyday clothes",120,50,false));
		armor_list.add(7,new Armor("Heavy Laminar Armor","Just your everyday clothes",150,60,false));
		armor_list.add(8,new Armor("Plate Armor","Just your everyday clothes",400,100,false));
		armor_list.add(9,new Armor("Tournament Armor","Just your everyday clothes",500,120,false));
		
		
	
		shield_list = new ArrayList<Shield>();
		
		shield_list.add(0,new Shield("Wooden Buckler","Very fragile buckler",10,12,false));
		shield_list.add(1,new Shield("Round Wooden Shield","Very fragile buckler",15,24,false));
		shield_list.add(2,new Shield("Wooden Kite Shield","Very fragile buckler",20,30,false));
		shield_list.add(3,new Shield("Iron Buckler","Very fragile buckler",80,40,false));
		shield_list.add(4,new Shield("Iron Round Shield","Very fragile buckler",100,60,false));
		shield_list.add(5,new Shield("Iron Kite Shield","Very fragile buckler",130,80,false));
		shield_list.add(6,new Shield("Tournament Shield","Very fragile buckler",200,100,false));

		shield_list.add(7,new Shield("Rho Aias","Legendary",500,160,false));
		
		
		item_list = new ArrayList<Items>();
		
		item_list.add(0, new Items("Wolf Head","Trophy - can be sold for gold", 5,true));
		item_list.add(1, new Items("Saphire","Can be sold for gold", 200,true));		
		
		
		effect_list = new ArrayList<Ability_Effect>();
		
		effect_list.add(0,new Ability_Effect("Bleed-Weak",100,4,1,0,0,false));	// all bleeds can stack
		effect_list.add(1,new Ability_Effect("Bleed-Medium",100,5,2,0,0,false));
		effect_list.add(2,new Ability_Effect("Bleed-Big",100,6,4,0,0,false));
		effect_list.add(3,new Ability_Effect("Bleed-Uber",100,12,8,0,0,false));
		effect_list.add(4,new Ability_Effect("Fatigue-Weak",100,0,0,4,0,false));
		effect_list.add(5,new Ability_Effect("Fatigue-Mid",100,0,0,6,0,false));
		effect_list.add(6,new Ability_Effect("Fatigue-Big",100,0,0,12,0,false));
		effect_list.add(7,new Ability_Effect("Stun_Chance-Weak",20,1,0,0,0,true));
		effect_list.add(8,new Ability_Effect("Stun_Chance-Med",35,1,0,0,0,true));
		effect_list.add(9,new Ability_Effect("Stun_Chance-Hight",50,1,0,0,0,true));
		effect_list.add(10,new Ability_Effect("Parry",100,1,0,0,1,false));
		effect_list.add(11,new Ability_Effect("Dodge",100,1,0,0,2,false));
		effect_list.add(12,new Ability_Effect("Block",100,1,0,0,3,false));
		effect_list.add(13,new Ability_Effect("Swing",100,1,0,0,4,false));
		effect_list.add(14,new Ability_Effect("Slow",100,1,0,0,0,false));
		effect_list.add(15,new Ability_Effect("Stun+Fatigue",100,1,0,6,0,true));
		effect_list.add(16,new Ability_Effect("Sunder_armor",100,1,0,0,0,false));
		effect_list.add(17,new Ability_Effect("Stun_Chance-2turns",25,2,0,0,0,true));// 
		effect_list.add(18,new Ability_Effect("Trauma",100,6,4,0,0,false));

		effect_list.add(19,new Ability_Effect("Heavy Regen",100,999,6,0,0,false));	//troll regeneration
		
		
		//(name, desc, dmg, weap_mod, arp, crit,  cd, fatigue, target, Ability_Effect , priority, parryable)

		ability_list = new ArrayList<Abilities>();
		
		ability_list.add(0,new Abilities("Jab","Simple punch. Dealing 100% strenght dmg.",10,0,0,5,0,5,true,null,1,true));	// numbers are, base dmg % of str, weapon damage %, weapon mod %, arp, crit, cd, fatigue
		ability_list.add(1,new Abilities("Thrust","Fast attack that have bigger chance to penetrate armor and can't be parried. It has bigger crit chance and can cause bleed. \nSimplified damage calculation: 80% weapon damage*strenght + 100% str, uses 3 energy",10,80,75,20,0,3,true,effect_list.get(1),1.5,false));
		ability_list.add(2,new Abilities("Slash","Simple slash, can cause bleed.\nSimplified damage calculation: 130% weapon damage*strenght + 100% strenght, uses 5 energy ",10,130,0,10,0,5,true,effect_list.get(0),1,true));
		ability_list.add(3,new Abilities("Smash","Simple smash, tires enemy, can't be parried.\nSimplified damage calculation: 50% weapon damage*strenght + 170% strenght, uses 7 energy",17,50,10,5,0,7,true,effect_list.get(5),1,false));
		ability_list.add(4,new Abilities("Crushing Blow","Slow but powerfull blow that have bigger chance to penetrate armor.It also have chance to stun. \nSimplified damage calculation: 75% weapon damage*strenght + 150% strenght, uses 14 energy",15,75,35,10,0,14,true,effect_list.get(7),0,true));
		ability_list.add(5,new Abilities("Parry","Trying to parry enemy attack. Some abilities can't be parried. \nCooldown: 3 turn, uses 2 energy.",0,0,0,0,3,3,false,effect_list.get(10),2,false));
		ability_list.add(6,new Abilities("Dodge","Trying to dodge enemy attack. Dodge chance is basted on your and your's opponets speed, as well as remaining energy. \nCooldown: 3 turn, uses 5 energy.",0,0,0,0,3,5,false,effect_list.get(11),2,false));
		ability_list.add(7,new Abilities("Block","Trying to block enemy attack. Relies on your endurance and enemy's strenght. Uses energy based on how powerfull blow you are blocking. If hit is too powerfull, blocking will fail!\n Cooldown: 2 turns.",0,0,0,0,2,1,false,effect_list.get(12),2,false));
		ability_list.add(8,new Abilities("Deflect","Trying to deflect enemy attack causing him to take damage instead. Chance is based on your and your's enemy strenght. \nCooldown: 3 turns. Uses 15 energy",0,0,0,0,3,15,false,effect_list.get(13),2,true));
		ability_list.add(9,new Abilities("Charge","Very fast attack, very hard to dodge, parry or block. It also has higher crit chance, and have 25% chance to stun opponent for 2 turns. Also lowers cooldown on dodge by 1.\nSimplified damage calculation: 150% weapon damage*strenght + 150% str, Cooldown: 2 turns, uses 6 energy.",15,150,0,20,2,6,true,effect_list.get(17),3,true));
		ability_list.add(10,new Abilities("Overpower","Very powerfull attack, has hight crit chance and can cause powerfull bleed.\nSimplified damage calculation:350% weapon damage*strenght + 250% str, Cooldown: 4 turns, uses 20 energy.",25,350,0,30,4,20,true,effect_list.get(1),1,true));
		ability_list.add(11,new Abilities("Hamstring","Cuts enemy tendom and slows him down. Also cause bleeding \nSimplified damage calculation: 105% weapon damage*strenght + 100% strenght uses 4 energy",10,105,0,0,2,3,true,effect_list.get(1),1,true));
		ability_list.add(12,new Abilities("Riposte","Fast counter attack, can be used only after parrying. Can cause bleeding and have bigger crit chance.\nSimplified damage calculation: 200% weapon damage*strenght + 100% strenght, uses 4 energy",10,200,0,35,0,3,true,effect_list.get(1),1.5,true));
		ability_list.add(13,new Abilities("Rend","Cutting artery. Causing big bleeding. Also causes bleeds to deal aditional 30% dmg.\nSimplified damage calculation: 75% weapon damage*strenght + 100% strenght, Cooldown: 2 turns, uses 5 energy ",10,50,0,5,4,5,true,effect_list.get(18),1,true));
		ability_list.add(14,new Abilities("Execute","Executing enemy. Can only be used if enemy is below 20% hp. Big crit chance and armor penetration.\nSimplified damage calculation: MASSIVE!, cooldown: 1 turn, uses 15 energy ",50,500,75,75,1,15,true,null,0,false));
		ability_list.add(15,new Abilities("Concusion Blow","Powerfull strike that tires and stun enemy.\nSimplified damage calculation: 75% weapon damage*strenght + 200% str, Cooldown 3 turns, uses 14 energy",20,75,25,10,3,14,true,effect_list.get(15),0,true));
		ability_list.add(16,new Abilities("Shield Bash","Fast strike, Tires enemy down, if used after blocking hit, stuns enemy 1 turn, and deals dobule damage.\nSimplified damage calculation:  150% str, uses 7 energy ",15,0,0,0,0,7,true,effect_list.get(4),1.5,false));
		ability_list.add(17,new Abilities("Impale","Powerfull strike that cause massive bleed. Has very hight chance to pierce armor and very hight crit chance. Can be used only if enemy is stuned.\nSimplified damage calculation: 300% weapon damage*strenght + 100%str, uses 12 energy ",10,300,75,75,0,12,true,effect_list.get(3),1,false));
		ability_list.add(18,new Abilities("Sunder Armor","Powerfull strike that damages enemy armor.\nCooldown: 4 turns, uses 12 energy",20,100,85,25,4,12,true,effect_list.get(16),1,false));
		ability_list.add(19,new Abilities("Struggle","You know nothing John Snow.",1,0,0,0,0,0,true,null,1,false));	
		ability_list.add(20,new Abilities("Bite","Ouch",10,0,0,5,0,5,true,effect_list.get(1),1,false));
		ability_list.add(21,new Abilities("Dirty Fighting","Stun 4 turns cd",10,0,0,5,4,10,true,effect_list.get(15),2,true));
		ability_list.add(22,new Abilities("Sweeping Strike","Powerfull swipe that has very hight critical strike chance. \n\nSimplified damage calculation: 200% weapon damage*strenght + 100% strenght, cooldown: 2 turns, uses 8 energy ",10,200,0,40,2,8,true,effect_list.get(15),1,true));
		ability_list.add(23,new Abilities("OutRage","Very powerfull attack",50,0,0,30,4,50,true,effect_list.get(1),1,true));
		ability_list.add(24,new Abilities("Wait","You wait for your opponent move",0,0,0,0,0,0,false,null,3,true));
		ability_list.add(25,new Abilities("Run Away","Try to run away",0,0,0,0,0,0,false,null,3,true));
		
//enemy stats should have 30 total to match player, 3 weapon to get 12 at 20, and 10 armor to get 100 at max
		//stamina, attack_power, speed, armor, weapon
		

		enemy_list = new ArrayList<Enemy>();
		
		enemy_list.add(0,new Enemy("Wolf",10,8,12,0,0,50,0));
		enemy_list.get(0).dps_skills.add(ability_list.get(20));
		enemy_list.get(0).deffensive_skills.add(ability_list.get(20));
		enemy_list.get(0).ulti_skills.add(ability_list.get(20));
		enemy_list.get(0).on_stun_skills.add(ability_list.get(20));
		
		enemy_list.add(1,new Enemy("Thief",8,8,14,3,2,80,10));
		enemy_list.get(1).dps_skills.add(new Abilities(ability_list.get(1)));
		enemy_list.get(1).dps_skills.add(new Abilities(ability_list.get(2)));
		enemy_list.get(1).deffensive_skills.add(new Abilities(ability_list.get(5)));
		enemy_list.get(1).deffensive_skills.add(new Abilities(ability_list.get(6)));
		enemy_list.get(1).ulti_skills.add(new Abilities(ability_list.get(21)));
		enemy_list.get(1).on_stun_skills.add(new Abilities(ability_list.get(2)));
		
		enemy_list.add(2,new Enemy("Bandit",10,8,12,6,2,100,15));
		enemy_list.get(2).dps_skills.add(new Abilities(ability_list.get(12)));
		enemy_list.get(2).dps_skills.add(new Abilities(ability_list.get(1)));
		enemy_list.get(2).dps_skills.add(new Abilities(ability_list.get(2)));
		enemy_list.get(2).dps_skills.add(new Abilities(ability_list.get(13)));
		enemy_list.get(2).deffensive_skills.add(new Abilities(ability_list.get(5)));
		enemy_list.get(2).deffensive_skills.add(new Abilities(ability_list.get(6)));
		enemy_list.get(2).ulti_skills.add(new Abilities(ability_list.get(21)));
		enemy_list.get(2).on_stun_skills.add(enemy_list.get(2).dps_skills.get(3));
		
		enemy_list.add(3,new Enemy("Knight",15,15,6,12,3,200,45)); // Sword
		enemy_list.get(3).dps_skills.add(new Abilities(ability_list.get(12)));
		enemy_list.get(3).dps_skills.add(new Abilities(ability_list.get(1)));
		enemy_list.get(3).dps_skills.add(new Abilities(ability_list.get(2)));
		enemy_list.get(3).deffensive_skills.add(new Abilities(ability_list.get(5)));
		enemy_list.get(3).dps_skills.add(new Abilities(ability_list.get(13)));
		enemy_list.get(3).on_stun_skills.add(new Abilities(ability_list.get(10)));
		enemy_list.get(3).ulti_skills.add(enemy_list.get(3).on_stun_skills.get(0));
		
		enemy_list.add(4,new Enemy("Knight",17,16,3,12,2.5,200,45)); // Mace
		enemy_list.get(4).dps_skills.add(new Abilities(ability_list.get(3)));
		enemy_list.get(4).dps_skills.add(new Abilities(ability_list.get(4)));
		enemy_list.get(4).deffensive_skills.add(new Abilities(ability_list.get(8)));
		enemy_list.get(4).ulti_skills.add(new Abilities(ability_list.get(15)));
		enemy_list.get(4).on_stun_skills.add(new Abilities(ability_list.get(10)));
		
		enemy_list.add(5,new Enemy("Knight",8,20,10,12,3,200,45)); // Sword/Broad
		enemy_list.get(5).dps_skills.add(new Abilities(ability_list.get(12)));
		enemy_list.get(5).dps_skills.add(new Abilities(ability_list.get(16)));
		enemy_list.get(5).dps_skills.add(new Abilities(ability_list.get(2)));
		enemy_list.get(5).deffensive_skills.add(new Abilities(ability_list.get(5)));
		enemy_list.get(5).deffensive_skills.add(new Abilities(ability_list.get(7)));
		enemy_list.get(5).deffensive_skills.add(new Abilities(ability_list.get(8)));
		enemy_list.get(5).ulti_skills.add(new Abilities(ability_list.get(15)));
		
		enemy_list.add(6,new Enemy("Gladiator",12,6,16,8,3,300,10)); // Spear
		enemy_list.get(6).dps_skills.add(new Abilities(ability_list.get(12)));
		enemy_list.get(6).dps_skills.add(new Abilities(ability_list.get(13)));
		enemy_list.get(6).dps_skills.add(new Abilities(ability_list.get(1)));
		enemy_list.get(6).deffensive_skills.add(new Abilities(ability_list.get(5)));
		enemy_list.get(6).deffensive_skills.add(new Abilities(ability_list.get(6)));
		enemy_list.get(6).ulti_skills.add(new Abilities(ability_list.get(9)));
		enemy_list.get(6).on_stun_skills.add(new Abilities(ability_list.get(17)));
		
		
		enemy_list.add(7,new Enemy("Troll",20,40,5,0,0,500,100));
		enemy_list.get(7).dps_skills.add(new Abilities(ability_list.get(3)));
		enemy_list.get(7).dps_skills.add(new Abilities(ability_list.get(4)));
		enemy_list.get(7).deffensive_skills.add(new Abilities(ability_list.get(8)));
		enemy_list.get(7).ulti_skills.add(new Abilities(ability_list.get(9)));
		enemy_list.get(7).on_stun_skills.add(new Abilities(ability_list.get(23)));
		
		enemy_list.add(8,new Enemy("Champion",20,16,8,14,4,500,100)); //Sword - op shit
		//public Ability_Effect(String n, int chance, int d, int dmg, int av, boolean s)
		//
		//ability to weapon class list
		//List <List <Pair<Integer,Integer>>> ability_to_weapon;
		ability_to_weapon = new ArrayList<List<Pair<Integer,Abilities>>>();
		
		//swords
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(0).add(0,new Pair<Integer, Abilities>(1, ability_list.get(1)));	//thrust
		ability_to_weapon.get(0).add(1,new Pair<Integer, Abilities>(1, ability_list.get(2)));	//slash
		ability_to_weapon.get(0).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		ability_to_weapon.get(0).add(3,new Pair<Integer, Abilities>(3, ability_list.get(13)));  // rend
		ability_to_weapon.get(0).add(4,new Pair<Integer, Abilities>(4, ability_list.get(5)));	// parry
		ability_to_weapon.get(0).add(5,new Pair<Integer, Abilities>(5, ability_list.get(12)));	//riposte
		ability_to_weapon.get(0).add(6,new Pair<Integer, Abilities>(6, ability_list.get(10)));	//overpower
		ability_to_weapon.get(0).add(7,new Pair<Integer, Abilities>(7, ability_list.get(14)));	//execute
		//spear
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(1).add(0,new Pair<Integer, Abilities>(1, ability_list.get(1)));   //thrust
		ability_to_weapon.get(1).add(1,new Pair<Integer, Abilities>(1, ability_list.get(11)));  //hamstring
		ability_to_weapon.get(1).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));   //dodge
		ability_to_weapon.get(1).add(3,new Pair<Integer, Abilities>(3, ability_list.get(13)));	//rend
		ability_to_weapon.get(1).add(4,new Pair<Integer, Abilities>(4, ability_list.get(5)));	//parry
		ability_to_weapon.get(1).add(5,new Pair<Integer, Abilities>(5, ability_list.get(9)));	//charge
		ability_to_weapon.get(1).add(6,new Pair<Integer, Abilities>(6, ability_list.get(12)));	//riposte
		ability_to_weapon.get(1).add(7,new Pair<Integer, Abilities>(7, ability_list.get(17)));	//impale
		
		//sword - and broad
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(2).add(0,new Pair<Integer, Abilities>(1, ability_list.get(16)));	//shield bash
		ability_to_weapon.get(2).add(1,new Pair<Integer, Abilities>(1, ability_list.get(2)));	//slash
		ability_to_weapon.get(2).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		ability_to_weapon.get(2).add(3,new Pair<Integer, Abilities>(3, ability_list.get(18)));	//sunder armor
		ability_to_weapon.get(2).add(4,new Pair<Integer, Abilities>(4, ability_list.get(5)));	//parry
		ability_to_weapon.get(2).add(5,new Pair<Integer, Abilities>(5, ability_list.get(12)));	//riposte
		ability_to_weapon.get(2).add(6,new Pair<Integer, Abilities>(6, ability_list.get(7)));	//block
		ability_to_weapon.get(2).add(7,new Pair<Integer, Abilities>(7, ability_list.get(14)));	//execute
		
		//
		//axe
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(3).add(0,new Pair<Integer, Abilities>(1, ability_list.get(3)));	//smash
		ability_to_weapon.get(3).add(1,new Pair<Integer, Abilities>(1, ability_list.get(2)));	//slash
		ability_to_weapon.get(3).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		ability_to_weapon.get(3).add(3,new Pair<Integer, Abilities>(3, ability_list.get(22)));	//sweeping
		ability_to_weapon.get(3).add(4,new Pair<Integer, Abilities>(4, ability_list.get(8)));	//defflect
		ability_to_weapon.get(3).add(5,new Pair<Integer, Abilities>(5, ability_list.get(9)));	//charge
		ability_to_weapon.get(3).add(6,new Pair<Integer, Abilities>(6, ability_list.get(10)));	//overpower
		ability_to_weapon.get(3).add(7,new Pair<Integer, Abilities>(7, ability_list.get(14))); //execute
		
		//mace
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(4).add(0,new Pair<Integer, Abilities>(1, ability_list.get(3)));	//smash
		ability_to_weapon.get(4).add(1,new Pair<Integer, Abilities>(1, ability_list.get(4)));	//crushing blow
		ability_to_weapon.get(4).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		ability_to_weapon.get(4).add(3,new Pair<Integer, Abilities>(3, ability_list.get(18)));	//sunder armor
		ability_to_weapon.get(4).add(4,new Pair<Integer, Abilities>(4, ability_list.get(8)));	//defflect
		ability_to_weapon.get(4).add(5,new Pair<Integer, Abilities>(5, ability_list.get(15)));	//concussion blow
		ability_to_weapon.get(4).add(6,new Pair<Integer, Abilities>(6, ability_list.get(10)));	//overpower
		ability_to_weapon.get(4).add(7,new Pair<Integer, Abilities>(7, ability_list.get(14))); //execute
		
		// one hand sword
		
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(5).add(0,new Pair<Integer, Abilities>(1, ability_list.get(1)));	//thrust
		ability_to_weapon.get(5).add(1,new Pair<Integer, Abilities>(1, ability_list.get(2)));	//slash
		ability_to_weapon.get(5).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		ability_to_weapon.get(5).add(3,new Pair<Integer, Abilities>(3, ability_list.get(5)));	// parry
		ability_to_weapon.get(5).add(4,new Pair<Integer, Abilities>(4, ability_list.get(12)));	//riposte
		
		// no weapon - hand
		
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		
		ability_to_weapon.get(6).add(0,new Pair<Integer, Abilities>(1, ability_list.get(0)));	//jab
		ability_to_weapon.get(6).add(1,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		
		
		// shield + no weapon
		ability_to_weapon.add(new ArrayList<Pair<Integer,Abilities>>());
		

		ability_to_weapon.get(7).add(0,new Pair<Integer, Abilities>(1, ability_list.get(16)));	//shield bash
		ability_to_weapon.get(7).add(1,new Pair<Integer, Abilities>(1, ability_list.get(0)));	//jab
		ability_to_weapon.get(7).add(2,new Pair<Integer, Abilities>(2, ability_list.get(6)));	//dodge
		ability_to_weapon.get(7).add(3,new Pair<Integer, Abilities>(3, ability_list.get(18)));	//sunder armor
		ability_to_weapon.get(7).add(4,new Pair<Integer, Abilities>(4, ability_list.get(7)));	//block
		
		
		event_list = new ArrayList<Event>();
		event_list.add(0,new Encounter(enemy_list.get(0),1,2,"You woke up in middle of cold dark room. Your head hurts, and you don't remember how you got here. Suddenly you hear unsettling noise, you instinctively grab something that resembles weapon to defend your self!",new Items(item_list.get(0)),this));
		event_list.add(1, new Event("It appears you found exit from labirynth! Do you want to proced?",true));
		
	}
}
