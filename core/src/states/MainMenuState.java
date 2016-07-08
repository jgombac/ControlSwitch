package states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.phgame.bopit.BopGame;

import java.lang.reflect.InvocationTargetException;

import levels.Level;


public class MainMenuState extends State implements InputProcessor{



    private Stage stage;

    private OrthographicCamera cam;

    private Texture background;
    private Texture title;

    private BitmapFont gameName;
    private BitmapFont playFont;
    private GlyphLayout glControl;
    private GlyphLayout glPlay;
    private GlyphLayout glThis;

    private ImageButton highscores;
    private ImageButton options;
    private TextButton play;

    private ImageButton star;
    private ImageButton heart;
    private ImageButton fb;
    private boolean inputUnlock = false;





    public MainMenuState(GameStateManager gsm) {
        super(gsm);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, BopGame.WIDTH, BopGame.HEIGHT);
        cam.update();

        stage = new Stage();

        background = new Texture("background.png");
        title = new Texture("title.png");

        initFonts();
        setButtons();

//        Gdx.input.setInputProcessor(stage);

    }

    @Override
    protected void handleInput() {}

    @Override
    public void update(float dt) {
        if(!inputUnlock){
            Gdx.input.setInputProcessor(stage);
            inputUnlock = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.enableBlending();
        sb.begin();
        sb.draw(background, 0, 0, cam.viewportWidth, cam.viewportHeight);
        //sb.draw(title, (cam.viewportWidth - title.getWidth()) / 2, (cam.viewportHeight / 4) * 3);
        gameName.draw(sb, glControl, (cam.viewportWidth - glControl.width) / 2, (cam.viewportHeight / 6) * 5);
        gameName.draw(sb, glThis, (cam.viewportWidth - glThis.width) / 2, (cam.viewportHeight / 6)*5 - glControl.height*1.5f);

        //playFont.draw(sb, glPlay, (cam.viewportWidth - glPlay.width)/2, (cam.viewportHeight/2));
        play.draw(sb, 1);
        highscores.draw(sb, 1);
        options.draw(sb, 1);
        star.draw(sb, 1);
        heart.draw(sb, 1);
        //fb.draw(sb, 1);
        sb.end();
//        if(!inputUnlock){
//            Gdx.input.setInputProcessor(stage);
//            inputUnlock = true;
//        }
    }

    @Override
    public void dispose() {
        playFont.dispose();
        background.dispose();
        title.dispose();
        gameName.dispose();
    }

    private void initFonts(){
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/VV2Nightclub.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter nameParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        nameParam.size = 120;
        nameParam.shadowOffsetX = 4;
        nameParam.shadowOffsetY = 3;
        nameParam.shadowColor = new Color(0f, 0f, 0f, 0.3f);
        //TODO correct color (245, 245, 220, 1)
        gameName = gen.generateFont(nameParam);
        gameName.setColor(0.96f, 0.96f, 0.86f, 1f);


        glControl = new GlyphLayout();
        glControl.setText(gameName, "CONTROL");
        glThis = new GlyphLayout();
        glThis.setText(gameName, "THIS");

        FreeTypeFontGenerator.FreeTypeFontParameter playParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        playParam.size = 40;
        playParam.color = new Color(245, 245, 220, 1);

        playFont = gen.generateFont(playParam);
        glPlay = new GlyphLayout();
        glPlay.setText(playFont, "Tap to Play!");
        gen.dispose();
    }

    private void setButtons(){
        TextButton.TextButtonStyle playStyle = new TextButton.TextButtonStyle();
        playStyle.font = playFont;
        playStyle.fontColor = new Color(0.96f, 0.96f, 0.86f, 1f);
        playStyle.downFontColor = new Color(0.96f, 0.96f, 0.86f, 0.4f);
        play = new TextButton("Tap to Play", playStyle);
        play.setWidth(cam.viewportWidth);
        play.setHeight(cam.viewportHeight / 2);
        play.getLabel().setAlignment(Align.bottom);
        play.getLabelCell().padBottom(play.getHeight() / 3);
        play.invalidate();
        play.setPosition(cam.viewportWidth / 2 - play.getWidth() / 2, (cam.viewportHeight / 3));
        stage.addActor(play);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.pop();
                try {
                    gsm.push(new PlayState(gsm, (Level) BopGame.levels.getCurrent().getDeclaredConstructor().newInstance()));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton.ImageButtonStyle highscoreStyle = new ImageButton.ImageButtonStyle();
        highscoreStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/pokal.png")));
        highscoreStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/pokal_a.png")));
        highscoreStyle.pressedOffsetX = 3f;
        highscoreStyle.pressedOffsetY = -3f;
        highscores = new ImageButton(highscoreStyle);
        highscores.setWidth(highscores.getWidth() + 50);
        highscores.setHeight(highscores.getHeight() + 50);
        highscores.invalidate();
        highscores.setPosition(0, cam.viewportHeight / 10);
        stage.addActor(highscores);
        highscores.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("HIGH SCORES");
            }
        });

        ImageButton.ImageButtonStyle optionsStyle = new ImageButton.ImageButtonStyle();
        optionsStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/zobniki.png")));
        optionsStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/zobniki_a.png")));
        optionsStyle.pressedOffsetX = 3f;
        optionsStyle.pressedOffsetY = -3f;
        options = new ImageButton(optionsStyle);
        options.setWidth(options.getWidth() + 50);
        options.setHeight(options.getHeight() + 50);
        options.invalidate();
        options.setPosition(highscores.getX() + highscores.getWidth(), cam.viewportHeight / 10);
        stage.addActor(options);
        options.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("OPTIONS");
            }
        });
        ImageButton.ImageButtonStyle starStyle = new ImageButton.ImageButtonStyle();
        starStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/zvezda.png")));
        starStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/zvezda_a.png")));
        starStyle.pressedOffsetX = 3f;
        starStyle.pressedOffsetY = -3f;
        star = new ImageButton(starStyle);
        star.setWidth(star.getWidth() + 50);
        star.setHeight(star.getHeight() + 50);
        star.invalidate();
        star.setPosition(options.getX() + options.getWidth(), cam.viewportHeight / 10);
        stage.addActor(star);
        star.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.pop();
                gsm.push(new LevelSelectState(gsm));
            }
        });

        ImageButton.ImageButtonStyle heartStyle = new ImageButton.ImageButtonStyle();
        heartStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/srcek.png")));
        heartStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/srcek_a.png")));
        heartStyle.pressedOffsetX = 3f;
        heartStyle.pressedOffsetY = -3f;
        heart = new ImageButton(heartStyle);
        heart.setWidth(heart.getWidth() + 50);
        heart.setHeight(heart.getHeight() + 50);
        heart.invalidate();
        heart.setPosition(star.getX() + star.getWidth(), cam.viewportHeight / 10);
        stage.addActor(heart);
        heart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("HEART");
            }
        });

        ImageButton.ImageButtonStyle fbStyle = new ImageButton.ImageButtonStyle();
        fbStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/fb.png")));
        fbStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/fb_a.png")));
        fbStyle.pressedOffsetX = 3f;
        fbStyle.pressedOffsetY = -3f;
        fb = new ImageButton(fbStyle);
        fb.setWidth(fb.getWidth() + 44);
        fb.setHeight(fb.getHeight() +44);
        fb.invalidate();
        fb.setPosition(heart.getX()+heart.getWidth(), cam.viewportHeight / 10);
        stage.addActor(fb);
        fb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("FB");
            }
        });



    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    //Garbage methods
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
