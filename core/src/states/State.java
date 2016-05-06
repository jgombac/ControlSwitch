package states;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {
    protected GameStateManager gsm;
    protected Vector3 mouse;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
        mouse = new Vector3();
    }

    protected abstract void handleInput();
    protected abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();

}
