/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package srfont;
/**
 *
 * @author Marci
 */
public class Metric {
    private int u, v, width, height;
    private char c;
    public Metric(char c, int u, int v, int width){
        this.c = c;
        this.u = u;
        this.v = v;
        this.width = width;
    }
    
    @Override
    public String toString(){
        return String.format("%d, %d, %d", u, v, width);
    }
    
    public char getChar(){
        return c;
    }
}
