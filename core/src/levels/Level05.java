package levels;

import com.phgame.bopit.BopGame;

import java.util.Random;

import bonuses.Bonus;
import bonuses.SlowBonus;
import gameobjects.Circle;
import gameobjects.Square;

public class Level05 extends Level{



    private Random rnd;

    private static final float max = 1.5f;
    private static final float min = 0.5f;

    private static final int scoreTarget = 100;


    public Level05(){
        super(5, min, max, true, 3, scoreTarget);
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
                case 0:
                    float pos = rnd.nextFloat() * ((BopGame.WIDTH/100-.9f) - .9f) + .9f;
                    new Circle(super.world, pos, false); new Circle(super.world, pos, true); break;
                case 1:
                    float pos1 = rnd.nextFloat() * ((BopGame.WIDTH/100-.9f) - .9f) + .9f;
                    new Square(super.world, pos1, false); new Square(super.world, pos1, true); break;
                case 2:
                    float pos2 = rnd.nextFloat() * ((BopGame.WIDTH/100-.9f) - .9f) + .9f;
                    new Circle(super.world, pos2, false); new Square(super.world, pos2, true); break;
                case 3:
                    float pos3 = rnd.nextFloat() * ((BopGame.WIDTH/100-.9f) - .9f) + .9f;
                    new Square(super.world, pos3, false); new Circle(super.world, pos3, true); break;
            }
    }

    @Override
    public Bonus initBonus() {
        return new SlowBonus(5);
    }
}