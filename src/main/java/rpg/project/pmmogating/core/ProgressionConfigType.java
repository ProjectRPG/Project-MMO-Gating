package rpg.project.pmmogating.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryManager;
import rpg.project.lib.api.APIUtils;
import rpg.project.lib.api.data.MergeableData;
import rpg.project.lib.api.data.SubSystemConfig;
import rpg.project.lib.api.data.SubSystemConfigType;
import rpg.project.pmmogating.PmmoGating;

public record ProgressionConfigType() implements SubSystemConfigType{
	public static final ResourceLocation ID = new ResourceLocation(PmmoGating.MODID, "progression");
	public static final ProgressionConfigType IMPL = new ProgressionConfigType();

	@Override
	public Codec<SubSystemConfig> getCodec() {
		return ProgressionConfig.CODEC;
	}

	public static record ProgressionConfig(Map<ResourceLocation, List<SubSystemConfig>> reqs) implements SubSystemConfig {
		public static final Codec<SubSystemConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.unboundedMap(ResourceLocation.CODEC, Codec.list(APIUtils.getDispatchCodec().dispatch("type", SubSystemConfig::getType, SubSystemConfigType::getCodec)))
					.fieldOf("events").forGetter(ssc -> ((ProgressionConfig)ssc).reqs())			
				).apply(instance, ProgressionConfig::new));

		@Override
		public MergeableData combine(MergeableData other) {
			ProgressionConfig two = (ProgressionConfig) other;
			Map<ResourceLocation, List<SubSystemConfig>> current = new HashMap<>(this.reqs());
			two.reqs().forEach((rl, list) -> {
				current.merge(rl, list, (o, n) -> {
					return Stream.concat(o.stream(), n.stream()).distinct().toList();
				});
			});
			return new ProgressionConfig(current);
		}

		@Override
		public boolean isUnconfigured() {
			return reqs().entrySet().stream().allMatch(entry -> entry.getValue().isEmpty());
		}

		@Override
		public Codec<SubSystemConfig> getCodec() {
			return CODEC;
		}

		@Override
		public SubSystemConfig getDefault() {
			return new ProgressionConfig(RegistryManager.ACTIVE.getRegistry(APIUtils.GAMEPLAY_EVENTS)
					.getKeys().stream().collect(Collectors.toMap(rl -> rl, rl -> new ArrayList<>())));
		}

		@Override
		public SubSystemConfigType getType() {
			return IMPL;
		}
		
	}
}
