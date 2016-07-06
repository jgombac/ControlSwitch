package bonuses;


public class SlowBonus extends Bonus{
    private float duration;
    private float slow;


    public SlowBonus(int n){
        super(n);
        duration = 5f;
        slow = 1/100f;
    }

    public SlowBonus(){
        super(1);
        duration = 5f;
        slow = 1/100f;
    }


    @Override
    public void update(float dt) {
        super.updateBonus(dt, this.duration);
    }

    @Override
    public float getSlow(){
        return slow;
    }

    public float getTimeLeft(){
        return duration - super.getTimer();
    }
}
