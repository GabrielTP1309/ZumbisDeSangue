package com.xanduh.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.xanduh.graficos.Spritesheet;
import com.xanduh.main.Game;
import com.xanduh.world.Camera;
import com.xanduh.world.World;

public class Player extends Entity{

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	private boolean hasGun = false;
	
	public int ammo = 6;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public boolean shoot = false, mouseShoot = false;
	
	public double life = 8, maxLife = 8;
	public int mx, my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}
	
	public void tick() {
		moved = false;
		if (right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		
		if (up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			y += speed;
		}
		
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex)
					index = 0;
			}
		}
		
		this.checkColisionGun();
		this.checkColisionAmmo();
		this.checkColisionLifePack();
		
		if (isDamaged) {
			this.damageFrames++;
			if (this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if (shoot) {
			shoot = false;
			if (hasGun && ammo > 0) {
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 8;
				
				if (dir == right_dir) {
					px = 11;
					dx = 1;
				} else {
					px = -11;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if (mouseShoot) {
		    mouseShoot = false;
		    if (hasGun && ammo > 0) {
		        ammo--;
		        double angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));
		        double dx = Math.cos(angle);
		        double dy = Math.sin(angle);
		        int px = 8;
		        int py = 8;

		        BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
		        Game.bullets.add(bullet);
		    }
		}
		
		if (life <= 0) {
			Game.entities = new ArrayList<Entity>();
	        Game.enemies = new ArrayList<Enemy>();
	        Game.spritesheet = new Spritesheet("C:/Users/gabri/OneDrive/Área de Trabalho/code/Game_01/res/spritesheet.png");
	        Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
	        Game.entities.add(Game.player);
	        Game.world = new World("C:/Users/gabri/OneDrive/Área de Trabalho/code/Game_01/res/map.png");
	        return;
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkColisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isColliding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkColisionAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof Bullet) {
				if (Entity.isColliding(this, atual)) {
					ammo++;
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkColisionLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if (atual instanceof LifePack) {
				if (Entity.isColliding(this, atual)) {
					life += 4;
					if (life >= 8)
						life = 8;
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + 8, this.getY() - Camera.y, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.x - 8, this.getY() - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamage, this.getX()-Camera.x, this.getY()-Camera.y, null);
			if (hasGun) {
				if (dir == right_dir)
					g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + 8, this.getY() - Camera.y, null);
				else
					g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.x - 8, this.getY() - Camera.y, null);
			}
		}
	}

}
