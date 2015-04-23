package happypotatoes.slickgame.entitysystem;

import happypotatoes.slickgame.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class EntitySystem {
	private static EntitySystem instance;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private LinkedBlockingQueue<Entity> toAdd = new LinkedBlockingQueue<Entity>();
	private LinkedBlockingQueue<Entity> toRemove = new LinkedBlockingQueue<Entity>();
	private long nextid = 1;
	
	private EntitySystem() {}
	
	public static EntitySystem getInstance() {
		if (instance==null)
			instance = new EntitySystem();
		return instance;
	}
	
	public long getFreeID() {
		return nextid++;
	}
	
	public Entity getEntity(int id) {
		for(Entity e:entities)
			if (e.id == id)
				return e;
		return null;
	}
	
	public List<Entity> getAll() {
		return entities;
	}
	
	public <T extends Component> List<Entity> getEntities(Class<T>... components) {
		List<Entity> list = new ArrayList<Entity>();
		for (Entity e:entities) {
			boolean hasComponents = true;
			for (Class<T> c:components)
				if (e.getComponent(c)==null) {
					hasComponents = false;
					break;
				}
			if (hasComponents)
				list.add(e);
		}
			
		return list;
	}

	public void addEntity(Entity e){
		toAdd.add(e);
	}
	
	public void removeEntity(Entity e){
		toRemove.add(e);
	}
	
	public void update(World w, long delta) {
		while (!toAdd.isEmpty())
			entities.add(toAdd.poll());
		
		while (!toRemove.isEmpty())
			entities.remove(toAdd.poll());
		
		for (Entity entity : entities)
			entity.update(w, delta);
	}
	
}
