package levels;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.phgame.bopit.BopGame;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Levels {

    private int currentLevel;
    public SortedMap<Integer, Class> levels;
    Pool<Texture> circles = new Pool<Texture>() {
        @Override
        protected Texture newObject() {
            return new Texture("objects/zogaC.png");
        }
    };


    public Levels(int currentLevel){
        this.currentLevel = (currentLevel > 1) ? currentLevel : 1;
        levels = new TreeMap<Integer, Class>();
        levels.put(1, Level01.class);
        levels.put(2, Level02.class);
        levels.put(3, Level03.class);
        levels.put(4, Level04.class);
        levels.put(5, Level05.class);


    }


    public Class getLevel(int i){
        return levels.get(i);
    }

    public Class getCurrent(){
        return levels.get(currentLevel);
    }

    public void levelUp() {
        if(currentLevel < levels.size())
            currentLevel++;
        System.out.println("LEVEL UP : " + currentLevel);
    }

    public Set<Integer> getKeys(){
        return levels.keySet();
    }

}
