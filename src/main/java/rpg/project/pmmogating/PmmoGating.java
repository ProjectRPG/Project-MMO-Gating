package rpg.project.pmmogating;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import rpg.project.lib.api.gating.GateUtils;
import rpg.project.pmmogating.core.EventConfigType;
import rpg.project.pmmogating.core.EventGates;
import rpg.project.pmmogating.core.ProgressionConfigType;
import rpg.project.pmmogating.core.ProgressionGates;

@Mod(PmmoGating.MODID)
public class PmmoGating {
	public static final String MODID = "pmmogating";
	
	public PmmoGating(IEventBus modbus) {
		modbus.addListener(this::init);
	}
	
	public void init(FMLCommonSetupEvent event) {
		GateUtils.registerProgressGate(ProgressionConfigType.ID, ProgressionConfigType.IMPL, new ProgressionGates());
		GateUtils.registerEventGate(EventConfigType.ID, EventConfigType.IMPL, new EventGates());
	}
}
