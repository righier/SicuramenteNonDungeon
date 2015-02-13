package happypotatoes.slickgame.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import happypotatoes.slickgame.Camera;
import happypotatoes.slickgame.entity.Entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Quadtree {
	private int maxEntities;
	private int maxDepth;
	private int depth;
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	private Rectangle area;
	
	private Quadtree parent = null;
	private Quadtree[] nodes; 
	//  0 1
	//  2 3
	
	public Quadtree(int maxEntities, int maxDepth, Quadtree parent, Rectangle area) {
		nodes = null;
		this.maxEntities = maxEntities;
		this.maxDepth = maxDepth;
		this.area = area;
		
		this.parent = parent;
		
		if (parent!=null)
			depth = parent.getDepth()+1;
		else
			depth = 1;
	}
	
	
	
	public int getDepth() {
		return depth;
	}
	
	private void genChildren() {
		nodes = new Quadtree[4];
		float x = area.getX();
		float y = area.getY();
		float w = area.getWidth()/2f;
		float h = area.getHeight()/2f;
		
		Rectangle a0 = new Rectangle(x, y, w, h);
		Rectangle a1 = new Rectangle(x+w, y, w, h);
		Rectangle a2 = new Rectangle(x, y+h, w, h);
		Rectangle a3 = new Rectangle(x+w, y+h, w, h);
		
		nodes[0] = new Quadtree(maxEntities, maxDepth, this, a0);
		nodes[1] = new Quadtree(maxEntities, maxDepth, this, a1);
		nodes[2] = new Quadtree(maxEntities, maxDepth, this, a2);
		nodes[3] = new Quadtree(maxEntities, maxDepth, this, a3);
		
		List<Entity> entities = this.entities;
		this.entities = new ArrayList<Entity>();
		
		for (Entity e:entities)
			add(e);
	}
	
	public Rectangle getArea() {
		return area;
	}
	
	public boolean contains(Rectangle r1, Rectangle r2) {
		return 
				r1.getMinX()<=r2.getMinX()&&
				r1.getMinY()<=r2.getMinY()&&
				r1.getMaxX()>=r2.getMaxX()&&
				r1.getMaxY()>=r2.getMaxY();
	}
	
	public void add(Entity entity) {
		if (contains(area,entity.getSpriteShape())) {
			boolean added = false;
			if (nodes!=null) {
				Rectangle shape = entity.getSpriteShape();
				for (Quadtree node:nodes)
					if (contains(node.getArea(),shape)) {
						node.add(entity);
						added = true;
						break;
					}
			} 
			if (!added)
				addToList(entity);
		} else 
			if (parent==null)
				addToList(entity);
			else 
				parent.add(entity);
	}
	
	public void addToList(Entity entity) {
		entities.add(entity);
		if (entities.size()>maxEntities&&nodes==null) {
			genChildren();
		}
				
	}
	
	public void remove(Entity entity) {
		boolean removed = false;
		if (nodes!=null) {
			Rectangle shape = entity.getSpriteShape();
			for (int i=0;i<nodes.length;i++)
				if (contains(nodes[i].getArea(),shape)) {
					nodes[i].remove(entity);
					removed = true;
					break;
				}
			
		}
		
		if (!removed) {
			entities.remove(entity);
		}
	}
	

	public void update(GameContainer container, World world, int delta) {
		List<Entity> toAdd = new ArrayList<Entity>();
		this.update(container, world, delta, toAdd);
		for (Entity e:toAdd)
			add(e);
	}
	
	public void update(GameContainer container, World world, int delta, List<Entity> toAdd) {

		if (nodes!=null)
			for (Quadtree node:nodes)
				node.update(container, world, delta, toAdd);
		
		List<Entity> remove = new ArrayList<Entity>();
		
		for (Entity e:entities) {
			e.update(container, world, delta);
			if (!contains(area,e.getSpriteShape())) {
				if (parent!=null) {
					remove.add(e);
				}
			} else 
				if (nodes!=null)
					for (Quadtree node:nodes)
						if (contains(node.getArea(),e.getSpriteShape())) {
							remove.add(e);
						}
		}
		
		for (Entity e:remove) {
			entities.remove(e);
			toAdd.add(e);
		}
		
	}
	
	public void collisions(Queue<Entity> entityQueue) {
		if (entityQueue==null) {
			entityQueue = new LinkedBlockingQueue<Entity>();
		}
		
		int nentities = 0;
		for (Entity e:entities)
			if (e.doesCollide()) {
				entityQueue.add(e);
				nentities++;
			}
		
		if (nodes!=null)
			for (Quadtree node:nodes)
				node.collisions(entityQueue);
		
		for (int i=0;i<nentities;i++) {
			Entity e = entityQueue.poll();
			for (Entity a:entityQueue) {
				if (a.getShape().intersects(e.getShape())) {
					e.collideWith(a);
					a.collideWith(e);
				}
			}
		}
			
	}

	public void getRenderizable(Rectangle view, List<Entity> list) {
		for (Entity e:entities) 
			if (e.getSpriteShape().intersects(view))
				addToList(e, list);
		
		if (nodes!=null)
			for (Quadtree node:nodes)
				node.getRenderizable(view, list);			
	}
	
	public boolean isEmpty() {
		return entities.isEmpty()&&nodes==null;
	}
	
	public boolean isLast() {
		return nodes==null;
	}
	
	private void addToList(Entity e, List<Entity> list) {
		if (list.isEmpty())
			list.add(e);
		else {
			float y = e.getY();
			for (int i=0;i<list.size();i++)
				if (list.get(i).getY()>y) {
					list.add(i, e);
					return;
				}
			list.add(e);
		}
	}
	
	private static Color[] colors = new Color[] {
		new Color(255,   0,   0, 150), 
		new Color(  0, 255,   0, 150), 
		new Color(255, 255,   0, 150),
		new Color(  0,   0, 255, 150),
	};
	
	public void render(Graphics g) {
		if (nodes!=null) {
			for (int i=0;i<4;i++)
				if (nodes[i].isLast()) {
					Rectangle s = nodes[i].getArea();
					g.setColor(colors[i]);
					g.fillRect(s.getMinX(), s.getMinY(), s.getWidth(), s.getHeight());
				} else
					nodes[i].render(g);
		}
	}
}
