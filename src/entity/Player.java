package entity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.CollisionChecker;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;



public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter=0; 
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp , KeyHandler keyH){
        super(gp);

        this.keyH = keyH;
        screenX = gp.screenWidth/2-(gp.tileSize/2);
        screenY = gp.screenHeigth/2-(gp.tileSize/2);

        solidArea = new Rectangle();
        //COLLISION AREA FOR CHARACTER      8 , 16 , 32 ,32
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        //  attackArea.width = 36;
        //  attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize*23;
        worldY = gp.tileSize*21;

        speed = 6;
        direction="down";


        //PLAYER STATUS
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        level = 1;
        strength = 1; // the more strength he has , the more damage he gives
        dexterity =1;// the more dextirity he has , the less damage he receives
        exp = 1;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        attack = getAttack();// the totol atttak value is decided by the strength and weapon 
        defense = getDefense();// the total defense is decided by the dexterity and shield
    }
    

    public void setDefaultPosition(){
        worldX = gp.tileSize*23;
        worldY = gp.tileSize*21;
        direction="down";
    }

    public void restoreLifeAndMana(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }
    public void setItems(){
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
    }

    public int getAttack()
    {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }
    
    public void getPlayerImage(){
       
        up1 = setup("/res/player/boy_up_1",gp.tileSize,gp.tileSize);
        up2 = setup("/res/player/boy_up_2",gp.tileSize,gp.tileSize);
        down1 = setup("/res/player/boy_down_1",gp.tileSize,gp.tileSize);
        down2 = setup("/res/player/boy_down_2",gp.tileSize,gp.tileSize);
        left1 = setup("/res/player/boy_left_1",gp.tileSize,gp.tileSize);
        left2 = setup("/res/player/boy_left_2",gp.tileSize,gp.tileSize);
        right1 = setup("/res/player/boy_right_1",gp.tileSize,gp.tileSize);
        right2 = setup("/res/player/boy_right_2",gp.tileSize,gp.tileSize);

    }
    
    public void getPlayerAttackImage()
    {
        if (currentWeapon.type == type_sword)
        {
            attackUp1 = setup("/res/player/boy_attack_up_1",gp.tileSize,gp.tileSize*2);
            attackUp2 = setup("/res/player/boy_attack_up_2",gp.tileSize,gp.tileSize*2);
            attackDown1 = setup("/res/player/boy_attack_down_1",gp.tileSize,gp.tileSize*2);
            attackDown2 = setup("/res/player/boy_attack_down_2",gp.tileSize,gp.tileSize*2);
            attackLeft1 = setup("/res/player/boy_attack_left_1",gp.tileSize*2,gp.tileSize);
            attackLeft2 = setup("/res/player/boy_attack_left_2",gp.tileSize*2,gp.tileSize);
            attackRight1 = setup("/res/player/boy_attack_right_1",gp.tileSize*2,gp.tileSize);
            attackRight2 = setup("/res/player/boy_attack_right_2",gp.tileSize*2,gp.tileSize);
        }

        if (currentWeapon.type == type_axe)
        {
            attackUp1 = setup("/res/player/boy_axe_up_1",gp.tileSize,gp.tileSize*2);
            attackUp2 = setup("/res/player/boy_axe_up_2",gp.tileSize,gp.tileSize*2);
            attackDown1 = setup("/res/player/boy_axe_down_1",gp.tileSize,gp.tileSize*2);
            attackDown2 = setup("/res/player/boy_axe_down_2",gp.tileSize,gp.tileSize*2);
            attackLeft1 = setup("/res/player/boy_axe_left_1",gp.tileSize*2,gp.tileSize);
            attackLeft2 = setup("/res/player/boy_axe_left_2",gp.tileSize*2,gp.tileSize);
            attackRight1 = setup("/res/player/boy_axe_right_1",gp.tileSize*2,gp.tileSize);
            attackRight2 = setup("/res/player/boy_axe_right_2",gp.tileSize*2,gp.tileSize);
        }
    }

    public void update(){
        if(attacking == true){
                attacking();
        }
 
        else if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true){
            if(keyH.upPressed == true){
                direction="up";
                
            }else if(keyH.downPressed == true){
                direction="down";
                
            }else if(keyH.leftPressed == true){
                direction="left";
                
            }else if(keyH.rightPressed == true){
                direction="right";
                
            }

            //CHECK TILE CONDITION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //check npc collison
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this,gp.monster);
            contactMonster(monsterIndex);

            // check interactive title collision
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);

            // CHECK EVENT
            gp.eHandler.checkEvent();

            //IF COLLISION IS FALSE , PLAYER CAN MOVE
            if(collisionOn == false && keyH.enterPressed == false){
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed;break;
                    case "right": worldX += speed; break;
                }
            }

            if(keyH.enterPressed == true && attackCanceled == false){
                gp.playSE(2);
                attacking = true;
                spriteCounter = 0;


            }

            attackCanceled = false;

            gp.keyH.enterPressed = false;
            spriteCounter++;
            if(spriteCounter>12){
                if(spriteNum==1){
                    spriteNum = 2;
                }
                else if(spriteNum==2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }    
        else
        {
            standCounter++;
            if(standCounter==20){
                spriteNum = 1;
                standCounter=0;
            }
        }

        if (gp.keyH.shotKeyPressed == true && projectile.alive == false && shotAvailableCounter == 30 && projectile.haveResource(this)==true)
        {
            // SET DEFAULT COORDINATES, DIRECTION & USER
            projectile.set(worldX, worldY, direction, true, this);

            //Subtract the Cost(Mana,ammo,etc.)
            projectile.subtractResource(this);

            //ADD IT TO THE LIST
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
            gp.playSE(9);
        }

        // THIS NEEDS TO BE OUTSIDE OF KEY IF STATEMENT
        if(invincible ==true){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }   
        if (shotAvailableCounter < 30)
        {
            shotAvailableCounter++;
        }
        if(life > maxLife){
            life = maxLife;
        }
        if(mana > maxMana){
            mana = maxMana;
        }

        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.playSE(11);
        }
    }
    
    public void attacking(){
        spriteCounter++;
        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;
            
            //save the current worldX,worldY and solidArea.
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX/Y for the player attackArea
            switch (direction) {
                case "up": worldY -= attackArea.height; 
    
                    break;
                case "down": worldY += attackArea.height; 
    
                    break;
                case "left": worldX -= attackArea.width; 
    
                    break;
                case "right": worldX += attackArea.width; 
    
                    break;
            }
            //attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //check monster collision with the updated worldX,worldY, solidArea.
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageInteractiveTile(iTileIndex);

            //After checking collision, restore the orignial data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }


    }

    public void pickUpObject(int i)
    {
      if(i!=999)
      {

        //Pickup only items.
        if(gp.obj[i].type == type_pickupOnly){
            gp.obj[i].use(this);
            gp.obj[i]=null;
        }
        //Inventory items.
        else{
            String text;

            if (inventory.size() != maxInventorySize)
            {
                inventory.add(gp.obj[i]);
                gp.playSE(1);
                text = "Got a " + gp.obj[i].name + "!";
            }
            else
            {
                text = "You cannot carry any more!";
            }
    
            gp.ui.addMessage(text);
            gp.obj[i] = null;

            }
        }
    }
    
    public void interactNPC(int i){

        if(gp.keyH.enterPressed == true){
            
            if(i!=999)
            {  
                attackCanceled = true;
                gp.gameState = gp.dialougeState;
                gp.npc[i].speak();
            }   
            
              
        }
    }

    public void  contactMonster(int i){
        if(i != 999){

            if(invincible == false && gp.monster[i].dying == false)
            {
                int damage = gp.monster[i].attack - defense;
                if (damage < 0)
                {
                    damage = 0;
                }

                life -= damage;
                gp.playSE(6);
               life -= 1; 
               invincible = true;
            }
                
        }
    }
    
    public void damageMonster(int i, int attack)
    {

        if(i!=999)
        {
            if(gp.monster[i].invincible == false)
            {
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;
                if (damage < 0)
                {
                    damage = 0;
                }

                gp.monster[i].life -= damage;
                gp.ui.addMessage(damage + " damage!");

                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if(gp.monster[i].life <= 0)
                {
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("killed the " + gp.monster[i].name + "!"); 
                    gp.ui.addMessage("Exp + " + gp.monster[i].exp + "!");
                    exp += gp.monster[i].exp;
                    checkLevelUp(); 
                }
            }
        }
    }
    public void damageInteractiveTile(int i){
        if(i!=999 && gp.iTile[i].destructible == true && gp.iTile[i].isCorrectItem(this)==true && gp.iTile[i].invincible == false){
            gp.iTile[i].playSE();
            gp.iTile[i].life--;
            gp.iTile[i].invincible = true;

            //Generate particle
            generateParticle(gp.iTile[i], gp.iTile[i]);

            if(gp.iTile[i].life == 0){
                gp.iTile[i] = gp.iTile[i].getDestroyedForm();
            }
            
        }
    }
    public void checkLevelUp()
    {
        if (exp >= nextLevelExp)
        {
            level++;
            nextLevelExp = nextLevelExp*2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(7);
            gp.gameState = gp.dialougeState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n"
                                    +   "You feel Stronger!";
        }
    }

    public void selectItem()
    {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if (itemIndex < inventory.size())
        {
            Entity selectedIteam = inventory.get(itemIndex);

            if (selectedIteam.type == type_sword || selectedIteam.type == type_axe)
            {
                currentWeapon = selectedIteam;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedIteam.type == type_sheild)
            {
                currentShield = selectedIteam;
                defense = getDefense();
            }
            if (selectedIteam.type == type_consumable)
            {
                selectedIteam.use(this);
                inventory.remove(itemIndex);
            }



        }
    }

    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        switch(direction){
            case "up":
                if(attacking == false){
                    if(spriteNum == 1){
                        image=up1;
                    }
                    if(spriteNum == 2){
                        image=up2;
                    }
                }
                if(attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1){
                        image=attackUp1;
                    }
                    if(spriteNum == 2){
                        image=attackUp2;
                    }
                }
            break;

            case "down":
                if(attacking == false){
                    if(spriteNum == 1){
                        image=down1;
                    }
                    if(spriteNum == 2){
                        image=down2;
                }
                }
                if(attacking == true){
                    if(spriteNum == 1){
                        image=attackDown1;
                    }
                    if(spriteNum == 2){
                        image=attackDown2;
                }
            } 
            break;

            case "left":
                if(attacking == false){
                    if(spriteNum == 1){
                        image=left1;
                    }   
                    if(spriteNum == 2){
                        image=left2;
                    }
                }
                if(attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1){
                        image=attackLeft1;
                    }   
                    if(spriteNum == 2){
                        image=attackLeft2;
                    }
                }
                
            break;

            case "right":
                if(attacking == false){
                    if(spriteNum == 1){
                        image=right1;
                    }   
                    if(spriteNum == 2){
                        image=right2;
                    }
                }
                if(attacking == true){
                    if(spriteNum == 1){
                        image=attackRight1;
                    }   
                    if(spriteNum == 2){
                        image=attackRight2;
                    }
                }
            break;
        }

        if(invincible == true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        


        g2.drawImage(image,tempScreenX,tempScreenY,null);
       //reset
       g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
}
