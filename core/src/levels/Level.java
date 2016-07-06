package levels;

import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import bonuses.Bonus;

public abstract class Level implements Comparable<Level>{


    public int ID;
    World world;


    private Bonus bonus;
    private boolean bonusActive;
    public boolean bonusAvailable;

    private float timer;
    private float spawner;
    private Random rnd;

    private float min;
    private float max;

    public int bonusActivate;

    private boolean gameOver = false;

    private int scoreTarget;


    /**
     *
     * @param ID st. levela
     * @param min minimalen interval spawn timerja
     * @param max maximalen interval spawn timerja
     * @param scoreTarget tockovni cilj levela
     */
    public Level(int ID, float min, float max, int scoreTarget){
        this.ID = ID;
        this.min = min;
        this.max = max;
        bonusActive = false;
        bonusAvailable = false;
        this.scoreTarget = scoreTarget;



        timer = 0;
        rnd = new Random();
        spawner = rnd.nextFloat() * (max - min) + min;
    }

    /**
     *
     * @param ID st. levela
     * @param min minimalen interval spawn timerja
     * @param max max maximalen interval spawn timerja
     * @param bonusAvailable ali je uporaba bonusa možna (dost neumen parameter, bo šel enkrat stran)
     * @param bonusActivate št možnih uporab bonusa
     * @param scoreTarget tockovni cilj levela
     */

    public Level(int ID, float min, float max, boolean bonusAvailable, int bonusActivate, int scoreTarget){
        this.ID = ID;
        this.min = min;
        this.max = max;
        bonusActive = false;
        this.bonusAvailable = bonusAvailable;
        this.bonusActivate = bonusActivate;
        this.scoreTarget = scoreTarget;

        timer = 0;
        rnd = new Random();
        spawner = rnd.nextFloat() * (max - min) + min;
    }


    public void update(float dt){
        //če je igre že konec ne updatam
        if(!gameOver)
            timer += dt;

        //spawnam objekt če je že napočil čas
        if(timer >= spawner){
            spawnObject();
            timer = 0;
            spawner = rnd.nextFloat() * (max - min) + min;
        }

        //če bonus traja
        if(bonus != null && bonus.getUse())
            bonusActive = true;

        //če bonus ne traja več in je zmanjkalo št. njegovih uporab ga odstranim
        if(bonus != null && !bonus.getUse() && bonus.getN() <= 0){
            bonus = null;
            bonusActive = false;

        }
        //če bonus ne traja več
        if(bonus != null && !bonus.getUse()) {
            bonusActive = false;
        }
    }

    synchronized void spawnObject() {

    }

    public int getScoreTarget(){
        return scoreTarget;
    }

    public void setBonus(Bonus bonus){
        if(bonusAvailable) {
            this.bonus = bonus;
            bonusAvailable = false;
        }

    }

    public void itsOver(){
        gameOver = true;
    }

    public void setWorld(World world){
        this.world = world;
    }

    @Override
    public int compareTo(Level o) {
        return this.ID - o.ID;
    }

    public boolean getBonusActive(){
        return bonusActive;
    }

    public void setBonusActive(boolean b){
        bonusActive = b;
    }

    public Bonus getBonus(){
        return bonus;
    }

    public abstract Bonus initBonus();
}
