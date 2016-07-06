package bonuses;


public abstract class Bonus {
    private int n;
    private float timer;
    private boolean inUse;

    public Bonus(int n){
        this.n = n;
        timer = 0;
        inUse = false;
    }

    public Bonus(){
        this.n = 1;
        timer = 0;
        inUse = false;
    }

    public void updateBonus(float dt, float duration){
        if(inUse) {
            timer += dt;
            if (timer > duration) {
                timer = 0;
                inUse = false;
            }
        }
    }

    public void startBonus(){
        if(n >= 1) {
            n--;
            inUse = true;
        }
    }

    public boolean getUse(){
        return inUse;
    }

    public int getN(){
        return n;
    }

    public float getTimer(){
        return timer;
    }

    public abstract void update(float dt);

    public abstract float getSlow();
}
