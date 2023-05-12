package rpg.project.pmmogating;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rpg.project.pmmogating.setup.CommonSetup;

@Mod(PmmoGating.MODID)
public class PmmoGating {
	public static final String MODID = "pmmogating";
	
	public PmmoGating() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		modBus.addListener(CommonSetup::init);
	}
}
