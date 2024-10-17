package com.xanduh.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xanduh.entities.*;
import com.xanduh.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
        try {
            File file = new File(path);
            //System.out.println("Loading world from: " + file.getAbsolutePath());
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
            		
            		tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
            		
            		switch (pixelAtual) {
            		case 0xFF000000:
            			// Floor
            			tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
            			break;
            		case 0xFFFFFFFF:
            			// Wall
            			tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
            			break;
            		case 0xFF0026FF:
            			// Player
            			Game.player.setX(xx * TILE_SIZE);
            			Game.player.setY(yy * TILE_SIZE);
            			break;
            		case 0xFF00FF21:
            			// Lifepack
            			LifePack pack = new LifePack(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.LIFEPACK_EN);
            			Game.entities.add(pack);
            			break;
            		case 0xFFFFD800:
            			// Bullet
            			Game.entities.add(new Bullet(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.BULLET_EN));
            			break;
            		case 0xFFFF0000:
            			// Enemy
            			Enemy en = new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENEMY_EN);
            			Game.entities.add(en);
            			Game.enemies.add(en);
            			break;
            		case 0xFF808080:
            			// Weapon
            			Game.entities.add(new Weapon(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.WEAPON_EN));
            			break;
            		}
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static boolean isFree(int xNext, int yNext) {
	    int x1 = xNext / TILE_SIZE;
	    int y1 = yNext / TILE_SIZE;
	    
	    int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
	    int y2 = yNext / TILE_SIZE;
	    
	    int x3 = xNext / TILE_SIZE;
	    int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
	    
	    int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
	    int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

	    if (x1 < 0 || x2 < 0 || x3 < 0 || x4 < 0 || 
	        y1 < 0 || y2 < 0 || y3 < 0 || y4 < 0 || 
	        x1 >= World.WIDTH || x2 >= World.WIDTH || 
	        x3 >= World.WIDTH || x4 >= World.WIDTH ||
	        y1 >= World.HEIGHT || y2 >= World.HEIGHT || 
	        y3 >= World.HEIGHT || y4 >= World.HEIGHT) {
	        return false;
	    }

	    return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
	             tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
	             tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
	             tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);
	}

	
	public void render(Graphics g) {
		 int xStart = Camera.x / TILE_SIZE;
		    int yStart = Camera.y / TILE_SIZE;

		    int xFinal = xStart + (Game.WIDTH / TILE_SIZE);
		    int yFinal = yStart + (Game.HEIGHT / TILE_SIZE);

		    for (int xx = xStart; xx <= xFinal; xx++) {
		        for (int yy = yStart; yy <= yFinal; yy++) {
		            if (xx >= 0 && xx < WIDTH && yy >= 0 && yy < HEIGHT) {
		                Tile tile = tiles[xx + (yy * WIDTH)];
		                tile.render(g);
		            	}
			}
		}
	}
}