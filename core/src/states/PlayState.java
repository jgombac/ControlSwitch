package states;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.phgame.bopit.BopGame;


import java.lang.reflect.InvocationTargetException;

import bonuses.Bonus;
import bonuses.SlowBonus;
import bonuses.StopBonus;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import gameobjects.Enums;
import gameobjects.MyObject;
import gameobjects.Particle;
import levels.Level;


public class PlayState extends State implements ContactListener{

    private Box2DDebugRenderer b2dr;
    private World world;
    private World particleWorld;

    private RayHandler rayHandler;
    private Light light;

    public float PPM = 100;


    private OrthographicCamera cam;

    private Texture background;

    private Vector3 mouse = new Vector3();
    private MouseJoint mJoint;


    private Array<Body> tempBodies = new Array<Body>();
    private Array<Body> tempParticles = new Array<Body>();

    private Body hitbody;
    private Body circlebody;

    private Body particleGround;
    private Body ground;
    private Body right;
    private Body left;
    private Body up;

    private Level level;

    private long score;
    private BitmapFont scoreFont;
    private GlyphLayout glScore;

    private BitmapFont overFont;
    private GlyphLayout glLevel;
    private GlyphLayout glComplete;

    private GlyphLayout glIntro;

    private SlowBonus slowBonus;
    private ImageButton slowButton;

    private float overTimer = 0;
    private boolean over = false;

    private float introTimer = 0;
    private float introTime = 2.5f;

//    private StopBonus stopBonus;
//    private ImageButton stopButton;

    private Stage stage;

    Matrix4 normal = new Matrix4().setToOrtho2D(0, 0, BopGame.WIDTH, BopGame.HEIGHT);


    public PlayState(GameStateManager gsm, Level level) {
        super(gsm);
        Box2D.init();
        world = new World(new Vector2(0, -10), false);
        particleWorld = new World(new Vector2(0, 0), false);

        RayHandler.setGammaCorrection(true);
        rayHandler = new RayHandler(world);
        rayHandler.setBlur(true);
        rayHandler.setAmbientLight(1);

        b2dr = new Box2DDebugRenderer();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, BopGame.WIDTH / PPM, BopGame.HEIGHT / PPM);
        cam.update();

        stage = new Stage();

        background = new Texture("background.png");

        this.level = level;
        this.level.setWorld(world);

        createGround();
        initFont();
        initPointLight();
//        setBonuses();

        score = 0L;



        setInputProcessors();

        world.setContactListener(this);
        particleWorld.setContactListener(this);

    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        synchronized(this) {
            if (introTimer <= introTime)
                introTimer += dt;
            if (introTimer >= introTime) {
                if (over)
                    overTimer += dt;
                if (score >= level.getScoreTarget() && !over) {
                    level.itsOver();
                    over = true;
                    gameOver();

                }
                if (level.getBonus() != null && level.getBonus() instanceof SlowBonus && level.getBonus().getUse())
                    world.step(level.getBonus().getSlow(), 6, 2);

                else
                    world.step(1 / 60f, 6, 2);

                particleWorld.step(1 / 60f, 6, 2);

                for (Body b : tempBodies) {
                    b.applyTorque(2f, true);
                }
                for (Body b : tempParticles) {
                    b.applyTorque(2f, true);
                }
                for (Contact con : particleWorld.getContactList()) {
                    Body a = con.getFixtureA().getBody();
                    Body b = con.getFixtureB().getBody();

                    if (b.getUserData() != null && b.getUserData() instanceof Sprite) {
                        Sprite sprite = (Sprite) b.getUserData();
                        sprite.getTexture().dispose();
                        particleWorld.destroyBody(b);
                        break;
                    }
                }

                for (Contact con : world.getContactList()) {
                    Body a = con.getFixtureA().getBody();
                    Body b = con.getFixtureB().getBody();

                    if (b.getUserData() != null && ((MyObject) b.getUserData()).hit && a != ground) {
                        MyObject object = (MyObject) b.getUserData();

                        object.sprite.getTexture().dispose();
                        if (mJoint != null && mJoint.getBodyB() == b) {
                            world.destroyJoint(mJoint);
                            mJoint = null;
                        }

                        Vector2 pos = new Vector2();
                        if (a == right)
                            pos = new Vector2(cam.viewportWidth, b.getPosition().y);

                        if (a == left)
                            pos = new Vector2(0, b.getPosition().y);

                        if (a == up)
                            pos = new Vector2(b.getPosition().x, cam.viewportHeight);

                        for (int i = 0; i <= 8; i++) {
                            new Particle(particleWorld, pos);
                        }

                        if (b == hitbody) {
//                        light.remove();
//                        light.setPosition(-10, -10);
                            hitbody = null;
                        }
                        world.destroyBody(b);
                        score += 1;
                        break;
                    } else if (b.getUserData() != null && (((MyObject) b.getUserData()).en == Enums.SQUARE || ((MyObject) b.getUserData()).en == Enums.CIRCLE) && a == ground) {
                        gsm.pop();
                        gsm.push(new GameOverState(gsm, score));
                        break;
                    }
                }

                if (mJoint != null)
                    light.setPosition(mJoint.getBodyB().getPosition());
                if (mJoint == null)
                    light.setPosition(-10, -10);


                if (level.bonusAvailable && score >= level.bonusActivate) {
                    Bonus bonus = level.initBonus();
                    level.setBonus(bonus);
                    setBonus(bonus);
                }


                if (level.getBonus() != null && level.getBonus().getUse()) {
                    level.getBonus().update(dt);
                    level.update(dt / 2);
                }

                if (level.getBonus() == null || level.getBonus() != null && !level.getBonus().getUse())
                    level.update(dt);

                if (level.getBonus() != null && level.getBonus().getN() >= 1 && !level.getBonus().getUse()) {
                    slowButton.setDisabled(false);
                }


                glScore.setText(scoreFont, score + "");

                if (over && overTimer >= 3) {
                    BopGame.levels.levelUp();
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

            }
        }
    }

    private void gameOver(){
        world.getBodies(tempBodies);
        if (mJoint != null) {
            world.destroyJoint(mJoint);
            mJoint = null;
        }
        if (hitbody != null ) {
            hitbody = null;

        }
        for(Body b : tempBodies) {
            if (b.getUserData() != null) {
                MyObject object = (MyObject) b.getUserData();

                object.sprite.getTexture().dispose();

            }
            for(int i = 0; i <= 6; i++) {
                new Particle(particleWorld, b.getPosition());
            }
            world.destroyBody(b);
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        synchronized (this) {
            cam.update();
            sb.setProjectionMatrix(cam.combined);
            rayHandler.setCombinedMatrix(cam);
            rayHandler.update();
            sb.begin();
            sb.draw(background, 0, 0, cam.viewportWidth, cam.viewportHeight);
            sb.end();

            rayHandler.render();
            world.getBodies(tempBodies);
            particleWorld.getBodies(tempParticles);




            sb.begin();

            for (Body b : tempBodies) {
                if (b.getUserData() != null) {
                    MyObject object = (MyObject) b.getUserData();
                    Sprite sprite = object.sprite;
                    sprite.setPosition(b.getPosition().x - sprite.getWidth() / 2, b.getPosition().y - sprite.getHeight() / 2);
                    sprite.setRotation(MathUtils.radiansToDegrees * b.getAngle());
                    sprite.draw(sb);
                }
            }
            for (Body b : tempParticles) {
                if (b.getUserData() != null && b.getUserData() instanceof Sprite) {

                    Sprite sprite = (Sprite) b.getUserData();
                    sprite.setPosition(b.getPosition().x - sprite.getWidth() / 2, b.getPosition().y - sprite.getHeight() / 2);
                    sprite.setRotation(MathUtils.radiansToDegrees * b.getAngle());
                    sprite.draw(sb);
                }
            }
            sb.setProjectionMatrix(normal);

            scoreFont.draw(sb, glScore, (BopGame.WIDTH - glScore.width) / 2, BopGame.HEIGHT - glScore.height * 1.5f);
            if(introTimer <= introTime)
                overFont.draw(sb, glIntro, (BopGame.WIDTH - glIntro.width)/2, BopGame.HEIGHT /2);
            if(over) {
                overFont.draw(sb, glLevel, (BopGame.WIDTH - glLevel.width) / 2, BopGame.HEIGHT / 2 + glLevel.height*1.4f);
                overFont.draw(sb, glComplete, (BopGame.WIDTH - glComplete.width) / 2, BopGame.HEIGHT/2);
            }
            if(slowButton != null && level.getBonus() != null && !level.getBonusActive()) {
                slowButton.draw(sb, 1);
            }
            sb.end();



        }
    }

    private void initPointLight(){
        light = new PointLight(rayHandler, 16, new Color(0.96f, 0.96f, 0.86f, 0.8f), 2.5f, -10, -10);
    }

    private void createGround(){
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(cam.viewportWidth, 0f);
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(0, -2f);
        ground = world.createBody(groundDef);
        particleGround = particleWorld.createBody(groundDef);


        FixtureDef fdef = new FixtureDef();
        fdef.shape = groundShape;
        fdef.filter.groupIndex = -2;
        ground.createFixture(fdef);
        particleGround.createFixture(fdef);
        groundShape.dispose();

        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(0f, cam.viewportHeight + 2f);
        BodyDef rightDef = new BodyDef();
        rightDef.type = BodyDef.BodyType.StaticBody;
        rightDef.position.set(cam.viewportWidth+0.5f, 0);
        right = world.createBody(rightDef);

        FixtureDef rightFix = new FixtureDef();
        rightFix.shape = rightShape;
        rightFix.filter.groupIndex = -2;
        particleWorld.createBody(rightDef).createFixture(rightFix);
        right.createFixture(rightFix);

        BodyDef leftDef = new BodyDef();
        leftDef.type = BodyDef.BodyType.StaticBody;
        leftDef.position.set(-0.5f, 0);
        left = world.createBody(leftDef);

        FixtureDef leftFix = new FixtureDef();
        leftFix.shape = rightShape;
        leftFix.filter.groupIndex = -2;
        particleWorld.createBody(leftDef).createFixture(leftFix);
        left.createFixture(leftFix);

        rightShape.dispose();

        PolygonShape upShape = new PolygonShape();
        upShape.setAsBox(cam.viewportWidth + 1f, 0f);
        BodyDef upDef = new BodyDef();
        upDef.type = BodyDef.BodyType.StaticBody;
        upDef.position.set(-0.5f, cam.viewportHeight+0.5f);
        up = world.createBody(upDef);

        FixtureDef upFix = new FixtureDef();
        upFix.shape = upShape;
        upFix.filter.groupIndex = -2;
        particleWorld.createBody(upDef).createFixture(upFix);
        up.createFixture(upFix);

        upShape.dispose();
    }

    @Override
    public void dispose() {
        background.dispose();
        rayHandler.dispose();
        scoreFont.dispose();
        overFont.dispose();

        for(Body b : tempBodies){
            if(b.getUserData() != null){
                MyObject object = (MyObject) b.getUserData();
                Sprite sprite = object.sprite;
                sprite.getTexture().dispose();
            }
        }
        for(Body b : tempParticles){
            if(b.getUserData() != null && b.getUserData() instanceof Sprite){
                Sprite sprite = (Sprite) b.getUserData();
                sprite.setPosition(b.getPosition().x - sprite.getWidth()/2, b.getPosition().y - sprite.getHeight()/2);
                sprite.setRotation(MathUtils.radiansToDegrees * b.getAngle());
                sprite.getTexture().dispose();
            }
        }
        world.dispose();
        particleWorld.dispose();
    }


    private void initFont(){
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/VV2Nightclub.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter scoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParam.size = 50;
        scoreParam.shadowOffsetX = 2;
        scoreParam.shadowOffsetY = 1;
        scoreParam.shadowColor = new Color(0f, 0f, 0f, 0.3f);
        scoreFont = gen.generateFont(scoreParam);
        scoreFont.setColor(0.96f, 0.96f, 0.86f, 1f);

        FreeTypeFontGenerator.FreeTypeFontParameter overParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        overParam.size = 80;
        overParam.shadowOffsetX = 2;
        overParam.shadowOffsetY = 1;
        overParam.shadowColor = new Color(0f, 0f, 0f, 0.3f);
        overFont = gen.generateFont(overParam);
        overFont.setColor(0.96f, 0.96f, 0.86f, 1f);

        gen.dispose();

        glScore = new GlyphLayout();
        glScore.setText(scoreFont, score + "");

        glLevel = new GlyphLayout();
        glLevel.setText(overFont, "LEVEL");
        glComplete = new GlyphLayout();
        glComplete.setText(overFont, "COMPLETE");

        glIntro = new GlyphLayout();
        glIntro.setText(overFont, "Level " + level.ID);

    }

    private void setBonuses(){
        slowBonus = new SlowBonus(3);
        ImageButton.ImageButtonStyle slowStyle = new ImageButton.ImageButtonStyle();
        slowStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/pokal.png")));
        slowStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/pokal_a.png")));
        slowStyle.pressedOffsetX = 3f;
        slowStyle.pressedOffsetY = -3f;
        slowButton = new ImageButton(slowStyle);
        slowButton.setWidth(slowButton.getWidth());
        slowButton.setHeight(slowButton.getHeight());
        slowButton.invalidate();
        slowButton.setPosition(BopGame.WIDTH - slowButton.getWidth() * 1.5f, BopGame.HEIGHT - slowButton.getHeight() * 1.5f);
        stage.addActor(slowButton);
        slowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!slowButton.isDisabled() && !level.getBonusActive()) {
                    slowBonus.startBonus();
                    level.setBonusActive(true);
                    slowButton.setDisabled(true);
                }
            }
        });

//        stopBonus = new StopBonus(3);
//        ImageButton.ImageButtonStyle stopStyle = new ImageButton.ImageButtonStyle();
//        stopStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/fb.png")));
//        stopStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/fb_a.png")));
//        stopStyle.pressedOffsetX = 3f;
//        stopStyle.pressedOffsetY = -3f;
//        stopButton = new ImageButton(stopStyle);
//        stopButton.setWidth(stopButton.getWidth());
//        stopButton.setHeight(stopButton.getHeight());
//        stopButton.invalidate();
//        stopButton.setPosition(slowButton.getX(), slowButton.getY() - slowButton.getHeight()*1.2f);
//        stage.addActor(stopButton);
//        stopButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (!stopButton.isDisabled() && !level.getBonusActive()) {
//                    stopBonus.startBonus();
//                    level.setBonusActive(true);
//                    stopButton.setDisabled(true);
//                }
//            }
//        });
    }

    private void setBonus(Bonus bonus){
        if(bonus instanceof SlowBonus) {
            ImageButton.ImageButtonStyle slowStyle = new ImageButton.ImageButtonStyle();
            slowStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/pokal.png")));
            slowStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("buttons/pokal_a.png")));
            slowStyle.pressedOffsetX = 3f;
            slowStyle.pressedOffsetY = -3f;
            slowButton = new ImageButton(slowStyle);
            slowButton.setWidth(slowButton.getWidth()/2);
            slowButton.setHeight(slowButton.getHeight()/2);
            slowButton.invalidate();
            slowButton.setPosition(BopGame.WIDTH - slowButton.getWidth() * 1.5f, BopGame.HEIGHT - slowButton.getHeight() * 1.5f);
            stage.addActor(slowButton);
            slowButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (level.getBonus() != null && !slowButton.isDisabled() && !level.getBonusActive()) {
                        level.getBonus().startBonus();
                        level.setBonusActive(true);
                        slowButton.setDisabled(true);
                        System.out.println("ACTIVATING-------------------------------------------");
                    }
                }
            });
        }
    }




    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            hitbody = fixture.getBody();
            return true;
        }
    };

    QueryCallback circlecallback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            circlebody = fixture.getBody();
            return true;
        }
    };

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    Vector2 target = new Vector2();

    private void setInputProcessors(){
        InputProcessor gameplay = new InputProcessor() {
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
                if(pointer == 0) {
                    mouse.set(screenX / PPM, (BopGame.HEIGHT - screenY) / PPM, 0);
                    hitbody = null;
                    world.QueryAABB(callback, mouse.x - 0.3f, mouse.y - 0.3f, mouse.x + 0.3f, mouse.y + 0.3f);
                    if (mJoint == null && hitbody != null && hitbody.getUserData() != null && ((MyObject) hitbody.getUserData()).en == Enums.SQUARE) {
                        MouseJointDef def = new MouseJointDef();
                        def.bodyA = ground;
                        def.bodyB = hitbody;
                        ((MyObject) hitbody.getUserData()).hit = true;
                        def.collideConnected = true;
                        def.maxForce = 1000f * hitbody.getMass();
                        def.dampingRatio = 0f;
                        def.target.set(hitbody.getPosition());
                        mJoint = (MouseJoint) world.createJoint(def);
                        //light.attachToBody(hitbody);
                    } else if (mJoint == null && hitbody != null && hitbody.getUserData() != null && ((MyObject) hitbody.getUserData()).en == Enums.CIRCLE) {
                        MouseJointDef def = new MouseJointDef();
                        def.bodyA = ground;
                        def.bodyB = hitbody;
                        def.collideConnected = true;
                        def.maxForce = 0;
                        def.dampingRatio = 0f;
                        def.target.set(mouse.x, mouse.y);
                        mJoint = (MouseJoint) world.createJoint(def);
                        //light.attachToBody(hitbody);
                    }
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(pointer == 0) {
                    mouse.set(screenX / PPM, (BopGame.HEIGHT - screenY) / PPM, 0);
                    if (hitbody != null && mJoint != null) {
                        world.QueryAABB(circlecallback, mouse.x - 0.1f, mouse.y - 0.1f, mouse.x + 0.1f, mouse.y + 0.1f);
                        if (hitbody.getUserData() != null && hitbody == circlebody && ((MyObject) hitbody.getUserData()).en == Enums.CIRCLE) {
                            MyObject object = (MyObject) hitbody.getUserData();
                            object.sprite.getTexture().dispose();
                            world.destroyJoint(mJoint);
                            mJoint = null;
                            for(int i = 0; i <= 6; i++) {
                                new Particle(particleWorld, hitbody.getPosition());
                            }
                            world.destroyBody(hitbody);
//                            light.setPosition(-10, -10);
                            hitbody = null;
                            circlebody = null;
                            score += 1;
                        }
                    }
                    if (mJoint != null) {
                        world.destroyJoint(mJoint);
                        mJoint = null;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if(pointer == 0) {
                    if (mJoint != null) {
                        target.set(screenX / PPM, (BopGame.HEIGHT - screenY) / PPM);
                        if (hitbody.getUserData() != null && ((MyObject) hitbody.getUserData()).en == Enums.CIRCLE && mJoint.getTarget() != target){//mJoint.getTarget().dst(target) >= 5) {
                            world.destroyJoint(mJoint);
                            mJoint = null;
                        } else if (hitbody.getUserData() != null && ((MyObject) hitbody.getUserData()).en == Enums.SQUARE)
                            mJoint.setTarget(target);
                    }
                }
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gameplay);

        Gdx.input.setInputProcessor(multiplexer);
    }


}
