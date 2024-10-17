package com.xanduh.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.xanduh.entities.Player;
import com.xanduh.main.Game;

public class UI {
	
	public void render(Graphics g) {
		//g.setColor(Color.red);
		//g.fillRect(8, 4, 50, 8);
		//g.setColor(Color.green);
		//g.fillRect(8, 4,(int)((Player.life/Player.maxLife)*50),8);
		g.setColor(Color.white);
		g.setFont(new Font("comic sans", Font.BOLD, 10));
		g.drawString((int)Game.player.life + "/" + (int)Game.player.maxLife,8,16);
	}

}
