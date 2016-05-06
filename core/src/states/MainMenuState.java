package states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.phgame.bopit.BopGame;

public class MainMenuState extends State{

    private BitmapFont font;




    public MainMenuState(GameStateManager gsm) {
        super(gsm);
        initFonts();
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.push(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        font.draw(sb, "NEW GAME", BopGame.WIDTH/2 - font.getSpaceWidth()*8, BopGame.HEIGHT/2);
        sb.end();

    }

    @Override
    public void dispose() {
        font.dispose();
    }

    private void initFonts(){
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/brainstorm_bold.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 50;
        param.color = com.badlogic.gdx.graphics.Color.GOLD;
        font = gen.generateFont(param);
        gen.dispose();
    }
}
