package levels;


import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import bonuses.Bonus;
import gameobjects.Circle;
import gameobjects.Square;

public class Level03 extends Level{



    private Random rnd;

    private static final float max = 1.5f;
    private static final float min = 0.5f;

    private static final int scoreTarget = 16;

    public Level03(){
        super(3, min, max, scoreTarget);

        rnd = new Random();

    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    void spawnObject() {
        if(super.world != null)
            switch(rnd.nextInt(2)) {
                case 0: new Square(super.world); break;
                case 1: new Circle(super.world); break;
            }
    }

    @Override
    public Bonus initBonus() {
        return null;
    }
}
