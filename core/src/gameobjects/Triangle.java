package gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.phgame.bopit.BopGame;

import java.util.Random;

public class Triangle {
    public World world;
    public Body body;
    float PPM = 100;


    public Triangle(World world){
        this.world = world;
        defineTriangle();
    }

    public void defineTriangle(){
        Random rnd = new Random();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bdef.position.set(rnd.nextFloat() * ((BopGame.WIDTH / PPM - .5f) - .5f) + .5f, 0);
        System.out.println(bdef.position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        body = world.createBody(bdef);


        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[]{new Vector2(0f, 0f), new Vector2(1f, 0f), new Vector2(0.5f, 0.85f)};
        shape.set(vertices);


        fdef.shape = shape;
        fdef.density = 20f;
        fdef.friction = 0f;
        fdef.filter.groupIndex = -1;



        body.createFixture(fdef);

        float xforce = (body.getPosition().x > BopGame.WIDTH/2/PPM) ? -15 : 15;
        body.applyLinearImpulse(new Vector2(xforce, 110), body.getPosition(), true);

        shape.dispose();
    }
}
