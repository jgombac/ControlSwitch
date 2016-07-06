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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.phgame.bopit.BopGame;

import java.lang.reflect.InvocationTargetException;

import gameobjects.MyObject;
import levels.Level;

public class GameOverState extends State implements InputProcessor{



    private OrthographicCamera cam;
    private Stage stage;
    private Texture background;

    private BitmapFont scoreFont;
    private GlyphLayout glScore;
    private BitmapFont message;


    private ImageButton replayButton;
    private ImageButton menuButton;

    private long score;

    public GameOverState(GameStateManager gsm, long score){
        super(gsm);

        this.score = score;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, BopGame.WIDTH, BopGame.HEIGHT);
        cam.update();

        background = new Texture("background.png");

        stage = new Stage();

        initFont();
        createButtons();

        Gdx.input.setInputProcessor(stage);

    }



    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.enableBlending();
        sb.begin();
        sb.draw(background, 0, 0, BopGame.WIDTH, BopGame.HEIGHT);
        scoreFont.draw(sb, glScore, (cam.viewportWidth - glScore.width)/2, cam.viewportHeight - glScore.height*1.5f);
        message.draw(sb, "Unfortunately you let an object pass. Please try again.", (cam.viewportWidth - (cam.viewportWidth/6)*5) / 2, (cam.viewportHeight/5)*4, (cam.viewportWidth/6)*5, 1, true);
        replayButton.draw(sb, 1);
        menuButton.draw(sb, 1);

        sb.end();

    }

    @Override
    public void dispose() {
        background.dispose();
        message.dispose();
    }

    private void createButtons(){
        ImageButton.ImageButtonStyle replayStyle = new ImageButton.ImageButtonStyle();
        replayStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/replay.png")));
        replayStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/replay_a.png")));
        replayStyle.pressedOffsetX = 3f;
        replayStyle.pressedOffsetY = -3f;
        replayButton = new ImageButton(replayStyle);
        replayButton.setSize(replayButton.getWidth()*2, replayButton.getHeight()*2);
        replayButton.setPosition(cam.viewportWidth/2  - replayButton.getWidth()/2, cam.viewportHeight/2 - replayButton.getHeight()/2);
        stage.addActor(replayButton);
        replayButton.addListener(new ClickListener() {
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


        ImageButton.ImageButtonStyle menuStyle = new ImageButton.ImageButtonStyle();
        menuStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/srcek.png")));
        menuStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/srcek_a.png")));
        menuStyle.pressedOffsetX = 3f;
        menuStyle.pressedOffsetY = -3f;
        menuButton = new ImageButton(menuStyle);
        menuButton.setSize(menuButton.getWidth()*2, menuButton.getHeight()*2);
        menuButton.setPosition(cam.viewportWidth/2 - menuButton.getWidth()/2, cam.viewportHeight/10);
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
        FreeTypeFontGenerator.FreeTypeFontParameter messageParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        messageParam.size = 40;
        messageParam.shadowOffsetX = 2;
        messageParam.shadowOffsetY = 1;
        messageParam.shadowColor = new Color(0f, 0f, 0f, 0.3f);
        message = gen.generateFont(messageParam);
        message.setColor(0.96f, 0.96f, 0.86f, 1f);


        FreeTypeFontGenerator.FreeTypeFontParameter scoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParam.size = 70;
        scoreParam.shadowOffsetX = 2;
        scoreParam.shadowOffsetY = 1;
        scoreParam.shadowColor = new Color(0f, 0f, 0f, 0.3f);
        scoreFont = gen.generateFont(scoreParam);
        scoreFont.setColor(0.96f, 0.96f, 0.86f, 1f);

        glScore = new GlyphLayout();
        glScore.setText(scoreFont, score+"");

        gen.dispose();


    }

    //INPUTLISTENER ---------------------------------------------------

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
        return true;
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
