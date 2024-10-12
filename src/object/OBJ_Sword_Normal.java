package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{
    public OBJ_Sword_Normal(GamePanel gp){
        super(gp);

        name = "EXCALIBUR";
        down1 = setup("/res/objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 1;
        description = "["+name+"]\n GODS OWN SWORD.";
    }
}
