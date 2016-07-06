package levels;

import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import bonuses.Bonus;
import bonuses.SlowBonus;
import gameobjects.Circle;
import gameobjects.Square;

public class Level02 extends Level{



    private static final float max = 1.5f;
    private static final float min = 0.5f;

    private static final int scoreTarget = 10;


    public Level02(){
        super(2, min, max, true, 2, scoreTarget);

    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    void spawnObject() {
        if(super.world != null)
            new Square(super.world);
    }

    @Override
    public Bonus initBonus() {
        return new SlowBonus(2);
    }
}
