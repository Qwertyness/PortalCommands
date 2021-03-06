////////////////////////////////////////////////////////////////////////////////////////
// PortalCommands: Bukkit Portal Plugin                                              ///
// Copyright (C) 2013  Qwertyness_                                                   ///
//                                                                                   ///
//     This program is free software: you can redistribute it and/or modify          ///
//    it under the terms of the GNU General Public License as published by           ///
//    the Free Software Foundation, either version 3 of the License, or              ///
//    (at your option) any later version.                                            ///
//                                                                                   ///
//    This program is distributed in the hope that it will be useful,                ///
//    but WITHOUT ANY WARRANTY; without even the implied warranty of                 ///
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                  ///
//    GNU General Public License for more details.                                   ///
//                                                                                   ///
//    You should have received a copy of the GNU General Public License              ///
//   along with this program.  If not, see <http://www.gnu.org/licenses/>.           ///
////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

package com.qwertyness.portalcommands;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.qwertyness.interactables.CooldownUtil;
import com.qwertyness.interactables.Interactables;
import com.qwertyness.interactables.InteractablesPlugin;
import com.qwertyness.portalcommands.command.*;
import com.qwertyness.portalcommands.listener.PortalListener;
import com.qwertyness.portalcommands.portal.Portal;
import com.qwertyness.portalcommands.utils.PortalUtil;
import com.qwertyness.portalcommands.utils.VersionConvertUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class PortalCommands extends JavaPlugin implements InteractablesPlugin {
	private Interactables interactablesPlugin;
	private Plugin worldedit;
	private PortalUtil portalUtil;

	public void onEnable() {
		
		// Register plugin with Interactables API.
		this.interactablesPlugin = this.getServer().getServicesManager().load(Interactables.class);
		this.interactablesPlugin.registerPlugin(this);
		this.getServer().getServicesManager().register(CooldownUtil.class, new CooldownUtil(), this, ServicePriority.Normal);
		this.getServer().getLogger().info("[PortalCommands] Enabling listeners...");
        PortalListener pl = new PortalListener(this);
        this.getServer().getPluginManager().registerEvents(pl, this);
        
		worldedit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldedit == null) {
            this.getServer().getLogger().warning("[PortalCommands] WorldEdit not detected! PortalCommands will not work without WorldEdit!");
        }
        
        
        
        portalUtil = new PortalUtil(this);
        this.getServer().getServicesManager().register(PortalUtil.class, this.portalUtil, this, ServicePriority.Normal);
        
        // Register Commands with Interactables.
        this.interactablesPlugin.getCommandHandler().registerCommand(new Create(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new Delete(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new AddCommand(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new RemoveCommand(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new ListCommands(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new AddMessage(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new RemoveMessage(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new ListMessages(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new SetCooldown(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new SetUses(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new Info(this));
        this.interactablesPlugin.getCommandHandler().registerCommand(new com.qwertyness.portalcommands.command.List(this));
        
        this.getServer().getLogger().info("Running version converter. Converting necessary ConfiguationSections...");
        new VersionConvertUtil(this).run();
        
	    try {
	    	List<String> portals = new ArrayList<String>(this.interactablesPlugin.dataFiles.get(this).get().getConfigurationSection("Portals").getKeys(false));
	    	for (String portal : portals) {
	        	this.interactablesPlugin.getInteractableManager().registerInteractable(new Portal(portal, this));
	        }
	    } catch(NullPointerException e){}
	}
	
    public WorldEditPlugin getWorldEdit() {
    	if (worldedit instanceof WorldEditPlugin) {
            return (WorldEditPlugin) worldedit;
        } else {
        	this.getServer().getLogger().warning("[CommandPortals] WorldEdit detection failed! CommandPortals will not work without WorldEdit!");
        	return null;
        }
    }
    
    public PortalUtil getPortalUtil() {
    	return this.portalUtil;
    }
	public String getPluginDescription() {
		return this.getDescription().getDescription();
	}
	public String getVersion() {
		return this.getDescription().getVersion();
	}
	public Interactables getInteractablesAPI() {
		return this.interactablesPlugin;
	}
}
