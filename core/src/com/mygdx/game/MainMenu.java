package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenu extends BaseScreen{
    public MainMenu(BaseGame g) {
        super(g);
    }

    @Override
    public void create() {
        Texture waterTex = new Texture(Gdx.files.internal("space.png"), true);
        waterTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        game.skin.add( "waterTex", waterTex );
        uiTable.background( game.skin.getDrawable("waterTex") );

        TextButton.TextButtonStyle uiTextButtonStyle = new TextButton.TextButtonStyle();
        BitmapFont uiFont = new
                BitmapFont(Gdx.files.internal("cooper.fnt"));
        uiFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        game.skin.add("uiFont", uiFont);
        uiTextButtonStyle.font=uiFont;
        uiTextButtonStyle.fontColor = Color.NAVY;
        Texture upTex = new
                Texture(Gdx.files.internal("ninepatch-1.png"));
        game.skin.add("buttonUp", new NinePatch(upTex, 26,26,16,20));
        uiTextButtonStyle.up = game.skin.getDrawable("buttonUp");
        Texture overTex = new
                Texture(Gdx.files.internal("ninepatch-2.png"));
        game.skin.add("buttonOver", new NinePatch(overTex, 26,26,16,20)
        );
        uiTextButtonStyle.over = game.skin.getDrawable("buttonOver");
        uiTextButtonStyle.overFontColor = Color.BLUE;
        Texture downTex = new
                Texture(Gdx.files.internal("ninepatch-3.png"));
        game.skin.add("buttonDown", new NinePatch(downTex, 26,26,16,20)
        );
        uiTextButtonStyle.down = game.skin.getDrawable("buttonDown");
        uiTextButtonStyle.downFontColor = Color.BLUE;
        game.skin.add("uiTextButtonStyle", uiTextButtonStyle);

        TextButton startButton = new TextButton("Start", game.skin,"uiTextButtonStyle");
        startButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                    {  return true;  }  // continue processing?

                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        game.setScreen( new LevelOne(game) );
                    }
                });
        TextButton quitButton = new TextButton("Quit", game.skin,"uiTextButtonStyle");
        quitButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                    {  return true;  }  // continue processing?

                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        Gdx.app.exit();
                    }
                });
        float w = startButton.getWidth();
        uiTable.row();
        uiTable.add(startButton);
        uiTable.add(quitButton).width(w);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
