package object;

import main.GamePanel;
import entity.Entity;

public class OBJ_Shield_Wood extends Entity{
    public OBJ_Shield_Wood(GamePanel gp){
        super(gp);

        type = type_sheild;

        name = "Wood Shield";
        down1 = setup("/res/objects/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "["+name+"]\nShield of Hephaestus.";
    }
}
