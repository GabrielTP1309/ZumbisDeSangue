package com.xanduh.graficos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Spritesheet {

    private BufferedImage spritesheet;

    public Spritesheet(String path) {
        try {
            // Debug statement to print the file path
            System.out.println("Loading spritesheet from: " + path);
            spritesheet = ImageIO.read(new File(path));
            if (spritesheet == null) {
                throw new IllegalArgumentException("Spritesheet image not found at path: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return spritesheet.getSubimage(x, y, width, height);
    }
}