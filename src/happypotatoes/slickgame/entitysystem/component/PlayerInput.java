package happypotatoes.slickgame.entitysystem.component;

import org.newdawn.slick.Input;

import happypotatoes.slickgame.Camera;
import happypotatoes.slickgame.entitysystem.Component;
import happypotatoes.slickgame.entitysystem.Entity;
import happypotatoes.slickgame.world.World;

public class PlayerInput extends Component {
	float destx, desty;
	
	Walker walker;
	Movement movement;
	float speed = 0.002f;
	public PlayerInput(Entity owner, float priority, Walker walker, Movement movement) {
		super(owner, priority);
		this.walker = walker;
		this.movement = movement;
	}
	public void update(World w, long delta) {
		Camera c = Camera.camera;
		Input input = w.container.getInput();
		
		if (Math.abs(movement.speedx)<0.0001&&Math.abs(movement.speedy)<0.0001&&walker.state==1) {
			walker.state = 0;
		}
				
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			destx = input.getMouseX()/(float)c.getUnit()+c.getRekt().x0;
			desty = input.getMouseY()/(float)c.getUnit()+c.getRekt().y0;
			if (walker.state==0)
				walker.state = 1;
		}
		
		if(walker.state==1) {
			float dx = destx-owner.x;
			float dy = desty-owner.y;
			float d = (float)Math.sqrt(dx*dx+dy*dy);
			float msx = 0;
			float msy = 0;
			if (d<.1) {
				walker.state=0;
				movement.speedx = 0;
				movement.speedy = 0;
			} else {
				msx = dx/d*speed;
				msy = dy/d*speed;
				movement.speedx += msx;
				movement.speedy += msy;
			}
			if (msx!=0||msy!=0)
				walker.setFacing(msx, msy);
		}			
			
		if (input.isKeyDown(Input.KEY_E)&&((Health) owner.getComponent(Health.class)).getHealth()>0&&walker.state<2) {
			walker.state=2;
			((Energy) owner.getComponent(Energy.class)).setEnergy(((Energy) owner.getComponent(Energy.class)).getEnergy()-20);
		}
		
	}
}