/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alphabet;

/**
 *
 * @author asidaya
 */
public class DiagnalLine 
{

    public DiagnalLine(String text, int rowIndex, int colIndex) {
        this.text = text;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public String getText() {
        return text;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }
    private String text;
    private int rowIndex;
    private int colIndex;
    
    public String toString() {
        return text + " " +rowIndex+ ":" + colIndex;
    }
    
}
