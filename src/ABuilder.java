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
	private int oakPlankID = 0, oakPlankNoteID = 0, oakLogID = 0, oakLogNoteID = 0;
	
	private int buildLarderParentInterface = 394, removeConfirmParentInterface = 228;
	private int buildOakLarderChildInterface = 11, removeConfirmYesChildInterface = 2;
	private int unnoteLogsConfirmParentInterface = 230, unnoteLogsConfirmParentChildInterface = 2;
	private int sendLogsToSawmillParent = 228, sendLogsToSawmillChild = 3;
	private int holdPlankParentInterface = 241, holdPlankChildInterface = 4;
	private int buildOakLarderComponent = 1;
	
	private long timeout = 0;
	
	private boolean withdrawFromBank = false; 
	
	@Override
	public boolean onStart(Map<String,String> args) {
		return true;
	}
	
	@Override
	public int loop() {
		log("Looping...");
		
		//Build Oak Larder
		if(!getInterface(394, 228).isValid()) {
			RSObject larderSpace = getNearestObjectByID(15403);
			if(larderSpace != null) {
				timeout = System.currentTimeMillis() + 5000;
				while(getInterface(394, 228) == null && System.currentTimeMillis() < timeout) {
					atObject(larderSpace, "Build");
					wait(random(1000,2000));
				}
				
				return random(1000, 1500);
			} else {
				log("Couldn't find oak larder space...");
			}
		} else {
			log("Interface is valid, skipping logic...");
		}
		
		//Build Oak Larder Interface / Component
		if(getInterface(394, 228).isValid()) {
			RSInterfaceComponent buildOakLarder = new RSInterfaceComponent(getInterface(394), 228, 1);
			
			if(buildOakLarder != null) {
				if(buildOakLarder.isValid()) {
					timeout = System.currentTimeMillis() + 5000;
					while(buildOakLarder.isValid() && System.currentTimeMillis() < timeout) {
						atComponent(buildOakLarder, "Build");
						wait(random(1000,2000));
					}
					
					return random(1000, 1500);
				}
			} else {
				log("Couldn't find build larder interface and component...");
			}
		} else {
			log("Build interface isn't valid...");
		}
		
		return 1;
	}
	
	@Override
	public void onFinish() {
		return ;
	}
}
