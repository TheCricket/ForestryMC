package forestry.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;

import net.minecraftforge.forgespi.language.ModFileScanData;

import net.minecraftforge.fml.ModList;

import forestry.api.modules.ForestryModule;
import forestry.api.modules.IForestryModule;
import forestry.core.utils.Log;
import forestry.modules.features.FeatureProvider;

import org.objectweb.asm.Type;

public class ForestryPluginUtil {
	private ForestryPluginUtil() {

	}

	public static Map<String, List<IForestryModule>> getForestryModules() {
		List<IForestryModule> instances = getInstances(ForestryModule.class, IForestryModule.class);
		Map<String, List<IForestryModule>> modules = new LinkedHashMap<>();
		for (IForestryModule module : instances) {
			ForestryModule info = module.getClass().getAnnotation(ForestryModule.class);
			modules.computeIfAbsent(info.containerID(), k -> new ArrayList<>()).add(module);
		}
		return modules;
	}

	public static void loadFeatureProviders() {
		Type annotationType = Type.getType(FeatureProvider.class);
		List<ModFileScanData> allScanData = ModList.get().getAllScanData();
		Set<String> pluginClassNames = new HashSet<>();

		for (ModFileScanData scanData : allScanData) {
			Set<ModFileScanData.AnnotationData> annotationData = scanData.getAnnotations();
			for (ModFileScanData.AnnotationData data : annotationData) {
				if (Objects.equals(data.annotationType(), annotationType)) {
					pluginClassNames.add(data.memberName());
				}
			}
		}
		for (String className : pluginClassNames) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException | ExceptionInInitializerError e) {
				Log.error("Failed to load: {}", className, e);
			}
		}
	}


	public static String getComment(IForestryModule module) {
		ForestryModule info = module.getClass().getAnnotation(ForestryModule.class);

		//TODO - check this is only called on the client. I don't think it is at the moment
		String comment = new TranslatableComponent(info.unlocalizedDescription()).getContents();
		Set<ResourceLocation> dependencies = module.getDependencyUids();
		if (!dependencies.isEmpty()) {
			Iterator<ResourceLocation> iDependencies = dependencies.iterator();

			StringBuilder builder = new StringBuilder(comment);
			builder.append("\n");
			builder.append("Dependencies: [ ");
			builder.append(iDependencies.next());
			while (iDependencies.hasNext()) {
				ResourceLocation uid = iDependencies.next();
				builder.append(", ").append(uid.toString());
			}
			builder.append(" ]");
			comment = builder.toString();
		}
		return comment;
	}

	private static <T> List<T> getInstances(Class annotationClass, Class<T> instanceClass) {
		Type annotationType = Type.getType(annotationClass);

		List<ModFileScanData> allScanData = ModList.get().getAllScanData();
		Set<String> pluginClassNames = new HashSet<>();

		for (ModFileScanData scanData : allScanData) {
			Set<ModFileScanData.AnnotationData> annotationData = scanData.getAnnotations();
			for (ModFileScanData.AnnotationData data : annotationData) {
				if (Objects.equals(data.annotationType(), annotationType)) {
					pluginClassNames.add(data.memberName());
				}
			}
		}

		List<T> instances = new ArrayList<>();
		for (String className : pluginClassNames) {
			try {
				Class<?> asmClass = Class.forName(className);
				Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
				T instance = asmInstanceClass.newInstance();
				instances.add(instance);
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
				Log.error("Failed to load: {}", className, e);
			}
		}
		return instances;
	}
}
