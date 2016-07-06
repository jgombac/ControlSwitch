package bonuses;


public class StopBonus extends Bonus{
    private float duration;

    public StopBonus(int n){
        super(n);
        duration = 2f;
    }


    @Override
    public void update(float dt) {
        super.updateBonus(dt, duration);
    }

    @Override
    public float getSlow() {
        return 0;
    }
}
