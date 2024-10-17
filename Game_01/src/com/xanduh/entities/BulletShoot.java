package com.xanduh.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.xanduh.main.Game;
import com.xanduh.world.Camera;
import com.xanduh.world.World;

public class BulletShoot extends Entity {
    
    private double dx;
    private double dy;
    private double spd = 8;
    
    private int life = 40, curLife = 0;
    
    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;
    }
    
    public void tick() {
        double nextX = x + dx * spd;
        double nextY = y + dy * spd;
        
        if (World.isFree((int)nextX, (int)nextY)) {
            x = nextX;
            y = nextY;
        } else {
            Game.bullets.remove(this);
            return;
        }
        
        curLife++;
        if (curLife == life) {
            Game.bullets.remove(this);
            return;
        }
    }
    
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
    }
}