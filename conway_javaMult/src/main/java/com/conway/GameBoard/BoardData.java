package com.conway.GameBoard;

import java.util.ArrayList;

public class BoardData {
    
    public int TICK_RATE; // ms per tick (50Hz)
    public ArrayList<Integer> frameCountList;
    public ArrayList<Long> nanoTimeList;
    public long lastFpsTime = 0;
    public int frameCount = 0;
    public long startNano; 

    public long startMili; 

}
