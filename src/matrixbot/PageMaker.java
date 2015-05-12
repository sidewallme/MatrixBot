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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * "pos_x":237,
         "pos_y":243,
         "cluster_id":23
         * 
 * @author erichsu
 */
public class PageMaker {
        public static ArrayList<Integer> ids = new ArrayList<>();
        public static ArrayList<Double> xs = new ArrayList<>();
        public static ArrayList<Double> ys = new ArrayList<>();
        public static ArrayList<Integer> clus = new ArrayList<>();
        
        public static Map<Integer, Double> idToX = new HashMap<>();
        public static Map<Integer, Double> idToY = new HashMap<>();
        public static Map<Integer, Integer> idToClus = new HashMap<>();

        
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        readGephi();
        init();
        extract();
        
    }
    
    public static void init(){
        
        for(int i=0;i<270;i++){
            idToX.put(i,0.);
            idToY.put(i,0.);
            idToClus.put(i,0);
        }
        
        int id;
        for(int i=0;i<ids.size();i++){            
            id = ids.get(i);
            idToX.put(id, (xs.get(i) + 5000.)/10000*800);
            idToY.put(id, (ys.get(i)+ 5000.)/10000*600);
            idToClus.put(id, clus.get(i));
        }
    }
    
    public static void readGephi() throws IOException{
        String sourceFile = "gephi_output.txt";
        BufferedReader br1 = new BufferedReader(new FileReader(sourceFile));
        String line = br1.readLine();
        while(line != null){
            if(line.contains("<node id")){
                int a = line.indexOf("\"");
                int b = line.lastIndexOf("\"");
                int id = Integer.parseInt(line.substring(a+1,b));
                ids.add(id-1);
            }
            if(line.contains(" for=\"Modularity Class\" ")){
                int a = line.indexOf("e=\"");
                int b = line.lastIndexOf("\"");
                int cid = Integer.parseInt(line.substring(a+3,b));
                clus.add(cid);
            }
            if(line.contains("viz:position")){
                int sp = line.indexOf("y=");
                String one = line.substring(0,sp);
                String two = line.substring(sp+3,line.indexOf("z="));
                
                int a = one.indexOf("\"");
                int b = one.lastIndexOf("\"");

                double x = Double.parseDouble(one.substring(a+1,b));
                xs.add(x);

                b = two.lastIndexOf("\"");
                double y = Double.parseDouble(two.substring(0,b));
                ys.add(y);
            }
            
            line=br1.readLine();
        }
        
    }
    public static void extract() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String source = "gear_profiles_old.json";

        BufferedReader br1 = new BufferedReader(new FileReader(source));

        PrintWriter wr1 = new PrintWriter("gear_profiles.json", "UTF-8");
        System.out.println("Extracting");
        
        String line = br1.readLine();
        String threeElements = "";
        
        int currentID = 0;
        while(line!=null){
                    //System.out.println(line);

            if(line.contains("\"member_id\":")){
                currentID = Integer.parseInt( line.substring(line.indexOf(":")+1, line.indexOf(",")) );
                        System.out.println("Extracting");

            }
            else if(line.contains("pos_x")){
                line = "\"pos_x\"" + ":" + idToX.get(currentID)+",";
            }
            else if(line.contains("pos_y")){
                line = "\"pos_y\"" + ":" + idToY.get(currentID)+",";
            }
            else if(line.contains("cluster_id")){
                line = "\"cluster_id\"" + ":" + idToClus.get(currentID);
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
