package org.eclipse.ui.internal.registry;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.*;

import org.eclipse.core.runtime.Platform;

/**
 * Provides access to a list of accelerator configurations, a list
 * of accelerator scopes, and a list of accelerator sets.
 */
public class AcceleratorRegistry {
	private List configurations;
	private List scopes;
	private List sets;
	private HashMap idToScope;
	
	public AcceleratorRegistry() {
		configurations = new ArrayList();
		scopes = new ArrayList();
		sets = new ArrayList();		
	}

	/**
	 * Adds the given accelerator configuration to the registry.
	 */	
	public boolean addConfiguration(AcceleratorConfiguration a) {
		return configurations.add(a);	
	}
	/**
	 * Returns all registered configurations.
	 */
	public AcceleratorConfiguration[] getConfigurations() {
		AcceleratorConfiguration[] result = new AcceleratorConfiguration[configurations.size()];
		configurations.toArray(result);
		return result;
	}
	/**
	 * Returns all registered configurations.
	 */
	public AcceleratorConfiguration getConfiguration(String id) {
		for (Iterator iterator = configurations.iterator(); iterator.hasNext();) {
			AcceleratorConfiguration element = (AcceleratorConfiguration)iterator.next();
			if(element.getId().equals(id))
				return element;
		}
		return null;
	}	
	/**
	 * Adds the given accelerator scope to the registry.
	 */
	public boolean addScope(AcceleratorScope a) {
		return scopes.add(a);	
	}
	/**
	 * Returns all registered scopes.
	 */
	public AcceleratorScope[] getScopes() {
		AcceleratorScope[] result = new AcceleratorScope[scopes.size()];
		scopes.toArray(result);
		return result;
	}
	/**
	 * Adds the given accelerator set to the registry.
	 */	
	public boolean addSet(AcceleratorSet a) {
		return sets.add(a);
	}	
	/**
	 * Loads the accelerator registry from the platform's
	 * plugin registry.
	 */
	public void load() {
		AcceleratorRegistryReader reader = 
			new AcceleratorRegistryReader();
		reader.read(Platform.getPluginRegistry(), this);
	}
	/**
	 * Queries the given accelerator configuration and scope to find accelerators
	 * which belong to both. Returns a mapping between action definition ids and
	 * accelerator keys representing these accelerators.
	 * 
	 * @param configId the accelerator configuration to be queried 
	 * @param scopeId the accelerator scope to be queried
	 */
	public Accelerator[] getAccelerators(String configId, String scopeId) {
		List accelarators = new ArrayList();
		for(int i=0;i<sets.size();i++) {
			AcceleratorSet set = (AcceleratorSet)(sets.get(i));
			String setConfigId = set.getConfigurationId();
			String setScopeId = set.getScopeId();
			if(configId.equals(setConfigId) && setScopeId.equals(setScopeId)) {
				accelarators.addAll(Arrays.asList(set.getAccelerators()));
			}
		}
		Accelerator[] result = new Accelerator[accelarators.size()];
		accelarators.toArray(result);
		return result;
	}
	
	public AcceleratorScope getScope(String scopeID) {
		if(idToScope == null) {
			idToScope = new HashMap();
			AcceleratorScope scopes[] = getScopes();
			for (int i = 0; i < scopes.length; i++) {
				AcceleratorScope s = scopes[i];
				idToScope.put(s.getId(),s);
			}
		}
		return (AcceleratorScope)idToScope.get(scopeID);
	}
}
