package happypotatoes.slickgame.entity;

import happypotatoes.slickgame.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import MobsManagers.AIManager;

public class Mob extends Npc implements IsEnemy{
	private String name, spritePath;
	private int health;
	private int timer;
	public static final String path = "./res/Mobs/";
	public Mob(boolean doesCollide, String race) {
		super(doesCollide);
		File f;
		f= new File(path+race+".mob");
		loadParams(f);
		try {
			Image texture = new Image(spritePath);
			texture.setFilter(Image.FILTER_NEAREST);
			setSize(48, 48, 2);
			setAnimations(texture, 0, 1, 1);
			setAnimations(texture, 1, 4, 100);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void render() {
		super.render();
	}
	public void loadParams(File f) {
	    Properties props = new Properties();
	    InputStream is = null;
	    try {
	        is = new FileInputStream( f );
	    }
	    catch ( Exception e ) { is = null; }
	    try {
	        if ( is == null ) {
	            is = getClass().getResourceAsStream(f.getPath());
	        }
	        props.load( is );
	    }
	    catch ( Exception e ) { }
	    try{
	    	name = props.getProperty("Name");
	    	health = Integer.parseInt(props.getProperty("Health"));
	    	spritePath=props.getProperty("SpritePath");
	    } catch(Exception e){
	    	System.out.println("dati nel file corrotti");
	    }
	}
	public void update(GameContainer container, World world, int delta) {
		super.update(container, world, delta);
		AIManager.move(this);
	}
	public void collideWith(Entity entity){
		
	}
	private void die(World world) {
		world.remove(this);
	}
}
