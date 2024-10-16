package main;
import javax.swing.JPanel;

import data.SaveLoad;
import entity.Entity;
import entity.Player;

import entity.Entity;

import tile.TileManager;
import tile_interactive.InteractiveTile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize*maxScreenCol;//960pixel
    public final int screenHeigth = tileSize*maxScreenRow;//576puixel

    //World Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

   //For full screen
   int screenWidth2 = screenWidth;
   int screenHeigth2 = screenHeigth;
   BufferedImage tempScreen;
   Graphics2D g2;
   public boolean fullScreenOn = false;

    
    int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    SaveLoad saveLoad = new SaveLoad(this);
    Thread gameThread;

    //Entity and Object
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[20];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public InteractiveTile iTile[] = new InteractiveTile[50];
    public ArrayList<Entity>  projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    
    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialougeState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    
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
        aSetter.setInteractiveTile();
        //playMusic(0);
        
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth , screenHeigth , BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn == true){

            setFullScreen();
        }

    }

    public void retry(){
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        aSetter.setNPC();
        aSetter.setMonster();
    }
    public void restart(){
        player.setDefaultValues();
        player.setDefaultPosition();
        player.restoreLifeAndMana();
        player.setItems();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
    }

    public void setFullScreen(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);
        //GET FULLL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeigth2 = Main.window.getHeight();
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
                drawToTempScreen();//draw everything to the buffered image
                drawToScreen();//drraw the buffered image to the screen
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

            for(int i = 0; i< monster.length; i++)
            {
                if(monster[i] != null)
                {
                    if (monster[i].alive == true && monster[i].dying == false);
                    {
                        monster[i].update();
                    }

                    if (monster[i].alive == false)
                    {
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }

            for(int i = 0; i < projectileList.size() ; i++)
            {
                if(projectileList.get(i) != null)
                {
                    if (projectileList.get(i).alive == true && projectileList.get(i).dying == false);
                    {
                        projectileList.get(i).update();
                    }

                    if (projectileList.get(i).alive == false)
                    {
                        projectileList.remove(i);
                    }
                }
            }

            for(int i = 0; i < particleList.size() ; i++)
            {
                if(particleList.get(i) != null)
                {
                    if (particleList.get(i).alive == true && particleList.get(i).dying == false);
                    {
                        particleList.get(i).update();
                    }

                    if (particleList.get(i).alive == false)
                    {
                        particleList.remove(i);
                    }
                }
            }

          for(int i = 0; i < iTile.length;i++ ){
            if(iTile[i]!=null){
                iTile[i].update();
            }
          }
        }
        if (gameState == pauseState)
        {
            //Nothing
        }
    }
    
    public void drawToTempScreen(){
        //TITLE SCREEN 
        super.paintComponent(g2);
        if (gameState == titleState)
        {
            ui.draw(g2);
        }
        else
        {

        //TILE
        tileM.draw(g2);
        //Interactive tile
        for(int i = 0; i < iTile.length;i++ ){
            if(iTile[i]!=null){
                iTile[i].draw(g2);
            }
          }


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

            for (int i=0; i<projectileList.size(); i++)
            {
                if (projectileList.get(i) != null)
                {
                    entityList.add(projectileList.get(i));
                }
            }
            for (int i=0; i<particleList.size(); i++)
            {
                if (particleList.get(i) != null)
                {
                    entityList.add(particleList.get(i));
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
    
    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2,screenHeigth2,null);
        g.dispose();
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
