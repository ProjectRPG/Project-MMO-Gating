package rpg.project.pmmogating;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rpg.project.lib.api.gating.GateUtils;
import rpg.project.pmmogating.core.EventConfigType;
import rpg.project.pmmogating.core.EventGates;
import rpg.project.pmmogating.core.ProgressionConfigType;
import rpg.project.pmmogating.core.ProgressionGates;

@Mod(PmmoGating.MODID)
public class PmmoGating {
	public static final String MODID = "pmmogating";
	
	public PmmoGating() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
	}
	
	public void init(FMLCommonSetupEvent event) {
		GateUtils.registerProgressGate(ProgressionConfigType.ID, ProgressionConfigType.IMPL, new ProgressionGates());
		GateUtils.registerEventGate(EventConfigType.ID, EventConfigType.IMPL, new EventGates());
	}
}
