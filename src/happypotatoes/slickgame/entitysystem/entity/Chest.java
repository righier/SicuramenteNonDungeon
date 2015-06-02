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
import happypotatoes.slickgame.entitysystem.component.Inventory;
import happypotatoes.slickgame.entitysystem.component.Open;
import happypotatoes.slickgame.entitysystem.component.SelectComponent;
import happypotatoes.slickgame.entitysystem.component.Walk;
import happypotatoes.slickgame.entitysystem.component.WalkerRender;
import happypotatoes.slickgame.entitysystem.component.HitBox;
import happypotatoes.slickgame.entitysystem.component.Movement;
import happypotatoes.slickgame.entitysystem.component.TerrainCollision;
import happypotatoes.slickgame.entitysystem.component.Walker;
import happypotatoes.slickgame.entitysystem.component.WalkerRender;
import happypotatoes.slickgame.entitysystem.interact.OpenChest;

public class Chest {
	static float speed = 0.001f;
	int facing;
	
	public static Entity create(int facing) {
		Entity e = new Entity(EntitySystem.getInstance().getFreeID(),"Chest");
		HitBox hitBox;
		switch(facing){
		case 2: case 6: hitBox = new HitBox(e, .4f, .7f, 0); break;
		case 0: case 4: hitBox = new HitBox(e, .7f, .4f, 0); break;
		default: hitBox = new HitBox(e, .65f, .65f, 0); break;
		}
		Faction f = new Faction(e, 0, Faction.neutral);
		Walker walker = new Walker(e, 0, 8, 3, facing);
		WalkerRender walkerRender = new WalkerRender(e, walker, "res/Sprites/Statics/chest/", 222, 144, -1.73f, -1.73f);	
		SelectComponent selectComponent = new SelectComponent(e, 0, -.7f,-1.2f, 1.4f, 1.4f);
		Inventory i = new Inventory(e, 0, 2, 2);
		Open o = new Open(e, 0,walker, walkerRender);
		OpenChest open = new OpenChest(e, walker, 0);
		return e;
	}
	
	public static Entity create(float x, float y, int facing){
		Entity e = create(facing);
		e.x=x; e.y=y;
		return e;
	}
}
