package happypotatoes.slickgame.entitysystem.entity;

import happypotatoes.slickgame.entitysystem.Entity;
import happypotatoes.slickgame.entitysystem.EntitySystem;
import happypotatoes.slickgame.entitysystem.component.AI;
import happypotatoes.slickgame.entitysystem.component.AIManager;
import happypotatoes.slickgame.entitysystem.component.AIPet;
import happypotatoes.slickgame.entitysystem.component.AIType;
import happypotatoes.slickgame.entitysystem.component.Attack;
import happypotatoes.slickgame.entitysystem.component.Energy;
import happypotatoes.slickgame.entitysystem.component.EntityCollision;
import happypotatoes.slickgame.entitysystem.component.Faction;
import happypotatoes.slickgame.entitysystem.component.Health;
import happypotatoes.slickgame.entitysystem.component.Interact;
import happypotatoes.slickgame.entitysystem.component.SelectComponent;
import happypotatoes.slickgame.entitysystem.component.Walk;
import happypotatoes.slickgame.entitysystem.component.WalkerRender;
import happypotatoes.slickgame.entitysystem.component.HitBox;
import happypotatoes.slickgame.entitysystem.component.Movement;
import happypotatoes.slickgame.entitysystem.component.TerrainCollision;
import happypotatoes.slickgame.entitysystem.component.Walker;
import happypotatoes.slickgame.entitysystem.component.WalkerRender;

public class Chest {
	static float speed = 0.001f;
	int facing;
	
	public static Entity create(int facing) {
		Entity e = new Entity(EntitySystem.getInstance().getFreeID(),"Chest");
		HitBox hitBox = new HitBox(e, .4f, .4f, 0);
		OpenChest i = new OpenChest(e, 0);
		Faction f = new Faction(e, 0, Faction.neutral);
		Walker walker = new Walker(e, 0, 8, 4, facing);
		WalkerRender walkerRender = new WalkerRender(e, walker, "res/Sprites/Mobs/wolf/", 96, 54, -.75f, -0.8f);	
		SelectComponent selectComponent = new SelectComponent(e, 0, -.4f,-.8f, 1, 1);
		return e;
	}
	
	public static Entity create(float x, float y, int facing){
		Entity e = create(facing);
		e.x=x; e.y=y;
		return e;
	}
}