package rpg.project.pmmogating.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import rpg.project.lib.api.APIUtils;
import rpg.project.lib.api.data.MergeableData;
import rpg.project.lib.api.data.SubSystemConfig;
import rpg.project.lib.api.data.SubSystemConfigType;
import rpg.project.pmmogating.PmmoGating;

public record EventConfigType() implements SubSystemConfigType{
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(PmmoGating.MODID, "event");
	public static final EventConfigType IMPL = new EventConfigType();

	@Override
	public MapCodec<SubSystemConfig> getCodec() {
		return EventConfig.CODEC;
	}

	public static record EventConfig(Map<String, List<SubSystemConfig>> reqs) implements SubSystemConfig {
		public static final MapCodec<SubSystemConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.unboundedMap(Codec.STRING, Codec.list(APIUtils.getDispatchCodec().dispatch("type", SubSystemConfig::getType, SubSystemConfigType::getCodec)))
					.fieldOf("requirements")
					.forGetter(ssc -> ((EventConfig)ssc).reqs())
				).apply(instance, EventConfig::new));

		@Override
		public MergeableData combine(MergeableData other) {
			EventConfig two = (EventConfig) other;
			Map<String, List<SubSystemConfig>> current = new HashMap<>(this.reqs());
			two.reqs().forEach((contiainer, list) -> {
				current.merge(contiainer, list, (o, n) -> {
					return Stream.concat(o.stream(), n.stream()).distinct().toList();
				});
			});
			return new EventConfig(current);
		}

		@Override
		public boolean isUnconfigured() {
			return reqs().entrySet().stream().allMatch(entry -> entry.getValue().isEmpty());
		}

		@Override
		public MapCodec<SubSystemConfig> getCodec() {
			return CODEC;
		}

		@Override
		public SubSystemConfig getDefault() {
			return new EventConfig(new HashMap<>());
		}

		@Override
		public SubSystemConfigType getType() {
			return IMPL;
		}
		
	}
}
