package alphabet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author John Giang
 */
public class Alphabet {

    /**
     * @param args the command line arguments
     */

    private static final ArrayList<String>      dictionary          = new ArrayList<String>();
    
    private static final ArrayList<String>      horizonText         = new ArrayList<String>();
    private static final ArrayList<String>      horizonReverses     = new ArrayList<String>();
    
    private static final ArrayList<String>      verticalText        = new ArrayList<String>();
    private static final ArrayList<String>      verticalReverses    = new ArrayList<String>();
    
    private static final ArrayList<DiagnalLine> diagnalList         = new ArrayList<>();
    private static final ArrayList<DiagnalLine> revDiagnalList      = new ArrayList<DiagnalLine>();    
    
    private static final LinkedHashMap<String, String> foundMap = new LinkedHashMap<>();
    
    private static char[][] grid;
    private static int row;
    private static int column;
    
    /**
     * Main
     */
    public static void main(String[] args) 
    {
        String filePath = "input.txt";        
        if (args != null && args.length > 0)
        {
            filePath = args[0];
        }
        buildGridData(filePath);
        scanHorizon();
        scanVertical();
        scanDiagnal();
        print();
    }
    
    public static void buildGridData(String filePath)         
    {
        List<String> lines = getLine(filePath);
        
        String  dimension   = lines.remove(0);
        int[]   dimensions  = getDimension(dimension);
        row         = dimensions[0];
        column      = dimensions[1];
        
        grid = new char[row][column];
        
        int     lastTextLine = 0;
        
        for (int i = 0; i < row; i++) 
        {
            String rowText = lines.get(i).replaceAll(" ", "");

            horizonText.add(rowText);
            horizonReverses.add(reverseString(rowText));
            
            for (int j = 0; j < rowText.length(); j++) {
                grid[i][j] = rowText.charAt(j);
            }
            lastTextLine = i;
        }
        
        
        for (int n = 0; n < column; n++) 
        {
            StringBuilder columnText = new StringBuilder();
            for (int m = 0; m < row; m++)
            {
                columnText.append(grid[m][n]);
            }

            verticalText.add(columnText.toString());
            verticalReverses.add(reverseString(columnText.toString()));
        }
//        System.out.println(verticalText);
//        System.out.println(verticalReverses);
        ++lastTextLine;

        for (int lineNumber = lastTextLine; lineNumber < lines.size(); lineNumber++) 
        {
            String word = lines.get(lineNumber);        
            dictionary.add(word);        
            foundMap.put(word, null);
        }
        buildDiagnal();
    }
        
    public static void print() 
    {
        
        for (Map.Entry<String, String> entry : foundMap.entrySet()) 
        {
            String key = entry.getKey();
            String val = entry.getValue();
            
            if (val != null)
            {
                System.out.println(val);
            }
            
        }
    }
    
    public static void scanDiagnal() 
    {
        matchDiagnal();
        matchRevDiagnal();
    }
    public static void matchDiagnal() 
    {
        for (DiagnalLine diagnalLine : diagnalList) {
            String text = diagnalLine.getText();
            
            for (String word : dictionary) {
                if (text.contains(word))
                {
                    int index = text.indexOf(word);
                    int row1  = diagnalLine.getRowIndex() + index;
                    int col1  = diagnalLine.getColIndex() + index;
                    int row2  = row1 + word.length() - 1;
                    int col2  = col1 + word.length() - 1;
                
                    String found= word + " " + row1+ ":" + col1+ " " + row2+ ":" + col2;
//                    System.out.println("found = " + found);
                    foundMap.put(word, found);                                       
                }
            }            
        }
    }
    
    
    public static void matchRevDiagnal() 
    {
        for (DiagnalLine diagnalLine : revDiagnalList) 
        {
            String text = diagnalLine.getText();
            
            for (String word : dictionary) 
            {
                if (text.contains(word))
                {
                    int index = text.indexOf(word);
                    int row1  = diagnalLine.getRowIndex() - index;
                    int col1  = diagnalLine.getColIndex() - index;
                    int row2  = row1 - word.length() + 1;
                    int col2  = col1 - word.length() + 1;
                
                    String found= word + " " + row1+ ":" + col1+ " " + row2+ ":" + col2;
//                    System.out.println("found = " + found);
                    foundMap.put(word, found);
                }
            }            
        }
    }
    public static void buildLeftDiagnal()
    {
        
        int rowIndex1 = grid.length - 2;
        int colIndex1 = 0;
        int rowIndex2 = grid.length - 1;
        int colIndex2 = 1;
        
        // col1 and row2 changes

        for (rowIndex1 = row - 2; rowIndex1 > 0 ; rowIndex1--) {
           
//            System.out.println("index1 = " + rowIndex1 + ":"+ colIndex1 + " " + rowIndex2+ ":" + colIndex2);
            
            buildDiagnalWord(rowIndex1, colIndex1, rowIndex2, colIndex2);
            ++colIndex2;         
            
        }
    }

    public static void buildRightDiagnal()
    {
        
        int rowIndex1 = 0;
        int colIndex1 = 0;
        int rowIndex2 = grid.length - 1;
        int colIndex2 = grid.length - 1;
        
        // col1 and row2 changes

        for (colIndex1 = 0; colIndex1 < grid.length  -1 ; colIndex1++) {
           
//            System.out.println("index1 = " + rowIndex1 + ":"+ colIndex1 + " " + rowIndex2+ ":" + colIndex2);
            
            buildDiagnalWord(rowIndex1, colIndex1, rowIndex2, colIndex2);
            --rowIndex2;         
            
        }
        
    
    }

    public static void  buildDiagnalWord(int row1, int col1, int limitRow2, int col2) 
    {
    
        StringBuilder stringB = new StringBuilder();
    
        for (int i = row1, j = col1; i <= limitRow2; i++,j++) {
            stringB.append(grid[i][j]);
        }
        
        DiagnalLine diagnalLine = new DiagnalLine(stringB.toString(), row1, col1);
        diagnalList.add(diagnalLine);
//        System.out.println("stringB = " + stringB);
    
        int revRow1 = row1 + stringB.length() -1;
        int revCol1 = col1 + stringB.length() -1;
        DiagnalLine revLine = new DiagnalLine(stringB.reverse().toString(), revRow1, revCol1);
        revDiagnalList.add(revLine);
//        System.out.println("revLine = " + revLine);

    }

    
    
/////////     end
    
    public static void buildDiagnal()
    {  
        buildLeftDiagnal();
        buildRightDiagnal();
    }  
    
    public static String reverseString(String text)
    {  
        StringBuilder sb = new StringBuilder(text);  
        sb.reverse();  
        return sb.toString();  
    }  
    
    public static void scanHorizon()
    {
        String  text            = "";
        String  reverse         = "";
        String  found           = "";
        int     currentColumn   = 0;
        boolean forwardMatch    = true;
        
        for (int curRow = 0; curRow < horizonText.size(); curRow++) 
        {
            text = horizonText.get(curRow);
            
            straightMatch(text, curRow, true);
        }
        for (int revRow = 0; revRow < horizonReverses.size(); revRow++) 
        {
            
            reverse = horizonReverses.get(revRow);
            
            straightMatch(reverse, revRow, false);
        }

    }

    
    public static void scanVertical()
    {
        String text = "";
        String reverse = "";
        
        for (int forCol = 0; forCol < verticalText.size(); forCol++) 
        {

            text = verticalText.get(forCol);            
            straightMatch(text, forCol, true);            
        }
        for (int revCol = 0; revCol < verticalReverses.size(); revCol++) {
            
            reverse = verticalReverses.get(revCol);
            straightMatch(reverse, revCol, false);
            
        }
        
    }    
        public static void straightMatch(String text, int index, boolean forwardMatch) {
                int begIndex = 0;
                int endIndex = 0;

        String found = "";
        for (int i = 0; i < dictionary.size(); i++) {
            String word = dictionary.get(i);
 
            if (text.contains(word))
            {
                if (forwardMatch)
                {
                    begIndex = text.indexOf(word);
                    endIndex = begIndex + word.length() - 1;

                    found = word + " "+ index+":"+begIndex+ " " +  index+":"+endIndex; 
                    foundMap.put(word, found);
//                    System.out.println("found = " + found);
                }
                else
                {
                    endIndex = text.indexOf(word);
                    begIndex = endIndex + word.length() - 1;
                
                    found = word + " "+ index+":"+begIndex+ " " +  index+":"+endIndex; 
//                    System.out.println("found = " + found);
                    foundMap.put(word, found);
                }
            }                    
        }
        
    }

    
    public static List<String> getLine(String filePath) 
    {
        
        
        List<String> arrayList = new ArrayList<>();
        
        try 
        {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            
            String line = "";
            while ((line = reader.readLine())!= null)
            {
                arrayList.add(line);
            }

        } 

        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        return arrayList;
    }
    
    public static int[] getDimension(String text) 
    {
        
        int [] pair = {0,0};
        String [] dimenssionPair = text.split("x");
        pair[0] = Integer.parseInt(dimenssionPair[0]);
        pair[1] = Integer.parseInt(dimenssionPair[1]);
        return pair;
    }
}
