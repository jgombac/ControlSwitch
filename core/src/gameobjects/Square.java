package gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.phgame.bopit.BopGame;

import java.util.Random;

public class Square {
    public World world;
    public Body body;
    float PPM = 100;


    public Square(World world){
        this.world = world;
        defineSquare();
    }

    public void defineSquare(){
        Random rnd = new Random();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bdef.position.set(rnd.nextFloat() * ((BopGame.WIDTH/PPM-.5f) - .5f) + .5f, 0);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);

        fdef.shape = shape;
        fdef.density = 10f;
        fdef.filter.groupIndex = -1;

        body.createFixture(fdef);

        float xforce = (body.getPosition().x > BopGame.WIDTH/2/PPM) ? -15 : 15;
        body.applyLinearImpulse(new Vector2(xforce, 110), body.getPosition(), true);
        body.applyTorque(0.8f, true);
        shape.dispose();
    }
}
