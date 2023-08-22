package com.mygdx.game;

public class RectangleDestroyerGame extends BaseGame
{
    public void create()
    {
        // initialize resources common to multiple screens

        // load game screen
        MainMenu gs = new MainMenu(this);
        setScreen( gs );
    }
}