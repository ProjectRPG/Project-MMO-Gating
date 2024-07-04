package rpg.project.pmmogating.core;

import java.util.List;
import java.util.Map;

import org.joml.Math;

import net.minecraft.resources.ResourceLocation;
import rpg.project.lib.api.Hub;
import rpg.project.lib.api.data.ObjectType;
import rpg.project.lib.api.data.SubSystemConfig;
import rpg.project.lib.api.events.EventContext;
import rpg.project.lib.api.gating.GateSystem;
import rpg.project.lib.api.gating.GateUtils.Type;
import rpg.project.lib.api.progression.ProgressionDataType;
import rpg.project.lib.api.progression.ProgressionDataType.Comparison;
import rpg.project.pmmogating.core.EventConfigType.EventConfig;

public class EventGates implements GateSystem {

	@Override
	public float isActionPermitted(EventContext context, Hub core, ResourceLocation eventID, String ignored) {		
		EventConfig eventGates = (EventConfig) core.getGateData(EventConfigType.IMPL, ObjectType.EVENT, Type.EVENT, eventID).orElse(null);
		if (eventGates == null)
			return 1f;
		
		float outValue = 1f;
		for (Map.Entry<String, List<SubSystemConfig>> reqs : eventGates.reqs().entrySet()) {
			ProgressionDataType eventGateValue = reqs.getValue().stream()
					.filter(config -> config.getType().equals(core.getProgression().dataType()))
					.map(ssc -> (ProgressionDataType)ssc)
					.findFirst()
					.orElse(null);
			ProgressionDataType currentProgress = core.getProgression().getProgress(context.getActor().getUUID(), reqs.getKey());
			outValue = Math.min(outValue, eventGateValue == null ? 1f : currentProgress.compare(Comparison.GREATER_THAN_OR_EQUAL, eventGateValue));
		}
		
		return outValue;
	}

}
