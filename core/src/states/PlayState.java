package states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.phgame.bopit.BopGame;

import java.util.Random;

import gameobjects.Circle;
import gameobjects.Square;
import gameobjects.Triangle;

public class PlayState extends State{

    private Box2DDebugRenderer b2dr;
    private World world;

    public float PPM = 100;

    private float timer = 0;
    private Random rnd = new Random();
    private float spawntime;

    private OrthographicCamera cam;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        Box2D.init();
        world = new World(new Vector2(0, -10), false);
        b2dr = new Box2DDebugRenderer();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, BopGame.WIDTH/PPM, BopGame.HEIGHT/PPM);
        cam.update();


        spawntime = rnd.nextFloat() * (1.5f - 0.5f) + 0.5f;

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            //circle.body.applyLinearImpulse(0, 20f, circle.body.getPosition().x, circle.body.getPosition().y, true);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(1/60f, 6, 2);

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(Body b : bodies){
            //b.applyTorque(0.9f, true);
            if(b.getPosition().y < -cam.viewportHeight/2){
                world.destroyBody(b);
            }
        }

        timer += dt;
        if(timer > spawntime){
            switch(rnd.nextInt(3)){
                case 0:
                    new Circle(world);break;
                case 1:
                    new Square(world);break;
                case 2:
                    new Triangle(world);break;
            }
            timer = 0;
            spawntime = rnd.nextFloat() * (1.5f - 0.5f) + 0.5f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        b2dr.render(world, cam.combined);
    }

    @Override
    public void dispose() {

    }
}
