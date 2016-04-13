import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.util.Random;

/**
 * Handles combat system. Most encounters are generated dynamically during game, and have scalling enemy strenght, as well as randomized rewards (to be implemented).
 * Special not scaled encounter are defined inside database class.
 */
public class Encounter extends Event {
	/** Enemy object*/
	Enemy enemy;							/** Player object*/
	Player player;							/** Gui Element*/
	JTextPane desc;							/** Gui Element*/
	JLabel player_hp;						/** Gui Element*/
	JLabel player_fatigue;					/** Gui Element*/
	JLabel enemy_name;						/** Gui Element*/
	JLabel enemy_hp;						/** Gui Element*/
	JLabel enemy_fatigue;					/** Enemy Ability, chosen by "AI_Mood function"*/
	Abilities enemy_skill;					/** Data: If true, player attacks first*/
	boolean first;							/** Data: If true, player avoided attack*/
	boolean player_avoided;					/** Data: If true, enemy avoided attack*/
	boolean enemy_avoided;					/** Data: If true, player parried attack*/
	boolean player_parried;					/** Data: If true, enemy parried attack*/
	boolean enemy_parried;					/** Data: If true, player blocked attack*/
	boolean player_blocked;					/** Data: If true, enemy blocked attack*/
	boolean enemy_blocked;					/** Data: Text that will be outputed in gui*/
	String text;							/** Data: Item reward*/
	Items item;								/** Database object*/
	Database data;							/** Data: If true, player successfully ran away*/
	boolean run_away;
	
	/**
	 * Normal constructor
	 * @param e enemy
	 * @param lv player level 
	 * @param b enemy behavior
	 * @param t enemy description
	 * @param it item reward
	 * @param datas Database object
	 */
	public Encounter(Enemy e,int lv,int b, String t,Items it,Database datas)
	{
		super(t);
		if(it != null)
		{
			if(it.getClass() == Armor.class) item = new Armor((Armor) it);
			else if(it.getClass() == Weapon.class) item = new Weapon((Weapon) it);
			else item = new Items(it);
		}
		data = datas;
		enemy = new Enemy(e,lv,b);
		description = t;
	}
	public Encounter(Encounter e) {
		super(e);
		description = e.description;
		automatic = e.automatic;
		enemy = e.enemy;
		text = e.text;
		item = e.item;
		
	}
	/**
	 * This function is ran, before starting up combat.
	 * @param p player object
	 * @param de description gui element
	 * @param php player hp gui element
	 * @param pf player energy gui element
	 * @param en enemy name gui element
	 * @param ehp enemy hp gui element
	 * @param ef enemy energy gui element
	 */
	public void set(Player p, JTextPane de, JLabel php, JLabel pf, JLabel en, JLabel ehp, JLabel ef)
	{
		player = p;
		desc = de;
		player_hp = php;
		player_fatigue = pf;
		enemy_name = en;
		enemy_hp = ehp;
		enemy_fatigue = ef;
		desc.setText(description);
		enemy_name.setText(enemy.Name);
		enemy_hp.setText(Double.toString(enemy.hitpoints)+"/"+Integer.toString(enemy.stamina*20));
		enemy_fatigue.setText((Double.toString(enemy.fatigue)+"/"+"100"));
		
		enemy_parried = false;
		enemy_avoided = false;
		player_parried = false;
		player_avoided = false;
	}
	// ability dmg = weapon_dmg * weapon_mod / 100 * str/10 + base_dmg/10 * str/10
	/**
	 * Restoring player abilities cooldown's timers to 0 after combat
	 */
	void player_restore()
	{
		int i = 0;
		while(i<8 && player.skills[i] != null)
		{
			player.skills[i].cooldown_timer = 0;
			i++;
		}
	}
	/**
	 * Calculates outcome of player attack
	 * @param s number of element of player abilities array
	 */
	void player_attacks(int s)
	{
		player_parried = false;
		player_blocked = false;
		boolean stun = false;
		int av = 0;
		int len = player.effects.size();
		for(int i = 0; i<len; i++)
		{
			if(player.effects.get(i).stun == true) {player.effects.get(i).duration--; stun = true;}
		}
		if(stun == true) text = text + player.name + " tries to move, but is stunned!";
		len = enemy.effects.size();
		if(first == false)
		for(int i = 0; i<len; i++)
		{
			if(enemy.effects.get(i).avoidance != 0) av = enemy.effects.get(i).avoidance;
		}		

		if(stun == false)
		if(player.skills[s].stamina_used<=player.fatigue)
		{
			player.skills[s].cooldown_timer = player.skills[s].cooldown;
			if(player.skills[s].target == true)
			{
				//checking if enemy is using avoidance mechanic
				//avoidance
				//putting ability on cd
				
				if(av != 0)
				{	
					if(av == 1)//parry
					{
						if(player.skills[s].can_be_parried == true)
						{
							text = text + player.name + " tried to use " + player.skills[s].name + " but " + enemy.Name + " parried it!";
							player.fatigue = player.fatigue - player.skills[s].stamina_used;
							enemy_parried = true;
							return;
						}
						else
						{
							text =  text + enemy.Name + " tried to parry incoming attack, but it failed! ";
						}
					}else
					if(av == 2)//dodge
					{
						double chance = 50*(enemy.speed/player.agility)+50*enemy.fatigue/100;
						Random randomGenerator = new Random();
						int random = randomGenerator.nextInt(100);
						if(chance >= random) 
						{
							text =  text + player.name + " tried to use " + player.skills[s].name + " but " + enemy.Name + " dodged it!";
							player.fatigue = player.fatigue - player.skills[s].stamina_used;
							return;
						}
						else
						{
							text =  text + enemy.Name + " tried to dodge incoming attack, but it failed! ";
						}
					}else
					if(av == 3)// block
					{
						if(enemy.fatigue > player.skills[s].stamina_used*((player.strenght)/enemy.stamina*2))
						{
							enemy_blocked = true;
							text =  text + player.name + " tried to use " + player.skills[s].name + " but " + enemy.Name + "blocked it!";
							player.fatigue = player.fatigue - player.skills[s].stamina_used;
							enemy.fatigue = enemy.fatigue - player.skills[s].stamina_used*(player.strenght/enemy.stamina*2);
							return;
						}
						else
						{
							text =  text + enemy.Name + " tried to block incoming attack, but he was too tired! ";
							enemy.fatigue = 0;
							
						}
					}else
					if(av == 4)//deflect
					{
						double chance = 35 * enemy.attack_power/player.strenght + 35*enemy.fatigue/100;
						Random randomGenerator = new Random();
						int random = randomGenerator.nextInt(100);
						if(chance >= random)
						{
							text =  text + player.name + " tried to use " + player.skills[s].name + " but " + enemy.Name + " deflected it!";
							player.fatigue = player.fatigue - player.skills[s].stamina_used;
							double dmg = (player.skills[s].weapon_damage_mod*(player.weapon1.weapon_damage/100) * player.strenght/10 + (player.skills[s].base_Damage/10)*player.strenght) * (0.5 + player.fatigue/200);
							player.hitpoints = player.hitpoints - dmg;
							text = player.name + " took" + dmg + " damage" + " instead!";
							return;
						}
						else
						{
							text =  text + enemy.Name + " tried to deflect incoming attack, but it failed! ";
						}
					}
				}
				double dmg =  (player.skills[s].weapon_damage_mod*(player.weapon1.weapon_damage/100) * player.strenght/10 + (player.skills[s].base_Damage/10)*player.strenght) * (0.5 + player.fatigue/200);
				if(player_blocked == true && player.skills[s].name.equals("Shield Bash") == true)
				{
					enemy.effects.add(new Ability_Effect(data.effect_list.get(15)));
					dmg = dmg * 2;
				}
				Random randomGenerator = new Random();
				int randomcrit = randomGenerator.nextInt(100);
				boolean crit = false;
				if(randomcrit <= player.skills[s].critical_strike) {crit = true; dmg = dmg * 2;}
				double armor_calc = enemy.armor - enemy.armor*(player.skills[s].armor_pen/100);  
				dmg = dmg - armor_calc;
				if(player.skills[s].effect != null)if(player.skills[s].effect.fatigue > 0) enemy.fatigue = enemy.fatigue - player.skills[s].effect.fatigue;
				if(dmg > 0) 
				{
					enemy.hitpoints = enemy.hitpoints - (int)dmg;
					text =  text + player.name +" uses " + player.skills[s].name +  " and deals "+ dmg + "damage to " + enemy.Name + ".";
					if(crit)
					{
						text = text +  " It's supper effective!";
					}
					if(player.skills[s].effect != null)
					{
						int random = randomGenerator.nextInt(100);
						if(random <= player.skills[s].effect.apply_chance)
						{
							if(player.skills[s].effect.stun == true) text = text +  " Hit stuns " + enemy.Name + "!";
							if(player.skills[s].effect.damage > 0) text = text +  enemy.Name +" bleeds!";
							enemy.effects.add(new Ability_Effect(player.skills[s].effect));
						}
						
					}
					if(player.skills[s].name.equals("Rush") == true && player.skills[1].cooldown_timer>0) player.skills[1].cooldown_timer--;
					if(player.skills[s].name.equals("Hamstring") == true) {text = text + player.name +" slows" + enemy.Name + " down.";enemy.speed = enemy.speed*10/100;}
					if(player.skills[s].name.equals("Sunder Armor") == true) {text = text + player.name + "crushes armor of" + enemy.Name + "."; enemy.armor = enemy.armor * 25/100;}
				}
				else
				{
					text = text + player.name + " uses " + player.skills[s] + " but it's not strong enough to penetrate armor.";
				}
					
				player.fatigue = player.fatigue - player.skills[s].stamina_used;
			}
			else
			{
				
				if(player.skills[s].name.equals("Wait")){text = text + player.name + " does nothing"; return;}
				if(player.skills[s].name.equals("Run Away")){
					Random randomGenerator = new Random();
					int random = randomGenerator.nextInt(75)+25;
					if(player.agility/enemy.speed * 35 > random) run_away = true;
					else{text = text +player.name + "tried to run away but wasn't fast enough!";}
					return;
				}
				if(enemy_avoided == true) text = text + enemy.Name + " used " + enemy_skill.name + " while " + player.name + " used" + player.skills[s] + ". Thats just silly!";
				player.effects.add(new Ability_Effect(player.skills[s].effect));
				player.fatigue = player.fatigue - player.skills[s].stamina_used;
				//self abilities like parry 
				player_avoided = true;
			}
		}
	}
	/**
	 * Calculates outcome of enemy attack
	 * @param s number of element of player abilities array
	 */
	void enemy_attacks(int s)
	{
		enemy_blocked = false;
		enemy_avoided = false;
		enemy_parried = false;
		boolean stun = false;
		int av = 0;
		int len = enemy.effects.size();
		for(int i = 0; i<len; i++)
		{
			if(enemy.effects.get(i).stun == true) {enemy.effects.get(i).duration--; stun = true;}
		}
		if(stun == true) text = text + enemy.Name + " tries to move, but is stunned!";
		len = player.effects.size();
		if(first == true)
		for(int i = 0; i<len; i++)
		{
			if(player.effects.get(i).avoidance != 0) av = player.effects.get(i).avoidance;
			
		}		
		if(stun == false)
		if(enemy_skill.stamina_used<=enemy.fatigue)
		{

			if(enemy_skill.target == true)
			{
				//checking if enemy is using avoidance mechanic
				//avoidance
				//putting ability on cd
				if(av != 0)
				{	
					if(av == 1)//parry
					{
						if(enemy_skill.can_be_parried == true)
						{
							text = text + enemy.Name + " tried to use " + enemy_skill.name + " but " + player.name + " parried it!";
							enemy.fatigue = enemy.fatigue - enemy_skill.stamina_used;
							player_parried = true;
							return;
						}
						else
						{
							text =  text + player.name + " tried to parry incoming attack, but it failed! ";
						}
					}else
					if(av == 2)//dodge
					{
						double chance = 50*(player.agility/enemy.speed)+50*player.fatigue/100;
						Random randomGenerator = new Random();
						int random = randomGenerator.nextInt(100);
						if(chance >= random) 
						{
							text = text + enemy.Name + " tried to use " + enemy_skill.name + " but " + player.name + " dodged it!";
							enemy.fatigue = enemy.fatigue - enemy_skill.stamina_used;
							return;
						}
						else
						{
							text =  text + player.name + " tried to dodge incoming attack, but it failed! ";
						}
					}else
					if(av == 3)// block
					{
						if(player.fatigue > enemy_skill.stamina_used*(enemy.attack_power/(player.stamina + player.shield.armor)))
						{
							player_blocked = true;
							text = text + enemy.Name + " tried to use " + enemy_skill.name + " but " + player.name + " blocked it!";
							enemy.fatigue = enemy.fatigue - enemy_skill.stamina_used;
							player.fatigue = player.fatigue - enemy_skill.stamina_used*(enemy.attack_power/(player.stamina + player.shield.armor));
							return;
						}
						else
						{
							player.fatigue = 0;
							text =  text + player.name + " tried to block incoming attack, but was too tired! ";
							
						}
					}else
					if(av == 4)//deflect
					{
						double chance = 35 * player.strenght/enemy.attack_power + 35*player.fatigue/100;
						Random randomGenerator = new Random();
						int random = randomGenerator.nextInt(100);
						if(chance >= random)
						{
							text = text + enemy.Name + " tried to use " + enemy_skill.name + " but " + player.name + " deflected it!";
							enemy.fatigue = enemy.fatigue - enemy_skill.stamina_used;
							double dmg = (enemy_skill.weapon_damage_mod*(enemy.weapon_damage/100) * enemy.attack_power/10 + (enemy_skill.base_Damage/10)*enemy.attack_power) * (0.5 + enemy.fatigue/200);
							enemy.hitpoints = enemy.hitpoints - dmg;
							text = enemy.Name + " took" + dmg + " damage" + " instead!";
							return;
						}
						else
						{
							text =  text + player.name + " tried to deflect incoming attack, but it failed! ";
						}
					}
				}
				double dmg =  (enemy_skill.weapon_damage_mod*(enemy.weapon_damage/100) * enemy.attack_power/10 + (enemy_skill.base_Damage/10)*enemy.attack_power) * (0.5 + enemy.fatigue/200);
				if(enemy_blocked == true && enemy_skill.name.equals("Shield Bash") == true)
					{
						player.effects.add(new Ability_Effect(data.effect_list.get(15)));
						dmg = dmg * 2;
					}
				Random randomGenerator = new Random();
				boolean crit = false;
				int randomcrit = randomGenerator.nextInt(100);
				if(randomcrit <= enemy_skill.critical_strike){crit = true; dmg = dmg * 2;}
				double armor_calc = player.armor.armor;
				if(player.shield != null){
					armor_calc = armor_calc+ player.shield.armor;
				}
				armor_calc =  armor_calc - armor_calc * (enemy_skill.armor_pen/100);  
				dmg = dmg - armor_calc;
				if(enemy_skill.effect != null)if(enemy_skill.effect.fatigue > 0) player.fatigue = player.fatigue - enemy_skill.effect.fatigue;
				if(dmg > 0) 
				{
					
					player.hitpoints = player.hitpoints - (int)dmg;
					text =  text + enemy.Name +" uses " + enemy_skill.name + " and deals " + dmg + " damage to " + player.name + "." ;
					if(crit)
					{
						text = text +  "It's supper effective!";
					}
					if(enemy_skill.effect != null)
						{
							if(enemy_skill.name.equals("Rush") == true && enemy.deffensive_skills.get(1).cooldown_timer>0) enemy.deffensive_skills.get(1).cooldown_timer--;
							int random = randomGenerator.nextInt(100);
							if(random <= enemy_skill.effect.apply_chance)
							{
								player.effects.add(new Ability_Effect(enemy_skill.effect));
							}
						}
				}
				else
				{
					text = text + enemy.Name + " uses " + enemy_skill.name + " but it's not strong enough to penetrate armor.";
				}
				enemy.fatigue = enemy.fatigue - enemy_skill.stamina_used;
			}
			else
			{
				if(player_avoided == true) text =enemy.Name + " used " + enemy_skill.name + " while " + player.name + " used" + player.skills[s] + ". Thats just silly!";
				enemy.effects.add(new Ability_Effect(enemy_skill.effect));
				enemy.fatigue = enemy.fatigue - enemy_skill.stamina_used;
				//self abilities like parry 
				enemy_avoided = true;
			}
		}
	}
	/**
	 * Chooses which ability ai will use. Selection is based on: what player and enemy did next turn, if player or enemy are stunted / have low hp. 
	 * Selection is somewhat randomized. Behavior of enemy have huge effect.
	 * @return
	 */
	Abilities ai_mood()
	{
		boolean stun = false;
		int len = player.effects.size();
		for(int i = 0; i<len; i++)
		{
			if(player.effects.get(i).stun == true) {stun = true;}
		}
		// if player is stunned try to use "on stun ability"
		if(stun == true && (enemy.on_stun_skills.isEmpty() == false))
		{
			if(enemy.on_stun_skills.get(0).stamina_used<= enemy.fatigue && enemy.on_stun_skills.get(0).cooldown_timer == 0 )
			{
				return enemy.on_stun_skills.get(0);
			}
			
		}
		if(player_blocked == true)
		{
			//after block there is hight chance that player will stun enemy with shield bash, so enemy will try to dodge/deflect/block
			Random randomGenerator = new Random();
			int random = randomGenerator.nextInt(100);
			int defensive_chance=0;
			if(enemy.behaviour == 1)defensive_chance = 25;
			if(enemy.behaviour == 2)defensive_chance = 65;
			if(enemy.behaviour == 3)defensive_chance = 90;
			if(defensive_chance >= random)
			{
				len = enemy.deffensive_skills.size();
				for(int i=0; i< len; i++)
				{	
					if(enemy.deffensive_skills.get(i).effect.avoidance != 1)
					if(enemy.deffensive_skills.get(i).stamina_used<= enemy.fatigue && enemy.deffensive_skills.get(i).cooldown_timer == 0 ) {enemy_avoided = true; return enemy.deffensive_skills.get(i);}
				}
			}
		}
		if(enemy_blocked == true)
		{
			//chance to use shield bash to stun enemy
			Random randomGenerator = new Random();
			int random = randomGenerator.nextInt(100);
			if(random < 80)
			{
				return enemy.dps_skills.get(1);
			}
		}
		// if player is low try to finish with ulti
		if(player.hitpoints<player.stamina*20*(20/100))
		{
			//if player is really low try to use priority attack, if enemy doesn't have any try to use other attack or defend if he is deffensive and is low as well
			if(player.hitpoints<player.stamina*20*(10/100))
			{
				len = enemy.dps_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.dps_skills.get(i).name.equals("Riposte") == true && enemy_parried == false) continue;
					if(enemy.dps_skills.get(i).priority>1&&enemy.dps_skills.get(i).stamina_used<= enemy.fatigue && enemy.dps_skills.get(i).cooldown_timer == 0) return enemy.dps_skills.get(i);
				}
				len = enemy.ulti_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.ulti_skills.get(i).priority>1&&enemy.ulti_skills.get(i).stamina_used<= enemy.fatigue && enemy.ulti_skills.get(i).cooldown_timer == 0) return enemy.ulti_skills.get(i);
				}
			}
			// if enemy is low as well, have chance to defend as well if defensive
			if(enemy.hitpoints<enemy.stamina*20*(10/100))
			{
				Random randomGenerator = new Random();
				int random = randomGenerator.nextInt(100);
				int defensive_chance=0;
				if(enemy.behaviour == 1)defensive_chance = 1;
				if(enemy.behaviour == 2)defensive_chance = 20;
				if(enemy.behaviour == 3)defensive_chance = 60;
				if(defensive_chance >= random)
				{
					len = enemy.deffensive_skills.size();
					for(int i=0; i< len; i++)
					{
						if(enemy.deffensive_skills.get(i).stamina_used<= enemy.fatigue && enemy.deffensive_skills.get(i).cooldown_timer == 0) {enemy_avoided = true; return enemy.deffensive_skills.get(i);}
					}
				}
			}
			//try to use ulti to finish player off
			len = enemy.ulti_skills.size();	
			for(int i=0; i< len; i++)
			{
				if(enemy.ulti_skills.get(i).stamina_used<= enemy.fatigue && enemy.ulti_skills.get(i).cooldown_timer == 0) return enemy.ulti_skills.get(i);
			}
			//if not enough energy or ulti on cd try to use basic abilities
			len = enemy.dps_skills.size();	//try to use ulti to finish player off
			for(int i=0; i< len; i++)
			{
				if(enemy.dps_skills.get(i).name.equals("Riposte") == true) continue;
				if(enemy.dps_skills.get(i).stamina_used<= enemy.fatigue && enemy.dps_skills.get(i).cooldown_timer == 0) return enemy.dps_skills.get(i);
			}
			
		}
		// if enemy avoided, counter attack(maybe ulti, or if defensive try to defend again!
		if(enemy_avoided)
		{
			Random randomGenerator = new Random();
			int defensive_chance=50;
			int ulti_chance=50;
			if(enemy.behaviour == 1){defensive_chance = 5;ulti_chance = 50;}
			if(enemy.behaviour == 2){defensive_chance = 30;ulti_chance = 20;}
			if(enemy.behaviour == 3){defensive_chance = 70;ulti_chance = 1;}
			int random = randomGenerator.nextInt(100);
			//defense chance
			if(defensive_chance > random)
			{
				len = enemy.deffensive_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.deffensive_skills.get(i).stamina_used<= enemy.fatigue && enemy.deffensive_skills.get(i).cooldown_timer == 0) {enemy_avoided = true; return enemy.deffensive_skills.get(i);}
				}
			}
			random = randomGenerator.nextInt(100);
			if(ulti_chance > random)
			{
				len = enemy.ulti_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.ulti_skills.get(i).stamina_used<= enemy.fatigue && enemy.ulti_skills.get(i).cooldown_timer == 0) return enemy.ulti_skills.get(i);
				}
			}
			if(enemy_parried == true)
			{
				enemy_parried = false;
				if(enemy.dps_skills.get(0).stamina_used<= enemy.fatigue)
				return enemy.dps_skills.get(0);
			}				
		}
		//if under 20 try to defend
		if(enemy.hitpoints<enemy.stamina*20*(10/100))
		{
			Random randomGenerator = new Random();
			int random = randomGenerator.nextInt(100);
			int defensive_chance=0;
			if(enemy.behaviour == 1)defensive_chance = 50;
			if(enemy.behaviour == 2)defensive_chance = 75;
			if(enemy.behaviour == 3)defensive_chance = 100;
			if(defensive_chance >= random)
			{
				len = enemy.deffensive_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.deffensive_skills.get(i).stamina_used<= enemy.fatigue && enemy.deffensive_skills.get(i).cooldown_timer == 0) {enemy_avoided = true; return enemy.deffensive_skills.get(i);}
				}
			}
		}
		// if player avoided either avoid or use ult, if player parried bigger chance to defend
		//behiaviour and chance decides what happens
		if(player_avoided == true)
		{
			Random randomGenerator = new Random();
			int defensive_chance=0;
			int ulti_chance=0;
			if(player_parried == true)
			{
				if(enemy.behaviour == 1){defensive_chance = 2;ulti_chance = 75;}
				if(enemy.behaviour == 2){defensive_chance = 20;ulti_chance = 50;}
				if(enemy.behaviour == 3){defensive_chance = 40;ulti_chance = 20;}
			}
			else
			{
				if(enemy.behaviour == 1){defensive_chance = 1;ulti_chance = 75;}
				if(enemy.behaviour == 2){defensive_chance = 10;ulti_chance = 50;}
				if(enemy.behaviour == 3){defensive_chance = 20;ulti_chance = 20;}
			}
			int random = randomGenerator.nextInt(100);
			//defense chance
			if(defensive_chance > random)
			{
				len = enemy.deffensive_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.deffensive_skills.get(i).stamina_used<= enemy.fatigue && enemy.deffensive_skills.get(i).cooldown_timer == 0) {enemy_avoided = true; return enemy.deffensive_skills.get(i);  }
				}
			}
			random = randomGenerator.nextInt(100);
			if(ulti_chance > random)
			{
				len = enemy.ulti_skills.size();
				for(int i=0; i< len; i++)
				{
					if(enemy.ulti_skills.get(i).stamina_used<= enemy.fatigue && enemy.ulti_skills.get(i).cooldown_timer == 0) return enemy.ulti_skills.get(i);
				}
			}
		}

		//otherwise basic attack, smaller random chance on defense or ulti
		Random randomGenerator = new Random();
		int random = randomGenerator.nextInt(100);
		int defensive_chance=0;
		int ulti_chance=0;
		if(enemy.behaviour == 1){defensive_chance = 10;ulti_chance = 20;}
		if(enemy.behaviour == 2){defensive_chance = 20;ulti_chance = 10;}
		if(enemy.behaviour == 3){defensive_chance = 30;ulti_chance = 2;}
		if(defensive_chance > random)
		{
			len = enemy.deffensive_skills.size();
			for(int i=0; i< len; i++)
			{
				if(enemy.deffensive_skills.get(i).stamina_used<= enemy.fatigue && enemy.deffensive_skills.get(i).cooldown_timer == 0) {enemy_avoided = true; return enemy.deffensive_skills.get(i);}
			}
		}
		random = randomGenerator.nextInt(100);
		if(ulti_chance > random)
		{
			len = enemy.ulti_skills.size();
			for(int i=0; i< len; i++)
			{
				if(enemy.ulti_skills.get(i).stamina_used<= enemy.fatigue && enemy.ulti_skills.get(i).cooldown_timer == 0) return enemy.ulti_skills.get(i);
			}
		}
		//random basic attack
		len = enemy.dps_skills.size();
		random = randomGenerator.nextInt(len);
		if(enemy.dps_skills.get(random).stamina_used<enemy.fatigue) return enemy.dps_skills.get(random);
		return new Abilities(data.ability_list.get(19));
		
		//have to return "wait" ability, in case enemy is too tired
	}
	/**
	 * Function lowers cooldown timers after each turn
	 */
	public void timers()
	{

		for(int i = 0; i< 8; i++)
		{
			if(player.skills[i] == null) break;
			else if(player.skills[i].cooldown_timer>0) player.skills[i].cooldown_timer--;
		}
		int len = enemy.dps_skills.size();
		for(int i = 0; i< len; i++)
		{
			if(enemy.dps_skills.get(i).cooldown_timer>0) enemy.dps_skills.get(i).cooldown_timer--;
		}
		len = enemy.ulti_skills.size();
		for(int i = 0; i< len; i++)
		{
			if(enemy.ulti_skills.get(i).cooldown_timer>0) enemy.ulti_skills.get(i).cooldown_timer--;
		}
		len = enemy.deffensive_skills.size();
		for(int i = 0; i< len; i++)
		{
			if(enemy.deffensive_skills.get(i).cooldown_timer>0) enemy.deffensive_skills.get(i).cooldown_timer--;
		}
		len = enemy.on_stun_skills.size();
		for(int i = 0; i< len; i++)
		{
			if(enemy.on_stun_skills.get(i).cooldown_timer>0) enemy.on_stun_skills.get(i).cooldown_timer--;
		}
	}
	/**
	 * Function calculates outcome of damage over time effects (Bleeds) or healing over time effects (regeneration)
	 */
	void dots_and_effects()
	{
		double bleed = 0;
		int len = enemy.effects.size();
		boolean trauma = false;
		for(int i = 0; i < len; i++)
		{
			if(enemy.effects.get(i).name.equals("Trauma")==true) trauma = true;
			bleed = bleed + enemy.effects.get(i).damage;
			enemy.effects.get(i).duration--;
			if(enemy.effects.get(i).duration == 0)
			{
				enemy.effects.remove(i);
				i--;
				len--;
			}
		}
		if(trauma == true) bleed = bleed *130/100;
		bleed = enemy.hitpoints*bleed/100;
		enemy.hitpoints = enemy.hitpoints - bleed;
		if(bleed >0)text = text + enemy.Name +" takes " + bleed + " bleed damage";
		if(bleed < 0) text = text + enemy.Name +" regenerates " + bleed + " hitpoints";
		trauma = false;
		bleed = 0;
		len = player.effects.size();
		for(int i = 0; i < len; i++)
		{
			if(player.effects.get(i).name.equals("Trauma")==true) trauma = true;
			bleed = bleed + player.effects.get(i).damage;
			if(player.effects.get(i).stun == false)player.effects.get(i).duration--;
			if(player.effects.get(i).duration == 0)
			{
				player.effects.remove(i);
				i--;
				len--;
			}
		}
		if(trauma == true) bleed = bleed * 130/100;
		bleed = player.hitpoints*bleed/100;
		player.hitpoints = player.hitpoints - bleed;
		if(bleed > 0) text = text + player.name +" takes " + bleed + " bleed damage";
		if(bleed < 0) text = text + player.name +" regenerates " + bleed + " hitpoints";
		//restoring portion of energy/fatigue
		player.fatigue = player.fatigue + player.stamina/10;
		enemy.fatigue = enemy.fatigue + enemy.stamina/10;

	}	
	/**
	 * Function updates gui elements after each turn, or if combat is over
	 */
	void update_status()
	{
		if(player.hitpoints>player.stamina*20) player.hitpoints = player.stamina*20;
		if(enemy.hitpoints>enemy.stamina*20) enemy.hitpoints = enemy.stamina*20;
		if(player.hitpoints<0) player.hitpoints = 0;
		if(enemy.hitpoints<0) enemy.hitpoints = 0;
		if(player.fatigue<0) player.fatigue = 0;
		if(enemy.fatigue<0) enemy.fatigue = 0;
		if(player.fatigue>100) player.fatigue = 100;
		if(enemy.fatigue>100) enemy.fatigue = 100;
		desc.setText(text);
		player_hp.setText(Integer.toString((int)player.hitpoints)+"/"+Integer.toString((int)player.stamina*20));
		player_fatigue.setText(Integer.toString((int)player.fatigue)+"/"+"100");
		enemy_hp.setText(Integer.toString((int)enemy.hitpoints)+"/"+Integer.toString((int)enemy.stamina*20));
		enemy_fatigue.setText((Integer.toString((int)enemy.fatigue)+"/"+"100"));
	}
	/**
	 * This function handles everything that happens each turn. 
	 * @param s Player skill(position in array)
	 * @return returns outcome of battle (0 - nothing happend, -1 lose, 1 player win, 2 player run away) 
	 */
	public int run(int s)
	{
		text = "";
		//ai chosing skill
		enemy_skill = ai_mood();
		player_avoided = enemy_avoided = false;
		
		//callculating who goes first
		first = false; // who attacks first
		//boolean player_move = false;
		if(player.skills[s].priority>enemy_skill.priority) first = true;
		else if(player.skills[s].priority<enemy_skill.priority) first = false;
		if(player.skills[s].priority==enemy_skill.priority) if(player.agility*player.fatigue/100>=enemy.speed*enemy.fatigue/100) first = true;
		else first = false;
		
		//lowering cooldown on abilities
		timers();
		
		//calculating outcome of turn
		if(first == true) {player_attacks(s);text = text + "\n";}
		if(player.hitpoints<=0){text = text + player.name +" dies!"; update_status();  return -1;}
		if(enemy.hitpoints<=0){text = text + enemy.Name +" dies!"; update_status();player_restore(); return 1;}
		//checking if someone died
		if(run_away) {update_status(); return 2;}
		enemy_attacks(s);
		text = text + "\n";
		if(player.hitpoints<=0){text = text + player.name +" dies!";  update_status();return -1;}
		if(enemy.hitpoints<=0){text = text + enemy.Name +" dies!";  update_status();player_restore();return 1;}
		//checking if someone died
		if(first == false) {player_attacks(s);text = text + "\n";}
		//checking if someone died
		if(player.hitpoints<=0){text = text + player.name +" dies!";  update_status();return -1;}
		if(enemy.hitpoints<=0){text = text + enemy.Name +" dies!"; update_status(); player_restore();return 1;}
		if(run_away) {update_status(); return 2;}
		
		//dot damage and handling effects 
	
		dots_and_effects();
		if(player.hitpoints == 0 && enemy.hitpoints > 0) {text = text + player.name + " bleed to death!";update_status(); return -1;}
		else if(enemy.hitpoints==0 && player.hitpoints>0) {text = text + enemy.Name + " bleed to death!";update_status();player_restore(); return 1;}
		else if(player.hitpoints == 0 && enemy.hitpoints == 0) {text = text + "Both " + player.name + " and " + enemy.Name + " bleed to death!";update_status(); return -1;}
		update_status();
		//changing texts
		return 0;

	}
	
	
}
