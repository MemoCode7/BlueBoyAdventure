package main;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;

import entity.Entity;

import tile.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize*maxScreenCol;
    public final int screenHeigth = tileSize*maxScreenRow;

    //World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
   

    
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread;

    //Entity and Object
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();
    
    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialougeState = 3;
    public final int characterState = 4;
    
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeigth));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setUpGame(){
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        //playMusic(0);
        
        gameState = titleState;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
        
    }
    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        int timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime-lastTime);
            lastTime = currentTime;
            
            if(delta >=1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >=1000000000){
                System.out.println("FPS: "+drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
        
        if (gameState == playState)
        {
            //player
            player.update();
            //npc
            for(int i=0;i<npc.length;i++){
                if(npc[i]!=null){
                    npc[i].update();
                }
            }

            for(int i = 0; i< monster.length; i++){
                if(monster[i] != null)
                {
                    if (monster[i].alive == true && monster[i].dying == false);
                    {
                        monster[i].update();
                    }

                    if (monster[i].alive == false)
                    {
                        monster[i] = null;
                    }

                }
            }
        }
        if (gameState == pauseState)
        {
            //Nothing
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //TITLE SCREEN 
        if (gameState == titleState)
        {
            ui.draw(g2);
        }
        else
        {

        //TILE
        tileM.draw(g2);


            //ADD ENTITIES TO THE LIST
            entityList.add(player);

            for (int i=0; i<npc.length; i++)
            {
                if (npc[i] != null)
                {
                    entityList.add(npc[i]);
                }
            }

            for (int i=0; i<obj.length; i++)
            {
                if (obj[i] != null)
                {
                    entityList.add(obj[i]);
                }
            }

            for (int i=0; i<monster.length; i++)
            {
                if (monster[i] != null)
                {
                    entityList.add(monster[i]);
                }
            }

            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                public int compare(Entity e1, Entity e2)
                {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            //DRAW ENTITIES
            for (int i=0; i<entityList.size(); i++)
            {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();


        //UI
        ui.draw(g2);
        }

        

        
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){
      se.setFile(i);
      se.play();
    }

}
