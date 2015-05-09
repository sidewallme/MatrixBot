/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixbot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * "pos_x":237,
         "pos_y":243,
         "cluster_id":23
         * 
 * @author erichsu
 */
public class PageMaker {
    public static void main() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String source = "gear_profiles_old.json";
        String clustfile = "cluster.txt";
        BufferedReader br1 = new BufferedReader(new FileReader(source));
        BufferedReader br2 = new BufferedReader(new FileReader(clustfile));
        PrintWriter wr1 = new PrintWriter("gear_profiles.json", "UTF-8");
        String line = br1.readLine();
        String threeElements = "";
        while(line!=null){
            
            if(line.contains("pos_x")){
                threeElements = br2.readLine();
                line = "\"pos_x\"" + ":" + getElement(threeElements, 0);
            }
            else if(line.contains("pos_y")){
                line = "\"pos_y\"" + ":" + getElement(threeElements, 1);
            }
            else if(line.contains("cluster_id")){
                line = "\"cluster_id\"" + ":" + getElement(threeElements, 2);
            }
            
            wr1.println(line);
            line=br1.readLine();
        }
    }
    public static String getElement(String input, int i){
        String[] elements = input.split("\\s+");
        return elements[i];
    }
}
