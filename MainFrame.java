import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JSplitPane;
import javax.swing.JRadioButton;
import javax.swing.JList;


import javax.swing.JScrollBar;
import javax.swing.JMenu;

import java.awt.Panel;

import javax.swing.JTextField;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTextPane;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.CardLayout;
import java.awt.SystemColor;

import javax.swing.JPasswordField;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * Class that mostly handles gui. It also defines function of handling item, stats interfaces and few others.
 */
public class MainFrame extends JFrame {
	
	private JLayeredPane contentPane;			/**Map size*/
	int map_size;								/**Player Save*/
	Player player;								/**Encounter data object*/
	Encounter battle_field;						/**Labyrinth/game filed data object*/
	Labirynth game_field;						/**Data: If true, items interface is active   */
	boolean bag;								/**Data: If true, stats interface is active */
	boolean stats;								/**Data: If true, combat interface is active */
	boolean combat;								/**Data: If true, player lost "The Game"*/
	boolean gameover;							/**Data: If true, player's inventory is full */
	boolean toomanyitems;						/**Data: Temporary item object */
	Items item_temp;							/**Data: Database Object  */
	Database data;
	int dialogue;// 0 - do you wish to remove item, 1 - are you sure you want to delete this item?
	private JTextField txtPlayer;
	/**
	 * Gui startup function
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/** 
	 * Just action listener for move buttons
	 */
	int move_button_listener(JTextPane text, List <JButton> list,int n)
	{
		text.setText(player.convert_move_maze(n));
		int x,y;
		x = player.loc_x;
		y = player.loc_y;
		System.out.print(x);
		System.out.print(" ");
		System.out.println(y);
		boolean auto = false;

		int len = game_field.maze[x][y].event.size();
		for(int i = 0; i < len;i++)
		{
			if(game_field.maze[x][y].event.equals(data.event_list.get(0)))
			{
				auto = true;
				list.get(4).setVisible(true);
				text.setText(data.event_list.get(1).description);
			}
			else
			list.get(4).setVisible(false);

		}
		if(game_field.maze[x][y].paths[player.convert_button(0)])  list.get(0).setEnabled(true); else  list.get(0).setEnabled(false); //forward
		if(game_field.maze[x][y].paths[player.convert_button(2)])  list.get(1).setEnabled(true); else list.get(1).setEnabled(false); //backward
		if(game_field.maze[x][y].paths[player.convert_button(3)])  list.get(2).setEnabled(true); else list.get(2).setEnabled(false); //left
		if(game_field.maze[x][y].paths[player.convert_button(1)])  list.get(3).setEnabled(true); else list.get(3).setEnabled(false); //right
		if(auto == false)
		{
			Random randomGenerator = new Random();
			int random = randomGenerator.nextInt(10);
			if(random == 0)
			{
				combat = true;
				if(player.level < 3){random = randomGenerator.nextInt(1);}
				else if(player.level < 6){random = randomGenerator.nextInt(3);}
				else if(player.level < 9){random = randomGenerator.nextInt(7);}
				else if(player.level < 12){random = randomGenerator.nextInt(7)+1;}
				else {random = randomGenerator.nextInt(6)+2;}
				battle_field = new Encounter( data.enemy_list.get(random),player.level,2,"You got attacked by enemy" + data.enemy_list.get(random).Name+"!",null,data);
				return 1;
			}
		}
		return 0;
	}
	/** 
	 * Just action listener for item buttons
	 */
	void item_button_listener(int n, JTextPane text, JButton drop, List <JButton> list,List <JButton> list2)
	{
		 if(toomanyitems == true)
		 {

			 game_field.maze[player.loc_x][player.loc_y].event.add(new Loot_Item(player.bag.get(n),false));
			 text.setText("You droped "+player.bag.get(n).name);
			 if(item_temp != null)
			 {
				 player.bag.add(n,item_temp);
				 item_temp = null;
			 }
			 else player.bag.remove(n);
			 item_buttons_turn(list);
			drop.setVisible(true);
			 toomanyitems = false;
		 }
		 else
		 {
			 if(player.bag.get(n).getClass() == Armor.class)
			 {
				 item_temp = player.armor;
				 player.armor = (Armor) player.bag.get(n);
				 player.bag.remove(n);
				 if(item_temp != null) player.bag.add(n, item_temp);
				 item_temp = null;
				 item_buttons_turn(list);
				 ability_buttons_turn(list2);
			 }
			 if(player.bag.get(n).getClass() == Weapon.class)
			 {
				 item_temp = player.weapon1;
				 player.weapon1 = (Weapon) player.bag.get(n);

				 player.bag.remove(n);
				 if(item_temp.name.equals("Fists")); else player.bag.add(n, item_temp);
				 item_temp = null;
				 item_buttons_turn(list);
				 ability_buttons_turn(list2);
			 }
			 if(player.bag.get(n).getClass() == Shield.class)
			 {

				 if(player.weapon1.subclass == 5)
				 {
					 item_temp = player.shield;
					 player.shield = (Shield) player.bag.get(n);
					 player.bag.remove(n);
					 if(item_temp != null)player.bag.add(n, item_temp);
					 item_temp = null;
					 item_buttons_turn(list);
					 ability_buttons_turn(list2);
				 }
				 else text.setText("You can only equip shield with one hand weapon");

			 }
			 if(player.bag.get(n)!=null)text.setText(player.bag.get(n).description);
		 }
		 
	}
	/** 
	 * Switches interface to inventory mode if player holds too many items
	 */
	public void cant_hold(List<JButton> list ,JButton drop, JPanel panel_adventure, JPanel ip, JPanel sp, JPanel abp)
	{
		toomanyitems = true;
		stats = false;
		bag = true;
		panel_adventure.setVisible(false);
		ip.setVisible(true);
		sp.setVisible(false);
		abp.setVisible(false);;
		item_buttons_turn(list);
		drop.setVisible(true);
	}
	/**
	 * Enables / disables every ability button
	 *  @param list Button list
	 *  @param t true/false
	 */
	public void ability_buttons_enable(List <JButton> list, boolean t)
	{
		for(int i = 0; i != 10; i++)
		{
			list.get(i).setEnabled(t);
		}
	}
	/**
	 * Function checks how many items player has, and based on that
	 * ets item buttons to visible/invisble. 
	 * @param list Button list
	 */
	public void item_buttons_turn(List <JButton> list)
	{
		for(int i = 0; i < 8; i++)
		{
			if(i < player.bag.size())
			{
				list.get(i).setVisible(true);
				list.get(i).setText(player.bag.get(i).name);
			}
			else
			{
				list.get(i).setVisible(false);
			}
		}
	}
	/**
	 * Turns ability buttons visible/invisible, and modifies their name based on player ability array.
	 * @param list button List
	 */
	public void ability_buttons_turn(List <JButton> list)
	{
		if(player.shield != null )
		{
			for(int i = 0; i < 8; i++)
			{
				if(i < data.ability_to_weapon.get(i).size())
				{
					if(player.weapon1.subclass == 5)
					{
						if(data.ability_to_weapon.get(2).get(i).first <= player.level)
							player.skills[i] = data.ability_to_weapon.get(2).get(i).second;
						else player.skills[i] = null;
					}
					else if(player.weapon1.subclass == 6)
					{
						if(data.ability_to_weapon.get(7).get(i).first <= player.level)
							player.skills[i] = data.ability_to_weapon.get(7).get(i).second;
						else player.skills[i] = null;
					}
						
				}
				else player.skills[i] = null;
				if(player.skills[i] != null) 
				{
					list.get(i).setText(player.skills[i].name);
					list.get(i).setVisible(true);
				}
				else list.get(i).setVisible(false);
			}
		}
		else
		for(int i = 0; i<data.ability_to_weapon.get(player.weapon1.subclass).size(); i++)
		{
			int sub = player.weapon1.subclass;
			if(data.ability_to_weapon.get(sub).get(i).first <= player.level)
				player.skills[i] = data.ability_to_weapon.get(sub).get(i).second;
			else player.skills[i] = null;
			if(player.skills[i] != null) 
			{
				list.get(i).setText(player.skills[i].name);
				list.get(i).setVisible(true);
			}
			else list.get(i).setVisible(false);
		}
	}
	/**
	 * Turns ability buttons enabled/disabled, based on their cooldown timers, or special conditions.
	 * @param list
	 */
	public void ability_buttons_toggle(List <JButton> list)
	{
		for(int i = 0; i != 8; i++)
		{
			if(player.skills[i] != null)
			{
				if(player.skills[i].name.equals("Execute") == true) 
					if(battle_field != null)
					{
						if(battle_field.enemy.hitpoints < (battle_field.enemy.stamina*20)*20/100)
						{
							list.get(0).setEnabled(true);
							continue;
						}
						else
						{
							list.get(0).setEnabled(false);
							continue;
						}
					}
				if(player.skills[i].name.equals("Riposte") == true) 
						if(battle_field.player_parried) 
							{
								list.get(0).setEnabled(true);
								continue;
							}
						else
						{
							list.get(0).setEnabled(false);
							continue;
						}
				if(player.skills[i].name.equals("Impale") == true) 
					if(battle_field.player_parried) 
						{
							list.get(0).setEnabled(true);
							continue;
						}
					else
					{
						list.get(0).setEnabled(false);
						continue;
					}
				if(player.skills[i].cooldown_timer == 0) list.get(i).setEnabled(true);
				else list.get(i).setEnabled(false);
			}}
	}
	/**
	 * Class constructor. Defines all gui elements, and implements all action listeners.
	 */
	public MainFrame() {
		data = new Database();
		combat = false;
		toomanyitems = false;
		stats = false;
		bag = false;
		map_size = 2;
		this.setTitle("The Game v0.01");
		game_field = new Labirynth(10,data);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 656, 495);
		contentPane = new JLayeredPane();
		contentPane.setVisible(true);
		contentPane.setOpaque(true);
		
		
		
		
		contentPane.setBackground(SystemColor.info);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		Panel new_game_panel = new Panel();
		new_game_panel.setBounds(161, 324, 329, 79);
		contentPane.add(new_game_panel);
		
		Panel panel_dialogue = new Panel();
		contentPane.setLayer(panel_dialogue, 0);
		panel_dialogue.setBounds(110, 325, 444, 90);
		contentPane.add(panel_dialogue);
		panel_dialogue.setBackground(SystemColor.info);
		panel_dialogue.setVisible(false);
		panel_dialogue.setLayout(null);
		
		txtPlayer = new JTextField();
		txtPlayer.setText("Player");
		txtPlayer.setBounds(10, 32, 86, 20);
		new_game_panel.add(txtPlayer);
		txtPlayer.setColumns(10);
		
		JLabel lblPlayerName = new JLabel("Player Name");
		lblPlayerName.setBounds(10, 9, 76, 14);
		new_game_panel.add(lblPlayerName);
		
		JPanel simple_status_panel = new JPanel();
		simple_status_panel.setBackground(SystemColor.info);
		simple_status_panel.setBounds(10, 45, 129, 86);
		contentPane.add(simple_status_panel);
		simple_status_panel.setLayout(null);
		
		JLabel simple_status_label = new JLabel("Player");
		simple_status_label.setFont(new Font("Arial Black", Font.PLAIN, 13));
		simple_status_label.setBounds(10, 11, 83, 26);
		simple_status_panel.add(simple_status_label);
		simple_status_panel.setVisible(false);
		
		JLabel lblGold = new JLabel("Gold: ");
		lblGold.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblGold.setBounds(10, 36, 46, 14);
		simple_status_panel.add(lblGold);
		
		JLabel Simple_Status_Gold = new JLabel("0");
		Simple_Status_Gold.setBounds(39, 36, 46, 14);
		simple_status_panel.add(Simple_Status_Gold);
		
		JLabel lblHitpoints = new JLabel("Hitpoints: ");
		lblHitpoints.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblHitpoints.setBounds(10, 48, 64, 14);
		simple_status_panel.add(lblHitpoints);
		
		JLabel Simple_Status_HP = new JLabel("0/0");
		Simple_Status_HP.setBounds(67, 48, 62, 14);
		simple_status_panel.add(Simple_Status_HP);
		
		JLabel Fatigue_Label = new JLabel("Energy: ");
		Fatigue_Label.setFont(new Font("Times New Roman", Font.BOLD, 12));
		Fatigue_Label.setBounds(10, 61, 64, 14);
		simple_status_panel.add(Fatigue_Label);
		
		JLabel Simple_Status_Fatigue = new JLabel("100/100");
		Simple_Status_Fatigue.setBounds(67, 61, 62, 14);
		simple_status_panel.add(Simple_Status_Fatigue);
		
		JPanel items_panel = new JPanel();
		contentPane.setLayer(items_panel, 7);
		items_panel.setBackground(SystemColor.info);
		items_panel.setForeground(SystemColor.info);
		items_panel.setBounds(110, 325, 444, 90);
		items_panel.setVisible(false);
		contentPane.add(items_panel);
		items_panel.setLayout(null);
		
		JButton btnDrop = new JButton("Drop");
		btnDrop.setBounds(321, 67, 89, 23);
		items_panel.add(btnDrop);
		
		List <JButton> item_button_list = new ArrayList<JButton>();
		for(int i=0;i<8;i++)
			{
				item_button_list.add(new JButton("New button"));
				items_panel.add(item_button_list.get(i));
				item_button_list.get(i).setVisible(false);
			}
		item_button_list.get(0).setBounds(36, 5, 89, 23);
		item_button_list.get(1).setBounds(130, 5, 89, 23);
		item_button_list.get(2).setBounds(224, 5, 89, 23);
		item_button_list.get(3).setBounds(318, 5, 89, 23);
		item_button_list.get(4).setBounds(36, 33, 89, 23);
		item_button_list.get(5).setBounds(130, 33, 89, 23);
		item_button_list.get(6).setBounds(224, 33, 89, 23);
		item_button_list.get(7).setBounds(318, 33, 89, 23);

		
		
		
		
		

		 
		
		JPanel panel_adventure = new JPanel();
		contentPane.setLayer(panel_adventure, 1);
		panel_adventure.setBounds(110, 325, 444, 90);
		contentPane.add(panel_adventure);
		panel_adventure.setBackground(SystemColor.info);
		panel_adventure.setVisible(false);
		panel_adventure.setLayout(null);

		List <JButton> move_button_list = new ArrayList<JButton>();
		move_button_list.add(new JButton("Forward"));
		move_button_list.add(new JButton("Behind"));
		move_button_list.add(new JButton("Left"));
		move_button_list.add(new JButton("Right"));
		move_button_list.add(new JButton("Exit"));
		
		move_button_list.get(0).setBounds(10, 11, 67, 23);
		panel_adventure.add(move_button_list.get(0));
		
		move_button_list.get(1).setBounds(85, 11, 67, 23);
		panel_adventure.add(move_button_list.get(1));
		
		move_button_list.get(2).setBounds(10, 45, 67, 23);
		panel_adventure.add(move_button_list.get(2));
		
		move_button_list.get(3).setBounds(85, 45, 67, 23);
		panel_adventure.add(move_button_list.get(3));
		
		move_button_list.get(4).setBounds(160, 11, 67, 23);
		panel_adventure.add(move_button_list.get(4));
		move_button_list.get(4).setVisible(false);
	
		JButton btnInterct = new JButton("Rest");
		btnInterct.setBounds(311, 11, 111, 23);
		panel_adventure.add(btnInterct);

		
		JButton btnInterct_1 = new JButton("Look Around");
		btnInterct_1.setBounds(311, 45, 111, 23);
		panel_adventure.add(btnInterct_1);
		
		
		JButton btnYes = new JButton("Yes");
		btnYes.setBounds(102, 11, 103, 37);
		panel_dialogue.add(btnYes);
		
		JButton btnNo = new JButton("No");
		btnNo.setBounds(215, 11, 103, 37);
		panel_dialogue.add(btnNo);
		btnYes.setVisible(true);
		
		JPanel enemy_status_panel = new JPanel();
		contentPane.setLayer(enemy_status_panel, 0);
		enemy_status_panel.setBackground(SystemColor.info);
		enemy_status_panel.setBounds(161, 134, 334, 67);
		contentPane.add(enemy_status_panel);
		enemy_status_panel.setLayout(null);
		enemy_status_panel.setVisible(false);
		
		JLabel enemy_status_name = new JLabel("Player");
		enemy_status_name.setBounds(144, 5, 45, 19);
		enemy_status_name.setFont(new Font("Arial Black", Font.PLAIN, 13));
		enemy_status_panel.add(enemy_status_name);
		
		JLabel label = new JLabel("Hitpoints: ");
		label.setFont(new Font("Times New Roman", Font.BOLD, 12));
		label.setBounds(80, 30, 64, 14);
		enemy_status_panel.add(label);
		
		JLabel label_1 = new JLabel("Fatigue: ");
		label_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		label_1.setBounds(80, 42, 64, 14);
		enemy_status_panel.add(label_1);
		
		JLabel enemy_status_HP = new JLabel("0/0");
		enemy_status_HP.setBounds(143, 30, 70, 14);
		enemy_status_panel.add(enemy_status_HP);
		
		JLabel enemy_status_fatigue = new JLabel("100/100");
		enemy_status_fatigue.setBounds(143, 42, 70, 14);
		enemy_status_panel.add(enemy_status_fatigue);
		
		JButton btnNew_Game = new JButton("New Game");

		btnNew_Game.setBounds(161, 409, 103, 37);
		contentPane.add(btnNew_Game);
		
		JPanel abilities_panel = new JPanel();
		abilities_panel.setBounds(26, 325, 578, 90);
		contentPane.add(abilities_panel);
		contentPane.setLayer(abilities_panel, 0);
		abilities_panel.setBackground(SystemColor.info);
		abilities_panel.setForeground(SystemColor.info);
		abilities_panel.setLayout(null);
		abilities_panel.setVisible(false);
		
		JTextPane txtpnObjectiveOfThe = new JTextPane();
		txtpnObjectiveOfThe.setForeground(Color.DARK_GRAY);
		txtpnObjectiveOfThe.setEditable(false);
		txtpnObjectiveOfThe.setText("Objective of The Game, is to collect maximum amount of gold inside maze, and than get back to the begining. Inside of dungeon there will be treasure rewarding player with lots of gold. Player can as well get gold from beating enemies. Beware, more time you spend in dungeon, stronger enemies will be. Dungeon size determines how big treasure is.");
		txtpnObjectiveOfThe.setBounds(110, 200, 444, 106);
		contentPane.add(txtpnObjectiveOfThe);
		
		JPanel options_panel = new JPanel();
		options_panel.setBackground(SystemColor.info);
		options_panel.setBounds(517, 45, 113, 86);
		contentPane.add(options_panel);
		options_panel.setLayout(null);
		options_panel.setVisible(false);
		JButton btnStats = new JButton("Stats");
		btnStats.setBounds(10, 0, 91, 35);
		options_panel.add(btnStats);
		
		
		
		JButton btnBag = new JButton("Bag");
		btnBag.setBounds(10, 46, 91, 35);
		options_panel.add(btnBag);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(274, 409, 103, 37);
		contentPane.add(btnLoad);
		btnLoad.setEnabled(false);
		

		JPanel Stats_Panel = new JPanel();
		contentPane.setLayer(Stats_Panel, 1);
		Stats_Panel.setBackground(SystemColor.info);
		Stats_Panel.setBounds(161, 45, 329, 116);
		contentPane.add(Stats_Panel);
		Stats_Panel.setLayout(null);
		
		JLabel lblStrenght = new JLabel("Strenght:");
		lblStrenght.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblStrenght.setBounds(13, 11, 64, 14);
		Stats_Panel.add(lblStrenght);
		
		JLabel lblEndurance = new JLabel("Endurance:");
		lblEndurance.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblEndurance.setBounds(13, 39, 64, 14);
		Stats_Panel.add(lblEndurance);
		
		JLabel lblAgility = new JLabel("Agility:");
		lblAgility.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblAgility.setBounds(13, 25, 64, 14);
		Stats_Panel.add(lblAgility);
		
		JLabel lblTotalSteps = new JLabel("Total Steps:");
		lblTotalSteps.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTotalSteps.setBounds(10, 71, 64, 14);
		Stats_Panel.add(lblTotalSteps);
		
		JLabel lblEnemiesKilled = new JLabel("Enemies Defeated:");
		lblEnemiesKilled.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblEnemiesKilled.setBounds(10, 84, 111, 14);
		Stats_Panel.add(lblEnemiesKilled);
		
		JLabel lblWeaponDamage = new JLabel("Weapon Damage:");
		lblWeaponDamage.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblWeaponDamage.setBounds(164, 11, 102, 14);
		Stats_Panel.add(lblWeaponDamage);
		
		JLabel lblArmorPenetration = new JLabel("Armor Penetration:");
		lblArmorPenetration.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblArmorPenetration.setBounds(164, 25, 111, 14);
		Stats_Panel.add(lblArmorPenetration);
		
		JLabel lblArmor = new JLabel("Armor:");
		lblArmor.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblArmor.setBounds(164, 39, 102, 14);
		Stats_Panel.add(lblArmor);
		
		JLabel full_status_str = new JLabel("0");
		full_status_str.setBounds(75, 11, 46, 14);
		Stats_Panel.add(full_status_str);
		
		JLabel full_status_agility = new JLabel("0");
		full_status_agility.setBounds(75, 25, 46, 14);
		Stats_Panel.add(full_status_agility);
		
		JLabel full_status_end = new JLabel("0");
		full_status_end.setBounds(75, 39, 46, 14);
		Stats_Panel.add(full_status_end);
		
		JLabel full_status_dmg = new JLabel("0");
		full_status_dmg.setBounds(276, 11, 46, 14);
		Stats_Panel.add(full_status_dmg);
		
		JLabel full_status_arp = new JLabel("0");
		full_status_arp.setBounds(276, 25, 46, 14);
		Stats_Panel.add(full_status_arp);
		
		JLabel full_status_armor = new JLabel("0");
		full_status_armor.setBounds(276, 39, 46, 14);
		Stats_Panel.add(full_status_armor);
		
		JLabel full_status_defeated = new JLabel("0");
		full_status_defeated.setBounds(114, 84, 46, 14);
		Stats_Panel.add(full_status_defeated);
		
		JLabel full_status_steps = new JLabel("0");
		full_status_steps.setBounds(114, 71, 46, 14);
		Stats_Panel.add(full_status_steps);
		
		JLabel lbllevel = new JLabel("Level:");
		lbllevel.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lbllevel.setBounds(164, 71, 64, 14);
		Stats_Panel.add(lbllevel);
		
		JLabel full_status_exp = new JLabel("0");
		full_status_exp.setBounds(276, 84, 46, 14);
		Stats_Panel.add(full_status_exp);
		
		JLabel lblexp = new JLabel("Experience:");
		lblexp.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblexp.setBounds(164, 84, 64, 14);
		Stats_Panel.add(lblexp);
		
		JLabel full_status_lv = new JLabel("1");
		full_status_lv.setBounds(276, 71, 46, 14);
		Stats_Panel.add(full_status_lv);
		
		JButton btn_plus_str = new JButton("+");
		btn_plus_str.setBounds(0, 10, 13, 14);
		Stats_Panel.add(btn_plus_str);
		 btn_plus_str.setMargin(new Insets(0, 0, 0, 0));
		 btn_plus_str.setVisible(false);
		 
		 JButton btn_plus_agility = new JButton("+");
		 btn_plus_agility.setMargin(new Insets(0, 0, 0, 0));
		 btn_plus_agility.setBounds(0, 24, 13, 14);
		 Stats_Panel.add(btn_plus_agility);
		 btn_plus_agility.setVisible(false);
		 
		 JButton btn_plus_end = new JButton("+");
		 btn_plus_end.setMargin(new Insets(0, 0, 0, 0));
		 btn_plus_end.setBounds(0, 38, 13, 14);
		 Stats_Panel.add(btn_plus_end);
		 btn_plus_end.setVisible(false);
		 
		
		List <JButton> ability_button_list = new ArrayList<JButton>();
		for(int i=0;i<10;i++)
			{
			ability_button_list.add(new JButton("New button"));
			abilities_panel.add(ability_button_list.get(i));
			ability_button_list.get(i).setVisible(false);
			}
		ability_button_list.get(0).setBounds(35, 11, 97, 23);
		ability_button_list.get(1).setBounds(35, 45, 97, 23);
		ability_button_list.get(2).setBounds(142, 11, 97, 23);
		ability_button_list.get(3).setBounds(142, 45, 97, 23);
		ability_button_list.get(4).setBounds(249, 11, 97, 23);
		ability_button_list.get(5).setBounds(249, 45, 97, 23);
		ability_button_list.get(6).setBounds(356, 11, 97, 23);
		ability_button_list.get(7).setBounds(356, 45, 97, 23);
		ability_button_list.get(8).setBounds(477, 11, 87, 23);
		ability_button_list.get(9).setBounds(477, 45, 87, 23);
		ability_button_list.get(8).setVisible(true);
		ability_button_list.get(9).setVisible(true);
		ability_button_list.get(8).setText("Wait");
		ability_button_list.get(9).setText("Run Away");
		
		ability_button_list.get(0).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					String text;
					switch(battle_field.run(0))
					{
					case 1:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								btn_plus_str.setVisible(true);
								btn_plus_agility.setVisible(true);
								btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space? Or just drop found item?";
								cant_hold(item_button_list,btnDrop, panel_adventure,  items_panel, Stats_Panel, abilities_panel);
								item_temp = battle_field.item;
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[0].description);
				}
				
			}
		});

		ability_button_list.get(1).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(1))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[1].description);
				}
				
			}
		});

		ability_button_list.get(2).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(2))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}ability_buttons_turn(ability_button_list);
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[2].description);
				}
			}
		});
		
		ability_button_list.get(3).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(3))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[3].description);
				}
			}
		});

		ability_button_list.get(4).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(4))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[4].description);
				}
			}
		});

		ability_button_list.get(5).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(5))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[5].description);
				}
			}
		});
		
		ability_button_list.get(6).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(6))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[6].description);
				}
			}
		});

		ability_button_list.get(7).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(7))
					{
					case 1:	
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								if(player.strenght != 0) btn_plus_str.setVisible(true);
								if(player.agility != 0) btn_plus_agility.setVisible(true);
								if(player.stamina != 0) btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						ability_buttons_enable(ability_button_list, false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[7].description);
				}
			}
		});

		ability_button_list.get(8).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(8))
					{
					case 1:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								btn_plus_str.setVisible(true);
								btn_plus_agility.setVisible(true);
								btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space? Or just drop found item?";
								cant_hold(item_button_list,btnDrop, panel_adventure,  items_panel, Stats_Panel, abilities_panel);
								item_temp = battle_field.item;
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[8].description);
				}
				
			}
		});
		ability_button_list.get(9).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(combat == true)
				{
					switch(battle_field.run(9))
					{
					case 1:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						String text;
						text = "You have beaten an enemy. You gained " + battle_field.enemy.exp + "experience points and " + battle_field.enemy.gold + "gold." ;
						player.experience = (int) (player.experience + battle_field.enemy.exp);
						player.gold = (int) (player.gold + battle_field.enemy.gold);
						if(player.experience+1 >= 50 * Math.pow(2, player.level-1)) 
						{
							if(player.level == 20)
							{
								player.experience = 0;
							}
							else
							{
								text = text + "You leveled up! You have 8 skill points to spend!";
								player.experience =(int)(player.experience - (50*Math.pow(2, player.level-1)));
								player.level++;
								ability_buttons_turn(ability_button_list);
								player.level_points += 8;
								btnStats.setText("Stats"+"("+player.level_points+")");
								btn_plus_str.setVisible(true);
								btn_plus_agility.setVisible(true);
								btn_plus_end.setVisible(true);
								btnStats.setName("Stats 8!");
							}
							
						}
						if(battle_field.item != null)
						{
							text = text + "You also found " + battle_field.item.name;
							if(player.bag.size()>8)
							{
								text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space? Or just drop found item?";
								cant_hold(item_button_list,btnDrop, panel_adventure,  items_panel, Stats_Panel, abilities_panel);
								item_temp = battle_field.item;
								
							}
							else
							{
								player.bag.add(battle_field.item);
							}
						}
						txtpnObjectiveOfThe.setText(text);
						break;
					case -1:
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						gameover = true;
						btnYes.setText("Next");
						btnNo.setVisible(false);
						break;
					case 2:
						ability_buttons_enable(ability_button_list, true);
						abilities_panel.setVisible(false);
						panel_dialogue.setVisible(true);
						btnYes.setText("Next");
						btnNo.setVisible(false);
						text = "You succesfully run away from enemy!";
						txtpnObjectiveOfThe.setText(text);
						break;
					default:
						ability_buttons_toggle(ability_button_list);
					}
				}
				else
				{
					txtpnObjectiveOfThe.setText(player.skills[9].description);
				}
				
			}
		});
		
		JButton btnHighscore = new JButton("HighScore");
		btnHighscore.setEnabled(false);
		btnHighscore.setBounds(387, 409, 103, 37);
		contentPane.add(btnHighscore);
		
		ButtonGroup set_size_group = new ButtonGroup();
		
		
		

		
		//look around
		btnInterct_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = "";
				//text = text; 
				if(game_field.maze[player.loc_x][player.loc_y].event.isEmpty() == false)
				for(int i = 0; i < game_field.maze[player.loc_x][player.loc_y].event.size(); i++)
				{
					if(game_field.maze[player.loc_x][player.loc_y].event.get(i).getClass() == Loot_Item.class)
					{
						Loot_Item temp_loot = (Loot_Item) game_field.maze[player.loc_x][player.loc_y].event.get(i);
						text = text + "You found " + temp_loot.item.name;
						if(player.bag.size() == 8)
						{
							text = text + "unfortunately, you don't have enough space for it in bag. Do you wish to remove any item to make space?";
							cant_hold(item_button_list,btnDrop, panel_adventure,  items_panel, Stats_Panel, abilities_panel);
							item_temp = temp_loot.item;
						}
						else
						{
							player.bag.add(temp_loot.item);
							
						}
						game_field.maze[player.loc_x][player.loc_y].event.remove(i);
						break;
					}
				}
				else
				{
					text = text + "You don't find anything intresting in this room. ";
					if(game_field.maze[player.loc_x][player.loc_y].visited) text = text + "You have also feeling you have been here before";
				}

				txtpnObjectiveOfThe.setText(text);
			}
		});
		

		
		JRadioButton radioButton_size1 = new JRadioButton("10x10");
		radioButton_size1.setBackground(SystemColor.info);
		radioButton_size1.setBounds(139, 5, 67, 23);
		radioButton_size1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map_size = 1;
			}
		});
		new_game_panel.setLayout(null);
		new_game_panel.add(radioButton_size1);
		
		JRadioButton radioButton_size2 = new JRadioButton("25x25");
		radioButton_size2.setBackground(SystemColor.info);
		radioButton_size2.setBounds(139, 31, 67, 23);
		radioButton_size2.setSelected(true);
		radioButton_size2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map_size = 2;
			}
		});
		new_game_panel.add(radioButton_size2);
		
		JRadioButton radioButton_size3 = new JRadioButton("50x50");
		radioButton_size3.setBackground(SystemColor.info);
		radioButton_size3.setBounds(223, 5, 67, 23);
		radioButton_size3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map_size = 3;
			}
		});
		new_game_panel.add(radioButton_size3);
		
		
		JRadioButton radioButton_size4 = new JRadioButton("100x100");
		radioButton_size4.setBackground(SystemColor.info);
		radioButton_size4.setBounds(223, 31, 67, 23);
		radioButton_size4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map_size = 4;
			}
		});
		new_game_panel.add(radioButton_size4);
		set_size_group.add(radioButton_size1);
		set_size_group.add(radioButton_size2);
		set_size_group.add(radioButton_size3);
		set_size_group.add(radioButton_size4);
		
		
		 btn_plus_str.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent e) {
			 		player.strenght = player.strenght + 1;
			 		player.level_points--;
			 		full_status_str.setText(Double.toString(player.strenght));
			 		if(player.strenght == 100)btn_plus_str.setVisible(false);
			 		if(player.level_points == 0)
			 		{
			 			btnStats.setText("Stats");
						btn_plus_str.setVisible(false);
						btn_plus_agility.setVisible(false);
						btn_plus_end.setVisible(false);
			 		}
			 		else
			 		{
			 			btnStats.setText("Stats"+"("+player.level_points+")");
			 		}
			 	}
			 });
		 btn_plus_agility.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent arg0) {
			 		player.agility = player.agility + 1;
			 		player.level_points--;
			 		full_status_agility.setText(Double.toString(player.agility));
			 		if(player.strenght == 100)btn_plus_str.setVisible(false);
			 		if(player.level_points == 0)
			 		{
			 			btnStats.setText("Stats");
						btn_plus_str.setVisible(false);
						btn_plus_agility.setVisible(false);
						btn_plus_end.setVisible(false);
			 		}
			 		else
			 		{
			 			btnStats.setText("Stats"+"("+player.level_points+")");
			 		}
			 	}
		 	});
		 btn_plus_end.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent e) {
			 		player.stamina = player.stamina + 1;
			 		player.level_points--;
			 		full_status_end.setText(Double.toString(player.stamina));
			 		if(player.stamina == 100)btn_plus_end.setVisible(false);
			 		if(player.level_points == 0)
			 		{
			 			btnStats.setText("Stats");
						btn_plus_str.setVisible(false);
						btn_plus_agility.setVisible(false);
						btn_plus_end.setVisible(false);
			 		}
			 		else
			 		{

						btnStats.setText("Stats"+"("+player.level_points+")");
			 		}
			 	}
			 });
			btnDrop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(toomanyitems == true)
					{
						toomanyitems = false;
						item_temp = null;
					}
					else
					{
						txtpnObjectiveOfThe.setText("What item you want to drop?");
						toomanyitems = true;
						btnDrop.setVisible(false);
					}
				}
			});
		 item_button_list.get(0).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(0, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(1).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(1, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(2).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(2, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(3).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(3, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(4).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(4, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(5).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(5, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(6).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(6, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 item_button_list.get(7).addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 item_button_listener(7, txtpnObjectiveOfThe, btnDrop,item_button_list,ability_button_list);
			 }});
		 
		Stats_Panel.setVisible(false);
		
		btnInterct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtpnObjectiveOfThe.setText(" You rested for couple of hours and regained your strenght");
				player.hitpoints = player.stamina * 20;
				player.fatigue = 100;
				Simple_Status_HP.setText(player.hitpoints+"/"+player.hitpoints);
				Simple_Status_Fatigue.setText("100/100");
				
				
			}
		});
		
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(combat == true)
				{
					combat = false;
					if(gameover == true)
					{

						panel_dialogue.setVisible(false);
						btnStats.setEnabled(true);
						btnBag.setEnabled(true);
						btnNew_Game.setVisible(true);
						btnLoad.setVisible(true);
						btnHighscore.setVisible(true);
						new_game_panel.setVisible(true);
						
						options_panel.setVisible(false);
						txtpnObjectiveOfThe.setText("Objective of The Game, is to collect maximum amount of gold inside maze, and than get back to the begining. Inside of dungeon there will be treasure rewarding player with lots of gold. Player can as well get gold from beating enemies. Beware, more time you spend in dungeon, stronger enemies will be. Dungeon size determines how big treasure is.");
						
						//simple_status_panel.setVisible(false);
					}
					else
					{
						player.killed++;
						panel_adventure.setVisible(true);
						btnStats.setEnabled(true);
						btnBag.setEnabled(true);
						panel_dialogue.setVisible(false);
						enemy_status_panel.setVisible(false);
						
						
					}
				}
				
			}
		});
		
		btnBag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(bag == false || stats == true)
				{
					if(item_temp == null)
					{
						toomanyitems = false;
						btnDrop.setVisible(true);
					}
					stats = false;
					bag = true;
					panel_adventure.setVisible(false);
					items_panel.setVisible(true);
					Stats_Panel.setVisible(false);
					abilities_panel.setVisible(false);
					item_buttons_turn(item_button_list);
				}
				else
				{
					bag = false;
					panel_adventure.setVisible(true);
					items_panel.setVisible(false);
				}
				
			}
		});
		
	
		btnStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(stats == false || bag == true)
				{
					
					stats = true;
					bag = false;
					panel_adventure.setVisible(false);
					items_panel.setVisible(false);
					Stats_Panel.setVisible(true);
					abilities_panel.setVisible(true);
					
					full_status_str.setText(Double.toString(player.strenght));
					full_status_agility.setText(Double.toString(player.agility));
					full_status_end.setText(Double.toString(player.stamina));
					full_status_dmg.setText(Double.toString(player.weapon1.weapon_damage));
					if(player.shield != null)full_status_armor.setText(Double.toString(player.armor.armor+player.shield.armor));
					else full_status_armor.setText(Double.toString(player.armor.armor));
					full_status_defeated.setText(Integer.toString(player.killed));
					full_status_steps.setText(Integer.toString(player.steps));
					full_status_lv.setText(Integer.toString(player.level));

					full_status_exp.setText(Integer.toString(player.experience)+"/"+Double.toString(50*Math.pow(2.0,player.level-1)));
					
					
					
				}
				else
				{
					stats = false;
					panel_adventure.setVisible(true);
					Stats_Panel.setVisible(false);
					abilities_panel.setVisible(false);
				}
				
			}
		});
		btnNew_Game.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(map_size)
				{
					case 1: game_field = new Labirynth(10,data); break;
					case 2: game_field = new Labirynth(25,data); break; 
					case 3: game_field = new Labirynth(50,data); break;
					case 4: game_field = new Labirynth(100,data);  break;
				}
				gameover = false;
				btnNew_Game.setVisible(false);
				btnLoad.setVisible(false);
				btnHighscore.setVisible(false);
				new_game_panel.setVisible(false);
				
				options_panel.setVisible(true);
				player = new Player(txtPlayer.getText(),game_field,data);
				simple_status_panel.setVisible(true);
				simple_status_label.setText(player.name);
				Simple_Status_HP.setText(Double.toString(player.hitpoints)+"/"+Double.toString(player.stamina*20));
				int x = player.loc_x;
				int y = player.loc_y;
				//txtpnObjectiveOfThe.setText("You woke up in middle of cold dark room. Your head hurts, and you don't remember how you got here. Maybe one beer too much?");
				if(game_field.maze[x][y].paths[player.convert_button(0)] == false )  move_button_list.get(0).setEnabled(false); // forward
				if(game_field.maze[x][y].paths[player.convert_button(2)] == false )  move_button_list.get(1).setEnabled(false); // backward
				if(game_field.maze[x][y].paths[player.convert_button(3)] == false )  move_button_list.get(2).setEnabled(false); // left
				if(game_field.maze[x][y].paths[player.convert_button(1)] == false )  move_button_list.get(3).setEnabled(false); // right

				combat = true;
				battle_field = new Encounter((Encounter)data.event_list.get(0));
				battle_field.set(player, txtpnObjectiveOfThe, Simple_Status_HP, Simple_Status_Fatigue, enemy_status_name, enemy_status_HP, enemy_status_fatigue);
				ability_buttons_turn(ability_button_list);
				
				panel_adventure.setVisible(false);
				abilities_panel.setVisible(true);
				enemy_status_panel.setVisible(true);
				btnStats.setEnabled(false);
				btnBag.setEnabled(false);
			}
		});

		//The game
		move_button_list.get(0).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(move_button_listener(txtpnObjectiveOfThe, move_button_list,0)==1)
				{
					battle_field.set(player, txtpnObjectiveOfThe, Simple_Status_HP, Simple_Status_Fatigue, enemy_status_name, enemy_status_HP, enemy_status_fatigue);
					ability_buttons_turn(ability_button_list);
					
					panel_adventure.setVisible(false);
					abilities_panel.setVisible(true);
					enemy_status_panel.setVisible(true);
					btnStats.setEnabled(false);
					btnBag.setEnabled(false);
				}
			}
		});
		move_button_list.get(1).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(move_button_listener(txtpnObjectiveOfThe, move_button_list,2)==1)
				{
					battle_field.set(player, txtpnObjectiveOfThe, Simple_Status_HP, Simple_Status_Fatigue, enemy_status_name, enemy_status_HP, enemy_status_fatigue);
					ability_buttons_turn(ability_button_list);
					
					panel_adventure.setVisible(false);
					abilities_panel.setVisible(true);
					enemy_status_panel.setVisible(true);
					btnStats.setEnabled(false);
					btnBag.setEnabled(false);
				}
			}
		});
		move_button_list.get(2).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(move_button_listener(txtpnObjectiveOfThe, move_button_list,3)==1)
				{
					battle_field.set(player, txtpnObjectiveOfThe, Simple_Status_HP, Simple_Status_Fatigue, enemy_status_name, enemy_status_HP, enemy_status_fatigue);
					ability_buttons_turn(ability_button_list);
					
					panel_adventure.setVisible(false);
					abilities_panel.setVisible(true);
					enemy_status_panel.setVisible(true);
					btnStats.setEnabled(false);
					btnBag.setEnabled(false);
				}
			}
		});
		move_button_list.get(3).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(move_button_listener(txtpnObjectiveOfThe, move_button_list,1)==1)
				{
					battle_field.set(player, txtpnObjectiveOfThe, Simple_Status_HP, Simple_Status_Fatigue, enemy_status_name, enemy_status_HP, enemy_status_fatigue);
					ability_buttons_turn(ability_button_list);
					
					panel_adventure.setVisible(false);
					abilities_panel.setVisible(true);
					enemy_status_panel.setVisible(true);
					btnStats.setEnabled(false);
					btnBag.setEnabled(false);
				}
			}
		});
		move_button_list.get(4).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtpnObjectiveOfThe.setText("GG you won!");
				
			}
		});
	}
}
