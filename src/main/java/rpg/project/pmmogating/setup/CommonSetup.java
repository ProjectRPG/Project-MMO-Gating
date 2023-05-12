package rpg.project.pmmogating.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import rpg.project.lib.api.gating.GateUtils;
import rpg.project.pmmogating.core.ProgressionConfigType;
import rpg.project.pmmogating.core.ProgressionGates;

public class CommonSetup {

	//TODO potentially move to main class if we don't do anything else here
	public static void init(FMLCommonSetupEvent event) {
		GateUtils.registerProgressGate(ProgressionConfigType.ID, ProgressionConfigType.IMPL, new ProgressionGates());
	}
}
