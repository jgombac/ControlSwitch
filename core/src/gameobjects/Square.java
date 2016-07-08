package gameobjects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.phgame.bopit.BopGame;

import java.util.Random;

public class Square extends GameObject{
    public World world;
    public Body body;
    float PPM = 100;

    private Sprite sprite;


    public Square(World world, float pos, boolean right){
        super(Enums.SQUARE);
        this.world = world;
        positionalSquare(pos, right);

        sprite = new Sprite(new Texture("objects/kvadratC.png"));

        sprite.setSize(0.8f * 2, 0.8f * 2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        MyObject object = new MyObject(sprite, getType());
        body.setUserData(object);

        body.applyLinearImpulse(new Vector2(0, (float) Math.sqrt(2 * 10 * ((11.84 / 4) * 3)) * body.getMass()), body.getPosition(), true);
    }


    public Square(World world){
        super(Enums.SQUARE);
        this.world = world;
        defineSquare();
        sprite = new Sprite(new Texture("objects/kvadratC.png"));
        sprite.setSize(0.8f * 2, 0.8f * 2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        MyObject object = new MyObject(sprite, getType());
        body.setUserData(object);


    }

    private void positionalSquare(float pos, boolean right){
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bdef.position.set((right) ? new Vector2(pos - 0.8f, 0) : new Vector2(pos + 0.8f, 0));
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.8f, 0.8f);

        fdef.shape = shape;
        fdef.density = 3f;
        fdef.restitution = 0.7f;
        fdef.filter.groupIndex = -1;

        body.createFixture(fdef);



        body.applyTorque(0.8f, true);
        shape.dispose();
    }





    public void defineSquare(){
        Random rnd = new Random();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bdef.position.set(rnd.nextFloat() * ((BopGame.WIDTH/PPM-.8f) - .8f) + .8f, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.8f, 0.8f);

        fdef.shape = shape;
        fdef.density = 3f;
        fdef.restitution = 0.7f;
        fdef.filter.groupIndex = -1;

        body.createFixture(fdef);

        float xforce = (body.getPosition().x > BopGame.WIDTH/2/PPM) ? -15 : 15;
        body.applyLinearImpulse(new Vector2(xforce, (float) Math.sqrt(2*10*((11.84/4)*3))*body.getMass()), body.getPosition(), true);
        body.applyTorque(0.8f, true);
        shape.dispose();
    }

    @Override
    public Body getBody(){
        return body;
    }

    @Override
    public Enums getType() {
        return super.getType();
    }
}
