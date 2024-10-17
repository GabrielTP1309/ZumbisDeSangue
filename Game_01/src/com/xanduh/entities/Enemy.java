package com.xanduh.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.xanduh.main.Game;
import com.xanduh.world.Camera;
import com.xanduh.world.World;

public class Enemy extends Entity {
    
    private double speed = 0.6;
    
    private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;
    
    private BufferedImage[] sprites;
    
    private int life = 2;
    
    private boolean isDamaged = false;
    private int damageFrames = 10, damageCurrent = 0;
    
    private boolean isDead = false;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[2];
        sprites[0] = Game.spritesheet.getSprite(112, 16, World.TILE_SIZE, World.TILE_SIZE);
        sprites[1] = Game.spritesheet.getSprite(112 + World.TILE_SIZE, 16, World.TILE_SIZE, World.TILE_SIZE);
    }
    
    public void tick() {
        if (isDead) {
            return; // Do nothing if the enemy is dead
        }

        if (!isCollidingPlayer()) {
            if ((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY())
                    && !isColliding((int)(x + speed), this.getY())) {
                x += speed;
            } else if ((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY())
                    && !isColliding((int)(x - speed), this.getY())) {
                x -= speed;
            }
        
            if ((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed))
                    && !isColliding(this.getX(), (int)(y + speed))) {
                y += speed;
            } else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - speed))
                    && !isColliding(this.getX(), (int)(y - speed))) {
                y -= speed;
            }
        
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex)
                    index = 0;
            }
        } else {
            if (Game.rand.nextInt(100) < 10) {
                if (!isDamaged) 
                    Game.player.life -= Game.rand.nextInt(3);
                else
                    Game.player.life -= Game.rand.nextInt(3) + 3;
                Game.player.isDamaged = true;
            }
        }
        
        collidingBullet();
        
        if (life <= 0) {
            destroySelf();
            return;
        }
        
        if (isDamaged) {
            speed = 1.0;
            this.damageCurrent++;
            if (this.damageCurrent >= this.damageFrames) {
                isDamaged = false;
                this.damageCurrent = 0;
            }
        }
    }
    
    public void destroySelf() {
        if (Game.rand.nextInt(100) < 60)
            Game.entities.add(new Bullet(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE, Entity.BULLET_EN));

        this.sprite = Entity.ENEMY_DEAD;
        this.isDead = true;
    }
    
    public void collidingBullet() {
        for (int i = 0; i < Game.bullets.size(); i++) {
            Entity e = Game.bullets.get(i);
            if (e instanceof BulletShoot) {
                if (Entity.isColliding(this, e)) {
                    isDamaged = true;
                    if (Game.rand.nextInt(100) < 40)
                        life -= 2;
                    else
                        life -= 1;
                    Game.bullets.remove(e);
                    return;
                }
            }
        }
    }
    
    public boolean isCollidingPlayer() {
        Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
        
        return enemyCurrent.intersects(player);
    }
    
    public boolean isColliding(int xNext, int yNext) {
        Rectangle enemyCurrent = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);
        
        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy e = Game.enemies.get(i);
            if (e == this || e.isDead()) // Ignore dead enemies
                continue;
            Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
            
            if (enemyCurrent.intersects(targetEnemy))
                return true;
        }
        
        return false;
    }
    
    public void render(Graphics g) {
        if (isDead) {
            g.drawImage(Entity.ENEMY_DEAD, this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else if (!isDamaged) {
            g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else {
            g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }

    public boolean isDead() {
        return isDead;
    }
}