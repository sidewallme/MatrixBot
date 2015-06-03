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
import java.util.StringTokenizer;

/**
 *
 * @author erichsu
 */
public class MatrixBot {
    public static int[][] coAuthorMatrix=new int[270][270];
    public static int[][] muCitationMatrix=new int[270][270];
    public static int[][] coCitationMatrix=new int[270][270];
    public static double[][] allMatrix=new double[270][270];
    
    public static int peoplesize = 0;
    public static double a = 1;
    public static double b = 0.;
    public static double c = 0.;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        init();
        
        String filename1 = "papers.json";
        String filename2 = "cocitation.txt";
        
        coAuthor(filename1);
        mutualCitation(filename2);
        coCitation(filename2);
        
        process();
        print();
        makeInputForGephi();
        
    }
    
    public static void init() throws FileNotFoundException, IOException{
        
        BufferedReader br1 = new BufferedReader(new FileReader("size.txt"));
        String line = br1.readLine();
        peoplesize = Integer.parseInt(line);
        br1.close();
        
        for(int i=0;i<peoplesize;i++){
            for(int j=0;j<peoplesize;j++){
                coAuthorMatrix[i][j]=0;
                muCitationMatrix[i][j]=0;
                coCitationMatrix[i][j]=0;
                allMatrix[i][j]=0;
            }
        }
    }
    
    public static void process(){
        for(int i=0;i<peoplesize;i++){
            coAuthorMatrix[i][i]=0;
            muCitationMatrix[i][i]=0;
            coCitationMatrix[i][i]=0;
        }
        
        for(int i=0;i<peoplesize;i++){
            for(int j=0;j<peoplesize;j++){
                if(muCitationMatrix[i][j]>0){
                    coCitationMatrix[i][j]=0;
                }
            }
        }
        
        
        
        for(int i=0;i<peoplesize;i++){
            for(int j=0;j<peoplesize;j++){
                allMatrix[i][j] = a*coAuthorMatrix[i][j]+ b*muCitationMatrix[i][j] + c*coCitationMatrix[i][j];
            }
        }
        
    }
    
    public static void print() throws FileNotFoundException, UnsupportedEncodingException{
        
        PrintWriter wr1 = new PrintWriter("coAuthorMatrix.txt", "UTF-8");
        PrintWriter wr2 = new PrintWriter("muCitationMatrix.txt", "UTF-8");
        PrintWriter wr3 = new PrintWriter("coCitationMatrix.txt", "UTF-8");
        PrintWriter wr4 = new PrintWriter("finalMatrix.txt", "UTF-8");
        
        for(int i=0;i<peoplesize;i++){
            for(int j=0;j<peoplesize;j++){
                wr1.print(coAuthorMatrix[i][j]);
                wr1.print(" ");
                wr2.print(muCitationMatrix[i][j]);
                wr2.print(" ");
                wr3.print(coCitationMatrix[i][j]);
                wr3.print(" ");
                wr4.print(allMatrix[i][j]);
                wr4.print(" ");
            }
            wr1.println("");
            wr2.println("");
            wr3.println("");
            wr4.println("");
        }
        wr1.close();
        wr2.close();
        wr3.close();
        wr4.close();
    }
    
    public static void coAuthor(String filename) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        while (line != null) {
            if(line.contains("collaborator_ids")){
                coAuthorParser(line);
            }
            line = br.readLine();
        }
        
    }
    
    public static void mutualCitation(String filename) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        List<Integer> currAuthor = new ArrayList<>();
        List<Integer> currCitation = new ArrayList<>();
        
        while (line != null) {
            if(line.contains("author_ids")){
                currAuthor = authorParser(line);
            }
            if(line.contains("citation_ids")){
                currCitation = authorParser(line);
                for(int a:currAuthor){
                    for(int b:currCitation){
                        muCitationMatrix[a][b] +=1;
                        muCitationMatrix[b][a] +=1;
                    }
                }
                currAuthor = new ArrayList<>();
                currCitation = new ArrayList<>();
            }
            line = br.readLine();
        }
    }
    
    public static void coCitation(String filename) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        while (line != null) {
            if(line.contains("citation_ids")){
                coCitationParser(line);
            }
            line = br.readLine();
        }
        
    }
    
    private static void coAuthorParser(String line){
        int one = line.indexOf("[");
        int two = line.indexOf("]");
        String numbers = line.substring(one+1,two);
        String delims = ",";
	StringTokenizer st = new StringTokenizer(numbers, delims);
        
        List<Integer> set=new ArrayList<>();
	while (st.hasMoreElements()){
            String temp=(st.nextElement()).toString();
            set.add(Integer.parseInt(temp));
	}
        
        for(int a:set){
            for(int b:set){
                coAuthorMatrix[a][b]+=1;
            }
        }
    }
    
    private static void coCitationParser(String line){
        int one = line.indexOf("[");
        int two = line.indexOf("]");
        String numbers = line.substring(one+1,two);
        String delims = ",";
	StringTokenizer st = new StringTokenizer(numbers, delims);
        
        List<Integer> set=new ArrayList<>();
	while (st.hasMoreElements()){
            String temp=(st.nextElement()).toString();
            set.add(Integer.parseInt(temp));
	}
        
        for(int a:set){
            for(int b:set){
                coCitationMatrix[a][b]+=1;
            }
        }
    }
    
    
    private static List<Integer> authorParser(String line){
        int one = line.indexOf("[");
        int two = line.indexOf("]");
        String numbers = line.substring(one+1,two);
        String delims = ",";
	StringTokenizer st = new StringTokenizer(numbers, delims);
        
        List<Integer> set=new ArrayList<>();
	while (st.hasMoreElements()){
            String temp=(st.nextElement()).toString();
            set.add(Integer.parseInt(temp));
        }
        return set;
    }
    
    private static void makeInputForGephi() throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter wr1 = new PrintWriter("inputForGephi.csv", "UTF-8");
        wr1.println("Source;Target;Weight;Type");
        double weight = 0;
        int id1=0;
        int id2=0;
        for(int i=0;i<peoplesize;i++){
            for(int j=0;j<peoplesize;j++){
                weight = allMatrix[i][j];
                if(weight !=0){
                    id1 = i+1;
                    id2 = j+1;
                    wr1.println(id1+";"+id2+";"+weight+";"+"undirected");
                }
            }
        }
        wr1.close();
    }
}
