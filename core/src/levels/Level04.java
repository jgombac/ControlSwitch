package levels;


import com.badlogic.gdx.physics.box2d.World;
import com.phgame.bopit.BopGame;

import java.util.Random;

import bonuses.Bonus;
import gameobjects.Circle;
import gameobjects.Square;

public class Level04 extends Level{



    private Random rnd;

    private static final float max = 1.5f;
    private static final float min = 0.5f;

    private static final int scoreTarget = 20;


    public Level04(){
        super(4, min, max, scoreTarget);
        rnd = new Random();
    }


    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    void spawnObject() {
        if(super.world != null)
            switch(rnd.nextInt(4)) {
                case 0: new Square(super.world); break;
                case 1: new Circle(super.world); break;
                case 2:
                    float pos = rnd.nextFloat() * ((BopGame.WIDTH/100-.9f) - .9f) + .9f;
                    new Circle(super.world, pos, false); new Circle(super.world, pos, true); break;
                case 3:
                    float pos1 = rnd.nextFloat() * ((BopGame.WIDTH/100-.9f) - .9f) + .9f;
                    new Square(super.world, pos1, false); new Square(super.world, pos1, true); break;
            }
    }

    @Override
    public Bonus initBonus() {
        return null;
    }
}
