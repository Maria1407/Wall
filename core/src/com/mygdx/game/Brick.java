package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Brick extends BaseActor
{
    public Brick()
    {  super();  }

    public Rectangle getRectangle()
    {  return new Rectangle( getX(), getY(), getWidth(), getHeight() );  }

    public Brick clone()
    {
        Brick newbie = new Brick();
        newbie.copy( this );
        return newbie;
    }

    public void destroy()
    {
        addAction( Actions.sequence( Actions.fadeOut(0.5f), Actions.removeActor() ) );

        if (parentList != null)
            parentList.remove(this);
    }
}