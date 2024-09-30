package com.xanduh.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xanduh.entities.*;
import com.xanduh.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
        try {
            File file = new File(path);
            System.out.println("Loading world from: " + file.getAbsolutePath());
            if (!file.exists()) {
                throw new IOException("File not found: " + file.getAbsolutePath());
            }
            
            BufferedImage map = ImageIO.read(file);
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
            	for (int yy = 0; yy < map.getHeight(); yy++) {
            		int pixelAtual = pixels[xx + (yy * map.getWidth())];
            		
            		tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
            		
            		switch (pixelAtual) {
            		case 0xFF000000:
            			// Floor
            			tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
            			break;
            		case 0xFFFFFFFF:
            			// Wall
            			tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
            			break;
            		case 0xFF0026FF:
            			// Player
            			Game.player.setX(xx*16);
            			Game.player.setY(yy*16);
            			break;
            		case 0xFF00FF21:
            			// Life Pack
            			Game.entities.add(new LifePack(xx*16, yy*16, 16, 16, Entity.LIFEPACK_EN));
            			break;
            		case 0xFFFFD800:
            			// Bullet
            			Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Entity.BULLET_EN));
            			break;
            		case 0xFFFF0000:
            			// Enemy
            			Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.ENEMY_EN));
            			break;
            		case 0xFF808080:
            			// Weapon
            			Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.WEAPON_EN));
            			break;
            		}
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void render(Graphics g) {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}