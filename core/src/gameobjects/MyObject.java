package gameobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyObject {
    public Sprite sprite;
    public Enums en;
    public boolean hit;

    public MyObject(Sprite sprite, Enums en){
        this.sprite = sprite;
        this.en = en;
        hit = false;
    }

    public String toString(){
        switch(en){
            case SQUARE:
                return "square";
            case CIRCLE:
                return "circle";
            default:
                return "";
        }
    }

    public Sprite getSprite(){
        return sprite;
    }
}
