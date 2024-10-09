package main;
import java.awt.Rectangle;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[] [];

    public EventHandler(GamePanel gp){
        this.gp = gp;
        eventRect = new EventRect[gp.maxScreenCol][gp.maxScreenRow];
        int col = 0;
        int row = 0;
        while (col<gp.maxScreenCol && row<gp.maxScreenRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row] .x = 23;
            eventRect[col][row] .y = 23;
            eventRect[col][row] .width = 2;
            eventRect[col][row] .height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[row][col].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
            col++;
            if(col == gp.maxScreenCol){
                col = 0;
                row++;
            }
        }    
    }

    public void checkEvent(){
        if(hit(27,16,"right")==true){
            damagePit(gp.dialougeState);
        }

        if(hit(23,12,"up")==true){
            healingPool(gp.dialougeState);
        }
        if(hit(20,16,"left")==true){
            teleport(gp.dialougeState);
        }
    }

    public boolean hit(int col,int row , String reqDirection){
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;

        if(gp.player.solidArea.intersects(eventRect[col][row])) {
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void damagePit(int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "ALLAH HU AKHBAAARRRR....\nBOOOOOOM";
        gp.player.life -= 1;
    }
    public void healingPool(int gameState){
        if(gp.keyH.enterPressed == true){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You drink the holy urine of baba. \nYour life has been recovered";
            gp.player.life = gp.player.maxLife;
        }

    }

    public void teleport(int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Aaila Jaadu?!";
        gp.player.worldX = gp.tileSize*37;
        gp.player.worldY = gp.tileSize*10;
    }
}
