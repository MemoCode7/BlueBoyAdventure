package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;
import object.OBJ_Axe;
import object.OBJ_Door;
import object.OBJ_Key;
import object.OBJ_Potion_Red;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp)
    {
        this.gp = gp;
    }


    public void setObject()
    {
        int i = 0;

        gp.npc[i] = new OBJ_Key(gp);
        gp.npc[i].worldX = gp.tileSize*25;
        gp.npc[i].worldY = gp.tileSize*23;
        i++;
        gp.npc[i] = new OBJ_Key(gp);
        gp.npc[i].worldX = gp.tileSize*21;
        gp.npc[i].worldY = gp.tileSize*19;
        i++;
        gp.npc[i] = new OBJ_Key(gp);
        gp.npc[i].worldX = gp.tileSize*26;
        gp.npc[i].worldY = gp.tileSize*21;
        i++;
        gp.npc[i] = new OBJ_Axe(gp);
        gp.npc[i].worldX = gp.tileSize*33;
        gp.npc[i].worldY = gp.tileSize*21;
        i++;
        gp.npc[i] = new OBJ_Potion_Red(gp);
        gp.npc[i].worldX = gp.tileSize*22;
        gp.npc[i].worldY = gp.tileSize*27;  
    }


    public void setNPC()
    {
     gp.npc[0] = new NPC_OldMan(gp);
     gp.npc[0].worldX = gp.tileSize*21;
     gp.npc[0].worldY = gp.tileSize*21;

    //  gp.npc[0] = new NPC_OldMan(gp);
    //  gp.npc[0].worldX = gp.tileSize*9;
    //  gp.npc[0].worldY = gp.tileSize*10;
    }

    public void  setMonster()
    {
        int i=0;

        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*23;
        gp.monster[i].worldY = gp.tileSize*36;
        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*23;
        gp.monster[i].worldY = gp.tileSize*37;

        // gp.monster[0] = new MON_GreenSlime(gp);
        // gp.monster[0].worldX = gp.tileSize*11;
        // gp.monster[0].worldY = gp.tileSize*10;

        // gp.monster[1] = new MON_GreenSlime(gp);
        // gp.monster[1].worldX = gp.tileSize*11;
        // gp.monster[1].worldY = gp.tileSize*11;

    }
}
