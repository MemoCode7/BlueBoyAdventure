package main;


import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
public class Config {
    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;

    }

    public void saveConfig(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            //FULLSREEN
            if(gp.fullScreenOn == true){
                bw.write("On");

            }
            if(gp.fullScreenOn == false){
                bw.write("Off");
            }
            bw.newLine();

            //Music volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            //SE VOLUME
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();


            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(){
        try{

            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            String s = br.readLine();

            //FULLSCREEN
            if(s.equals("On")){
                gp.fullScreenOn = true;
            }
            if(s.equals("Off")){
                gp.fullScreenOn = false;
            }

            //Music Volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            //SE Volume
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            br.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
