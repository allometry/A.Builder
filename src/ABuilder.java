import java.util.Map;

import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSInterfaceChild;
import org.rsbot.script.wrappers.RSInterfaceComponent;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;

@ScriptManifest(authors = { "Allometry" }, category = "Construction", description = "Allometry Builder", name = "A. Builder")
public class ABuilder extends Script {
	private int rickServantID = 0, maidServantID = 0, cookServantID = 0, butlerServantID = 4241, demonButlerServantID = 0;
	private int larderSpaceID = 15403, oakLarderID = 13566;
	private int oakPlankID = 8778, oakPlankNoteID = 0, oakLogID = 0, oakLogNoteID = 1522;
	private int objectsInServantInventory = 0;
	
	private Servant demonButlerServant = new Servant(4243, 26);
	private Servant butlerServant = new Servant(4241, 20);
	
	public class Servant {
		public int id;
		public int inventorySpace;
		
		public Servant(int id, int inventorySpace) {
			this.id = id;
			this.inventorySpace = inventorySpace;
		}
	}
	
	private long timeout = 0;
	
	private boolean isServantHoldingPlanks = false, withdrawFromBank = false; 
	
	@Override
	public boolean onStart(Map<String,String> args) {
		return true;
	}
	
	@Override
	public int loop() {
		try {
			log("Looping...");
			//Pay the Servant
			//Uses same interface as holding items, so we need some logic!
			//Check if Child 4 contains, "Master, if thou desirest my continued service"
			//Then click Child 7
			//Wait for interface 232
			//If paying from bank, click Child 3
			//Else click Child 2
			//Thank the butler, Parent 241, click Child 5
			
			//Un-note logs
			//Parent 230, Child 2
			
			//Un-note number entry
			//Parent 752, Child 5
			//Send 26
			
			//Send to sawmill
			//Parent 228, Child 3
			
			//Check for butler holding no items
			//Parent 242
			
			//Check for butler holding items
			//Parent 243
			
			//Check if we need to utilize the butler
			if(!isInventoryFull()) {
				RSNPC servant = getNearestFreeNPCByID(butlerServant.id, demonButlerServant.id);
				if(servant != null) {
					//If the butler is holding planks, grab them.
					if(isServantHoldingPlanks) {
						atNPC(servant, null);
						
					}
				}
			}
			
			//Build Oak Larder
			RSObject larderSpace = getNearestObjectByID(15403);
			if(larderSpace != null && (!getInterface(394).isValid() || getInterface(394, 11).getAbsoluteY() < 30)) {
				log("Moving to click object");
				
				atObject(larderSpace, "Build Larder space");
				
				return random(1000, 1500);
			} else if(getInterface(394).isValid() && getInterface(394, 11).getAbsoluteX() > 30) {
				RSInterfaceComponent buildOakLarder = new RSInterfaceComponent(getInterface(394), 11, 1);
				log((buildOakLarder == null) ? "Interface component is null" : "Found interface component");
				
				if(buildOakLarder != null)
					atComponent(buildOakLarder, "Build Oak larder");
			}
			
			//Remove Oak Larder
			RSObject oakLarder = getNearestObjectByID(oakLarderID);
			if(oakLarder != null && !getInterface(228).isValid()) {
				log("Removing larder");
				
				atObject(oakLarder, "Remove Larder");
				
				return random(1000, 1500);
			} else if(getInterface(228).isValid()) {
				RSInterfaceChild confirmInterface = getInterface(228, 2);
				
				atInterface(confirmInterface, true);
				
				return random(1000, 1500);
			}
		} catch(Exception e) {
			log("Caught exception: " + e.getMessage());
		}
			
		return 1;
	}
	
	@Override
	public void onFinish() {
		return ;
	}
}
