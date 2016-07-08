package states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.phgame.bopit.BopGame;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import levels.Level;

public class LevelSelectState extends State implements InputProcessor {

    private Stage stage;
    private Texture background;
    private OrthographicCamera cam;

    private BitmapFont font;

    private List<TextButton> buttons;
    private ImageButton menuButton;

    public LevelSelectState(GameStateManager gsm){
        super(gsm);

        background = new Texture("background.png");

        cam = new OrthographicCamera();
        cam.setToOrtho(false, BopGame.WIDTH, BopGame.HEIGHT);
        cam.update();
        stage = new Stage();

        buttons = new ArrayList<TextButton>();

        initFont();
        setLevels();

        Gdx.input.setInputProcessor(stage);


    }


    @Override
    protected void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);

        sb.begin();
        sb.draw(background, 0, 0, cam.viewportWidth, cam.viewportHeight);

        menuButton.draw(sb, 1);

        for(TextButton btn : buttons){
            btn.draw(sb, 1);
        }


        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();

    }

    private void setLevels() {

        for(int i : BopGame.levels.getKeys()) {
            final int xx = i;
            TextButton.TextButtonStyle levelStyle = new TextButton.TextButtonStyle();
            levelStyle.font = font;
            levelStyle.fontColor = new Color(0.96f, 0.96f, 0.86f, 1f);
            levelStyle.downFontColor = new Color(0.96f, 0.96f, 0.86f, 0.4f);
            TextButton button = new TextButton(i + "", levelStyle);
            button.setWidth(cam.viewportWidth / 3);
            button.setHeight(cam.viewportHeight / 5);
            button.getLabel().setAlignment(Align.center);
            button.invalidate();
            if(buttons.size() == 0)
                button.setPosition(0, cam.viewportHeight - button.getHeight()*2);
            else if(buttons.size() % 3 != 0)
                button.setPosition(buttons.get(buttons.size() - 1).getX() + button.getWidth(), buttons.get(buttons.size() - 1).getY());
            else
                button.setPosition(0, buttons.get(buttons.size() - 1).getY() - button.getHeight());
            stage.addActor(button);
            buttons.add(button);

            button.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent e, float x, float y) {
                    gsm.pop();
                    try {
                        gsm.push(new PlayState(gsm, (Level) BopGame.levels.getLevel(xx).getDeclaredConstructor().newInstance()));
                    } catch (InstantiationException e1) {
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }


        ImageButton.ImageButtonStyle menuStyle = new ImageButton.ImageButtonStyle();
        menuStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/srcek.png")));
        menuStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/srcek_a.png")));
        menuStyle.pressedOffsetX = 3f;
        menuStyle.pressedOffsetY = -3f;
        menuButton = new ImageButton(menuStyle);
        menuButton.setSize(menuButton.getWidth(), menuButton.getHeight());
        menuButton.invalidate();
        menuButton.setPosition((cam.viewportWidth - menuButton.getWidth())/2, cam.viewportHeight-menuButton.getHeight()*1.5f);
        stage.addActor(menuButton);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.pop();
                gsm.push(new MainMenuState(gsm));
            }
        });
    }

    private void initFont(){
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/VV2Nightclub.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParam.size = 100;
        fontParam.shadowOffsetX = 4;
        fontParam.shadowOffsetY = 3;
        fontParam.shadowColor = new Color(0f, 0f, 0f, 0.3f);

        font = gen.generateFont(fontParam);
        font.setColor(0.96f, 0.96f, 0.86f, 1f);

        gen.dispose();
    }

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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    protected void handleInput() {

    }

}
