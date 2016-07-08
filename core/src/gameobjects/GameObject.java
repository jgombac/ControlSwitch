package gameobjects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class GameObject {
    public Enums type;
    public Sprite sprite;

    public GameObject(Enums type){
        this.type = type;
    }

    public Enums getType(){
        return type;
    }

    public Body getBody(){
        return null;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }
}
