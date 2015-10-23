package javaarkanoid;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
  
public class Bat implements MouseMotionListener, KeyListener
{
    class MoveThread extends Thread
    {
        @Override
        public void run()
        {
            while(true)
            {
                if(Bat.this.isMoved == true)
                {
                    if(Bat.this.direct == Bat.LEFT)
                    {
                        Bat.this.left -= 8;
                        if(Bat.this.left <= 0) Bat.this.left = 0;
                    }
                    else if(Bat.this.direct == Bat.RIGTH)
                    {
                        Bat.this.left += 8;
                        if(Bat.this.left >= 820) Bat.this.left = 820;
                    }
                }
                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ie){}
            }
        }
    }
    
    private static final int LEFT = -1;
    private static final int RIGTH = 1;
    private boolean isMoved;
    private int direct;
    
    private int left;
    private int top = 0;
    private int height = 0;
    private int width = 0;
          
    public Bat(){ this.startMovie(); }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        this.left = e.getX();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        this.left = e.getX();
    }
    
    @Override
    public void keyTyped(KeyEvent ke){}

    @Override
    public void keyPressed(KeyEvent ke)
    {
        if(ke.getKeyCode() == 37)
        {
            if(this.isMoved == false) this.isMoved = true;
            this.direct = Bat.LEFT;
        }
        else if(ke.getKeyCode() == 39)
        {
            if(this.isMoved == false) this.isMoved = true;
            this.direct = Bat.RIGTH;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
        if(ke.getKeyCode() == 37 || ke.getKeyCode() == 39)
        {
            if(this.isMoved == true) this.isMoved = false;
        }
    }
    
    public final void startMovie()
    {
        MoveThread mt = new MoveThread();
        mt.setDaemon(true);
        mt.start();
    }
  
    public int getTop()
    {
        return this.top;
    }
    
    public int getLeft()
    {
        return this.left;
    }
    
    public int getHeight()
    {
        return this.height;
    }
    
    public int getWidth()
    {
        return this.width;
    }
    
    public void setTop(int t)
    {
        this.top = t;
    }
    
    public void setLeft(int l)
    {
        this.left = l;
    }
    
    public void setHeight(int h)
    {
        this.height = h;
    }
    
    public void setWidth(int w)
    {
        this.width = w;
    }
}