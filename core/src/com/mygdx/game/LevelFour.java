package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;

public class LevelFour extends BaseScreen
{
    private Paddle paddle;
    private Ball ball;

    private Brick baseBrick;
    private ArrayList<Brick> brickList;

    private Powerup basePowerup;
    private ArrayList<Powerup> powerupList;

    private ArrayList<BaseActor> removeList;
    final int mapWidth = 800;
    final int mapHeight = 600;
    private int popped;
    private Label poppedLabel;
    private Label poppedLabel1;
    private Label.LabelStyle style;

    public LevelFour(BaseGame g)
    {
        super(g);
    }

    public void create()
    {
        GameUtils.LifeCount=3;
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
            Color[] colorArray = {Color.RED, Color.SCARLET, Color.CORAL, Color.ORANGE, Color.GOLD, Color.YELLOW, Color.LIME, Color.OLIVE};

        for (int j = 0; j < 8; j++)

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

        removeList = new ArrayList<BaseActor>();
        font = new BitmapFont();
        style = new Label.LabelStyle(font, Color.WHITE);
        poppedLabel1 = new Label("Life:"+GameUtils.LifeCount, style);
        poppedLabel1.setFontScale(2);
        poppedLabel1.setPosition(500, 540);
        uiStage.addActor( poppedLabel1 );

        float volume=0.8f;
        Music song = Gdx.audio.newMusic(
                Gdx.files.internal("life.mp3"));
        song.setVolume(volume);
        song.play();
    }

    public void update(float dt)
    {


        paddle.setPosition( Gdx.input.getX() - paddle.getWidth()/2, 32 );



        if ( paddle.getX() < 0 ) {

            paddle.setX(0);
        }
        if ( paddle.getX() + paddle.getWidth() > mapWidth )
            paddle.setX(mapWidth - paddle.getWidth());



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
            if(GameUtils.LifeCount>0)GameUtils.LifeCount--;
            ball.setY(0);
            ball.multVelocityY(-1);
            poppedLabel1.setText("Life:"+GameUtils.LifeCount);
            if(GameUtils.LifeCount==0)
            {
                //напичать Игра закончена
                ball.multVelocityX(0);
                ball.multVelocityY(0);
                TextButton startButton = new TextButton("Game over", game.skin,"uiTextButtonStyle");
                startButton.addListener(
                        new InputListener()
                        {
                            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                            {  return true;  }

                            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                            {
                                game.setScreen(new MainMenu(game));
                            }
                        });
                startButton.setPosition(250,190);
                uiStage.addActor( startButton );


                float volume=0.8f;
                Music song = Gdx.audio.newMusic(
                        Gdx.files.internal("gameover.mp3"));
                song.setVolume(volume);
                song.play();
            }
        }

        if (ball.getY() + ball.getHeight() > mapHeight)
        {
            ball.setY( mapHeight - ball.getHeight() );
            ball.multVelocityY(-1);
        }



        if ( ball.overlaps(paddle, true) )
        {
            ball.overlaps(paddle, true);

            float volume=0.8f;
            Music song = Gdx.audio.newMusic(
                    Gdx.files.internal("Water_Drop.ogg"));
            song.setVolume(volume);
            song.play();
        }

        removeList.clear();

        for (Brick br : brickList)
        {
            if ( ball.overlaps(br, true) )
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
                            Gdx.files.internal("conflict.mp3"));
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
        if(brickList.isEmpty())
        {
            //подумай вывести надпись "Победа"

            {
                ball.multVelocityX(0);
                ball.multVelocityY(0);
                TextButton startButton = new TextButton("You win!!! Level 5", game.skin, "uiTextButtonStyle");
                startButton.addListener(
                        new InputListener()
                        {
                            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                            {  return true;  }
                            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                            {
                                game.setScreen(new LevelFive(game));
                            }
                        });
                startButton.setPosition(250,190);
                uiStage.addActor( startButton );

                float volume=0.8f;
                Music song = Gdx.audio.newMusic(
                        Gdx.files.internal("win.mp3"));
                song.setVolume(volume);
                song.play();
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
                            Gdx.files.internal("plus.mp3"));
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
                            Gdx.files.internal("minus.mp3"));
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

    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.P)
            togglePaused();

        if (keycode == Input.Keys.R)
            game.setScreen( new LevelOne(game) );

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}