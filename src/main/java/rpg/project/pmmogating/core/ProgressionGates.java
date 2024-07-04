package rpg.project.pmmogating.core;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import rpg.project.lib.api.Hub;
import rpg.project.lib.api.data.ObjectType;
import rpg.project.lib.api.events.EventContext;
import rpg.project.lib.api.gating.GateSystem;
import rpg.project.lib.api.gating.GateUtils.Type;
import rpg.project.lib.api.progression.ProgressionDataType;
import rpg.project.lib.api.progression.ProgressionDataType.Comparison;
import rpg.project.pmmogating.core.ProgressionConfigType.ProgressionConfig;

public class ProgressionGates implements GateSystem{

	@Override
	public float isActionPermitted(EventContext context, Hub core, ResourceLocation eventID, String container) {
		ResourceLocation objectID = context.subjectObject().getSecond();
		ObjectType type = context.subjectObject().getFirst();
		ProgressionConfig objectGates = (ProgressionConfig) core.getGateData(ProgressionConfigType.IMPL, type, Type.PROGRESS, objectID).orElse(null);
		ProgressionDataType objectGateValue = (ProgressionDataType) (objectGates == null ? null :
				objectGates.reqs().getOrDefault(eventID, List.of()).stream()
				.filter(config -> config.getType().equals(core.getProgression().dataType()))
				.findFirst()
				.orElse(null));
		
		
		ProgressionConfig eventGates = (ProgressionConfig) core.getGateData(ProgressionConfigType.IMPL, ObjectType.EVENT, Type.PROGRESS, eventID).orElse(null);
		ProgressionDataType eventGateValue = (ProgressionDataType) (eventGates == null ? null :
				eventGates.reqs().getOrDefault(eventID, List.of()).stream()
				.filter(config -> config.getType().equals(core.getProgression().dataType()))
				.findFirst()
				.orElse(null));
		
		ProgressionDataType currentProgress = core.getProgression().getProgress(context.getActor().getUUID(), container);
		float objectResult = objectGateValue == null ? 1f :currentProgress.compare(Comparison.GREATER_THAN_OR_EQUAL, objectGateValue);
		float eventResult = eventGateValue == null ? 1f : currentProgress.compare(Comparison.GREATER_THAN_OR_EQUAL, eventGateValue); 
		return Math.min(objectResult, eventResult);
	}

}
