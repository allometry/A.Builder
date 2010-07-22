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
	
	private boolean withdrawFromBank = false;
	
	@Override
	public boolean onStart(Map<String,String> args) {
		return true;
	}
	
	public boolean isInterfacesValid() {
		return getInterface(buildLarderParentInterface, buildOakLarderChildInterface).isValid() || 
			getInterface(removeConfirmParentInterface, removeConfirmYesChildInterface).isValid() || 
			getInterface(unnoteLogsConfirmParentChildInterface, unnoteLogsConfirmParentChildInterface).isValid();
	}
	
	@Override
	public int loop() {
		try {
			//Check and withdraw planks
			if(getInventoryCount(oakPlankID) < 8 && !isInterfacesValid()) {
				
				//Check for oak log notes
				if(getInventoryCount(oakLogNoteID) > 0) {
					RSNPC butler = getNearestFreeNPCByID(rickServantID, maidServantID, cookServantID, butlerServantID, demonButlerServantID);
					
					if(butler != null) {
						if(atInventoryItem(oakLogNoteID, "")) {
							atNPC(butler, "");
						}
					}
				}
			}
			
			//Build Oak Larder
			if(getNearestObjectByName("Larder space") != null && !isInterfacesValid()) {
				RSObject larderSpace = getNearestObjectByID(oakLarderID);
				atObject(larderSpace, "Build Larder Space");
			}
			
			//Remove Oak Larder
			if(getNearestObjectByID(oakLarderID) != null && !isInterfacesValid()) {
				RSObject oakLarder = getNearestObjectByID(oakLarderID);
				oakLarder.action("Remove Larder");
				return 1500;
			}
			
			//Check if build interface is up
			if(getInterface(buildLarderParentInterface, buildOakLarderChildInterface).isValid()) {
				RSInterfaceChild buildInterface = getInterface(buildLarderParentInterface, buildOakLarderChildInterface);
				atComponent(buildInterface.getComponents()[1], "Build");
			}
			
			//Check if remove interface is up
			if(getInterface(removeConfirmParentInterface, removeConfirmYesChildInterface).isValid()) {
				atInterface(removeConfirmParentInterface, removeConfirmYesChildInterface);
			}
			
			//Check if unnote interface is up
			if(getInterface(unnoteLogsConfirmParentChildInterface, unnoteLogsConfirmParentChildInterface).isValid()) {
				if(atInterface(unnoteLogsConfirmParentChildInterface, unnoteLogsConfirmParentChildInterface)) {
					sendText(new Integer(random(20, 45)).toString(), true);
				}
			}
		} catch(Exception e) {
			
		}
		
		return 1;
	}
	
	@Override
	public void onFinish() {
		return ;
	}
}
