package levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

import bonuses.Bonus;
import bonuses.SlowBonus;
import gameobjects.Circle;


public class Level01 extends Level{



    private static final float max = 1.5f;
    private static final float min = 0.5f;

    private static final int scoreTarget = 10;





    public Level01(){
        super(1, max, min, true, 1, scoreTarget);
    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }


    @Override
    synchronized void spawnObject() {

        new Circle(super.world);
    }

    @Override
    public Bonus initBonus() {
        return new SlowBonus();
    }
}
