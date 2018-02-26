/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.uab.cephis.util;

/**
 * 
 * @author dcr
 */
public class PerformanceLap {

    long t0, diff;
    
    /**
     *  Implicit start in constructor
     */
    public PerformanceLap()
    {
        start();
    }
    
    public void start()
    {
        t0 = System.nanoTime();
    }
    
    public void stop()
    {
        diff = System.nanoTime() - t0;
    }
    
    public double ellapsedSeconds()
    {
        if (diff == 0)
            stop();
        
        return ((double)diff)/ 1E9;
    }
}