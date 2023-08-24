package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

public class GameScreen extends BaseScreen
{
    private Paddle paddle;
    private Ball ball;

    private Brick baseBrick;
    private ArrayList<Brick> brickList;

    private Powerup basePowerup;
    private ArrayList<Powerup> powerupList;

    private ArrayList<BaseActor> removeList;

    // game world dimensions
    final int mapWidth = 800;
    final int mapHeight = 600;
    private int popped;
    private Label poppedLabel;

    public GameScreen(BaseGame g)
    {
        super(g);
    }

    public void create()
    {
        paddle = new Paddle();
        Texture paddleTex = new Texture(Gdx.files.internal("paddle.png"));
        paddleTex.setFilter( Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
        paddle.setTexture( paddleTex );
        mainStage.addActor(paddle);

        baseBrick = new Brick();
        Texture brickTex = new Texture(Gdx.files.internal("brick-gray.png"));
        baseBrick.setTexture( brickTex );
        baseBrick.setOriginCenter();

        brickList = new ArrayList<Brick>();

        ball = new Ball();
        Texture ballTex = new Texture(Gdx.files.internal("ball.png"));
        ball.storeAnimation( "default", ballTex );
        ball.setPosition( 400, 200 );
        ball.setVelocityAS( 30, 300 );
        ball.setAccelerationXY( 0, -10 );
        mainStage.addActor( ball );

        basePowerup = new Powerup();
        basePowerup.setVelocityXY(0, -100);
        basePowerup.storeAnimation("paddle-expand",
                new Texture(Gdx.files.internal("paddle-expand.png")) );
        basePowerup.storeAnimation("paddle-shrink",
                new Texture(Gdx.files.internal("paddle-shrink.png")) );
        basePowerup.setOriginCenter();

        powerupList = new ArrayList<Powerup>();

        Color[] colorArray = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE};

        for (int j = 0; j < 6; j++)
        {
            for (int i = 0; i < 10; i++)
            {
                Brick brick = baseBrick.clone();
                brick.setPosition( 8 + 80*i,  500 - (24 + 16)*j );
                brick.setColor( colorArray[j] );
                brickList.add( brick );
                brick.setParentList( brickList );
                mainStage.addActor( brick );
            }
        }

        removeList = new ArrayList<BaseActor>();
        BitmapFont font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle( font, Color.WHITE );
        popped = 0;
        poppedLabel = new Label( "Score:0", style );
        poppedLabel.setFontScale(2);
        poppedLabel.setPosition(20, 540);
        uiStage.addActor( poppedLabel );
    }

    public void update(float dt)
    {
        // adjust paddle position to horizontal mouse coordinate

        paddle.setPosition( Gdx.input.getX() - paddle.getWidth()/2, 32 );

        // bound paddle to screen

        if ( paddle.getX() < 0 )
            paddle.setX(0);

        if ( paddle.getX() + paddle.getWidth() > mapWidth )
            paddle.setX(mapWidth - paddle.getWidth());

        // bounce ball off screen edges

        if (ball.getX() < 0)
        {
            ball.setX(0);
            ball.multVelocityX(-1);
        }

        if (ball.getX() + ball.getWidth() > mapWidth)
        {
            ball.setX( mapWidth - ball.getWidth() );
            ball.multVelocityX(-1);
        }

        if (ball.getY() < 0)
        {
            ball.setY(0);
            ball.multVelocityY(-1);
        }

        if (ball.getY() + ball.getHeight() > mapHeight)
        {
            ball.setY( mapHeight - ball.getHeight() );
            ball.multVelocityY(-1);
        }

        // bounce ball off paddle

        if ( ball.overlaps(paddle, true) )
        {
           // ball.overlaps(paddle, true);
            //play boing sound
            float volume=0.8f;
            Music song = Gdx.audio.newMusic(
                    Gdx.files.internal("Water_Drop.ogg"));
            song.setVolume(volume);
            song.play();
        }

        removeList.clear();

        for (Brick br : brickList)
        {
            if ( ball.overlaps(br, true) ) // bounces off bricks
            {
                removeList.add(br);
                popped+=5;
                if (Math.random() < 0.20)
                {
                    Powerup pow = basePowerup.clone();
                    pow.randomize();
                    pow.moveToOrigin(br);

                    paddle.addAction( Actions.sizeBy(32,0, 0.5f) );
                    float volume=0.8f;
                    Music song = Gdx.audio.newMusic(
                            Gdx.files.internal("music.mp3"));
                    song.setVolume(volume);
                    song.play();


                    pow.setScale(0,0);
                    pow.addAction( Actions.scaleTo(1,1, 0.5f) );

                    powerupList.add(pow);
                    pow.setParentList(powerupList);
                    mainStage.addActor(pow);
                }
                poppedLabel.setText("Score:"+popped);
            }
        }

        for (Powerup pow : powerupList)
        {
            if ( pow.overlaps(paddle) )
            {
                String powName = pow.getAnimationName();
                if ( powName.equals("paddle-expand") && paddle.getWidth() < 256)
                {

                    popped+=5;
                    poppedLabel.setText("Score:"+popped);
                    paddle.addAction( Actions.sizeBy(32,0, 0.5f) );
                    float volume=0.8f;
                    Music song = Gdx.audio.newMusic(
                            Gdx.files.internal("song1.mp3"));
                    song.setVolume(volume);
                    song.play();
                }
                else if ( powName.equals("paddle-shrink") && paddle.getWidth() > 64)
                {

                    popped-=5;
                    poppedLabel.setText("Score:"+popped);
                    paddle.addAction( Actions.sizeBy(-32,0, 0.5f) );
                    float volume=0.8f;
                    Music song = Gdx.audio.newMusic(
                            Gdx.files.internal("song2.mp3"));
                    song.setVolume(volume);
                    song.play();
                }

                removeList.add(pow);
            }
        }

        for (BaseActor b : removeList)
        {
            b.destroy();
        }
    }

    // InputProcessor methods for handling discrete input
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.P)
            togglePaused();

        if (keycode == Input.Keys.R)
            game.setScreen( new GameScreen(game) );

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
