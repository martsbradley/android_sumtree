package com.mbradley.sumtree.model;


public interface MotionHandler
{

    void actionDown(int aX, int aY, int aUnscaledX, int aUnscaledY);


    void actionMove(int aX, int aY);
}
