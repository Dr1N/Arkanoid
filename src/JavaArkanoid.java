package javaarkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public class JavaArkanoid
{
    public static void main(String[] arg)
    {
        // Окно
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JavaArkanoidPanel arkanoidPanel = new JavaArkanoidPanel();
        f.add(arkanoidPanel);
        f.add(arkanoidPanel);
        f.pack();
        arkanoidPanel.init();
        f.setBounds(arkanoidPanel.getiFramex(), arkanoidPanel.getiFramey(), arkanoidPanel.getiFramew(), arkanoidPanel.getiFrameh());
        f.setResizable(false);
        f.setTitle("Бест Арканоид. Для начала игры нажмите Ctrl + N");
        
        f.addKeyListener(arkanoidPanel.getBat());
        
        //Меню
        
        JMenuBar menu = new JMenuBar();
        f.setJMenuBar(menu);
        JMenu mnGame = new JMenu("Игра");
        menu.add(mnGame);
        JMenuItem mniStart = new JMenuItem("Старт");
        mniStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        mniStart.addActionListener(
            new ActionListener()
            {
                //Начало игры
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    arkanoidPanel.init();
                    arkanoidPanel.gameThread.start();
                }
            }
        );
        mnGame.add(mniStart);
        JSeparator separator = new JSeparator();
        mnGame.add(separator);
        JMenuItem mniExit = new JMenuItem("Выход");
        mniExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        mniExit.addActionListener(
            new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.exit(0);
                }
            }
        );
        mnGame.add(mniExit);
        
        f.setVisible(true);
    }
}

class JavaArkanoidPanel extends JPanel implements Runnable
{
    private ArrayList<Rectangle> Bricks;
    private Ball Ball;
    private final Bat Bat = new Bat();
    private int numBlocks;
    private int numRows;
    private BufferedImage image;
    private boolean winner = false;
    private boolean dead = false;
    private final int iFramex;
    private final int iFramey;
    private final int iFrameh;
    private final int iFramew;
    
    public Thread gameThread = null;
  	
    public JavaArkanoidPanel()
    {
        this.iFramex = 100;
        this.iFramey = 100;
        this.iFrameh = 500;
        this.iFramew = 850;
       
        this.addMouseMotionListener(this.Bat);
    }
    
    /**
     * Основной цикл
     */
    @Override
    public void run()
    {
        while(true)
        {
            this.Ball.move(this.getiFramew(), this.getiFrameh());
            this.gameOver();
            if(this.dead == true)
            {
                repaint();
                break;
            }
            else
            {
                this.collisionCheck();
                repaint();
                try 
                {
                    Thread.sleep(8);
                }
                catch(Exception ex){ System.out.println(ex.getMessage()); }
            }
        }
     }
   
    /**
     * Отрисовка состояния игры
     * @param g 
     */
    @Override
    public void paint(Graphics g)
    {
    	if(this.winner == true)
        {
            g.setColor(Color.WHITE); 
            g.fillRect(0 ,0 , this.getiFramew(), this.getiFrameh());
            g.setColor(Color.BLACK);
            g.drawString("ПОБЕДА!", 50, 20);
    	}
        else if(this.dead == true)
        {
      		g.setColor(Color.WHITE); 
        	g.fillRect(0, 0, this.getiFramew(), this.getiFrameh());
        	g.setColor(Color.BLACK);
                g.drawString("ПОРАЖЕНИЕ ;(", 50, 20);
        }
        else
        { 
            Graphics b = this.image.getGraphics();
            b.setColor(Color.GRAY); 
            b.fillRect(0, 0, this.getiFramew(), this.getiFrameh());
            b.setColor(Color.BLACK);

            this.Ball.paint(b);

            b.setColor(Color.YELLOW);
            for (int i = 0; i < this.Bricks.size(); i++)
            {
                Rectangle rect = (Rectangle)this.Bricks.get(i);
                b.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
            b.setColor(Color.BLACK);
            b.fillRect(this.Bat.getLeft(), this.Bat.getTop(), this.Bat.getWidth(), this.Bat.getHeight());
            g.drawImage(this.image, 0, 0, this);
        }
    }

    /**
     * Инициализация игры
     */
    public void init()
    {
        this.dead = false;
        this.winner = false;
        this.numBlocks = 10;
        this.numRows = 4;
        this.gameThread = new Thread(this);
        this.gameThread.setDaemon(true);
        
        this.startBrick();
        this.startBat();
        this.startBall();
                
        this.image = new BufferedImage(this.getiFramew(), this.getiFrameh(), BufferedImage.TYPE_INT_RGB);
     }
    
    /**
     * Проверка столкновений
     */
    public void collisionCheck()
    {
        Rectangle ballColl = new Rectangle(this.Ball.getX(), this.Ball.getY(), this.Ball.getSize(), this.Ball.getSize());
        Rectangle xBatCrash = new Rectangle(this.Bat.getLeft(), this.Bat.getTop(), this.Bat.getWidth(), this.Bat.getHeight());
        for( int i = 0; i < this.Bricks.size(); i++ )
        {
            Rectangle r = (Rectangle)this.Bricks.get(i);
            if (r.intersects(ballColl))
            {
                this.Bricks.remove(r);
                getGraphics( ).clearRect(r.x, r.y, r.width, r.height);
                this.Ball.setDirY(-1 * this.Ball.getDirY());
            }
        }
        if (ballColl.intersects(xBatCrash))
        {
            this.Ball.setDirY(-1);
        }
    }
      	
    /**
     * Проверка на конец игры
     */
    public void gameOver()
    {
        if(this.Bricks.isEmpty() == true)
        {
            this.winner = true;
        }
        else if (this.Ball.getY() >= this.getiFrameh() - this.Ball.getSize())
        {   
            this.dead = true;
        }
    }
    
    /**
     * Отрисовка блоков
     */
    public void startBrick()
    {
        this.Bricks = new ArrayList<>(); 
        int blockSize = this.getiFramew() / this.numBlocks;
        int blockHeight = 20;	
        for(int rows = 0; rows < this.getiFramew(); rows += blockSize)
        {
            for(int cols = 0; cols < this.numRows * blockHeight; cols += blockHeight)
            {
                Rectangle r = new Rectangle(rows, 80 + cols, blockSize - 2, blockHeight - 2);
                this.Bricks.add(r);
            }
        }
    }
    
    /**
     * Установка параметров биты
     */
    public void startBat()
    {
        this.Bat.setTop(400);
        this.Bat.setLeft(200);
        this.Bat.setHeight(20);
        this.Bat.setWidth(75);
    }
	
    /**
     * Добавление шара
     */
    public void startBall()
    {
        repaint();
        this.Ball = new Ball(220, 380, 15, 3);
    }

    public int getiFramex()
    {
        return iFramex;
    }

    public int getiFramey()
    {
        return iFramey;
    }

    public int getiFrameh()
    {
        return iFrameh;
    }

    public int getiFramew()
    {
        return iFramew;
    }
    
    public Bat getBat()
    {
        return this.Bat;
    }
}