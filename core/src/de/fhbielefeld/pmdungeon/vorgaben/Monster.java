package de.fhbielefeld.pmdungeon.vorgaben;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.fhbielefeld.pmdungeon.vorgaben.dungeonCreater.DungeonWorld;
import de.fhbielefeld.pmdungeon.vorgaben.graphic.Animation;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IAnimatable;
import de.fhbielefeld.pmdungeon.vorgaben.interfaces.IUpdateable;
import de.fhbielefeld.pmdungeon.vorgaben.tools.Point;

import java.util.ArrayList;
import java.util.Random;

public class Monster implements IAnimatable, IUpdateable {

    private Point position;
    private DungeonWorld level;
    private Animation idleAnimation;

    int counter=0;

    public Monster(){
        this.level=level;

        ArrayList<Texture> idleTextures = new ArrayList<Texture>();
        Texture idle1= new Texture("textures/characters/demons/imp/imp_run_anim_f0.png");
        Texture idle2= new Texture("textures/characters/demons/imp/imp_run_anim_f1.png");
        Texture idle3= new Texture("textures/characters/demons/imp/imp_run_anim_f2.png");
        Texture idle4= new Texture("textures/characters/demons/imp/imp_run_anim_f3.png");
        idleTextures.add(idle1);
        idleTextures.add(idle2);
        idleTextures.add(idle3);
        idleTextures.add(idle4);
        idleAnimation = new Animation(idleTextures,4);


    }

    public void updateLevel(DungeonWorld level){
        this.level=level;
        this.position=new Point(level.getRandomLocationInDungeon());
    }

    @Override
    public Animation getActiveAnimation() {
        return this.idleAnimation;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean deleteable() {
        return false;
    }

    @Override
    public void update() {

        System.out.println(position.x+" "+position.y);
        if(level==null) return;

        double x=-0.01;
        double y=0;

        if (level.isTileAccessible((int)(position.x+x),(int)(position.y+y))){
            this.position.x+=x;
            this.position.y+=y;
        }


        this.draw();
    }
}
