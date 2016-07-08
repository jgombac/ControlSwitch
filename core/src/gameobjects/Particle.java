package gameobjects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class Particle{
    private Body body;
    private Random rnd = new Random();

    public Particle(World world, Vector2 pos){

        defineParticle(world, pos);
    }

    private void defineParticle(World world, Vector2 pos){


        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.angularVelocity = 5f;


        body = world.createBody(bdef);
        Sprite sprite = null;
        try {
            switch (rnd.nextInt(3) + 1) {
                case 1:
                    sprite = new Sprite(new Texture("objects/trikotnik1.png"));
                    break;
                case 2:
                    sprite = new Sprite(new Texture("objects/trikotnik2.png"));
                    break;
                case 3:
                    sprite = new Sprite(new Texture("objects/trikotnik3.png"));
                    break;
            }

            sprite.setSize(0.4f, 0.4f);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        }catch(Exception e){
            e.printStackTrace();
        }

        int[] multi = new int[]{-1, 1};

        body.setLinearVelocity((rnd.nextFloat() * 5+1) * multi[rnd.nextInt(2)], (rnd.nextFloat() * 5+1)*multi[rnd.nextInt(2)]);
        body.setUserData(sprite);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f, 0.2f);

        FixtureDef fdef = new FixtureDef();

        fdef.shape = shape;
        fdef.restitution = 0.05f;
        fdef.density = 0f;
        fdef.filter.groupIndex = -1;

        body.createFixture(fdef);

        shape.dispose();
    }

    public Body getBody(){
        return body;
    }
}
