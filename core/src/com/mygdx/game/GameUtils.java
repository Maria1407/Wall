package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameUtils
{
    public static int LifeCount=0;
    public static Animation parseSpriteSheet(String fileName, int frameCols, int frameRows,
                                             float frameDuration, Animation.PlayMode mode)
    {
        Texture t = new Texture(Gdx.files.internal(fileName), true);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int frameWidth = t.getWidth() / frameCols;
        int frameHeight = t.getHeight() / frameRows;

        TextureRegion[][] temp = TextureRegion.split(t, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];

        int index = 0;
        for (int i = 0; i < frameRows; i++)
        {
            for (int j = 0; j < frameCols; j++)
            {
                frames[index] = temp[i][j];
                index++;
            }
        }

        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        return new Animation(frameDuration, framesArray, mode);
    }
    public static Animation parseImageFiles(String fileNamePrefix, String fileNameSuffix,
                                            int frameCount, float frameDuration, Animation.PlayMode mode)
    {
        TextureRegion[] frames = new TextureRegion[frameCount];

        for (int n = 0; n < frameCount; n++)
        {
            String fileName = fileNamePrefix + n + fileNameSuffix;
            Texture tex = new Texture(Gdx.files.internal(fileName));
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames[n] = new TextureRegion( tex );
        }

        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        return new Animation(frameDuration, framesArray, mode);
    }
}