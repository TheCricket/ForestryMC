package forestry.modules;

import forestry.api.modules.IForestryModule;
import forestry.api.modules.IModuleContainer;
import forestry.core.config.Constants;

import java.util.Collection;

public class ForestryModules implements IModuleContainer {

	@Override
	public String getID() {
		return Constants.MOD_ID;
	}

	@Override
	public void onConfiguredModules(Collection<IForestryModule> activeModules, Collection<IForestryModule> unloadedModules) {
		ModuleManager.getModuleHandler().addModules(activeModules, unloadedModules);
	}
}
