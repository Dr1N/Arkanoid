package javaarkanoid;

import java.awt.Color;
import java.awt.Graphics;

public class Ball
{
    private int x, y;
    private final int size; 
    private final int speed;
    private int dirX, dirY;
   
    public Ball()
    {
        this.x = 400;
        this.y = 300;
        this.size = 18;
        this.speed = 3;
        this.dirX = 1;
        this.dirY = 1;
    }
   
    public Ball(int x, int y, int size, int speed)
    {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.dirX = 1;
        this.dirY = 1;
    }
    
    public void paint(Graphics g)
    {
        g.setColor(Color.BLUE);
        g.fillOval(this.x, this.y, this.size, this.size);
    }
    
    public void move(int Wdt, int Hgt)
    {
        this.x = this.x + this.speed * this.dirX; 
        this.y = this.y + this.speed * this.dirY; 

        if(this.x < 0)
        {
            this.dirX = 1;
        }
        else if(x > Wdt)
        {
            this.dirX = -1;
        }

        if(this.y < 0)
        {
            this.dirY = 1;
        }
    }
  
    public int getY()
    {
        return this.y;
    }
   
    public int getX()
    {
        return this.x;
    }
   
    public int getSize()
    {
        return this.size;
    }
   
    public int getDirX()
    {
        return this.dirX;
    }
   
    public int getDirY()
    {
        return this.dirY;
    }
   
    public void setDirY(int dy)
    {
        this.dirY = dy;
    }
}