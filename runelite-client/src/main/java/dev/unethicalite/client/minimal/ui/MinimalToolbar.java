package dev.unethicalite.client.minimal.ui;

import dev.unethicalite.api.plugins.Script;
import dev.unethicalite.client.minimal.config.MinimalConfig;
import dev.unethicalite.client.minimal.config.ConfigPanel;
import dev.unethicalite.client.minimal.config.ConfigurationDescriptor;
import dev.unethicalite.client.minimal.config.DisableRenderCallbacks;
import dev.unethicalite.client.devtools.EntityRenderer;
import dev.unethicalite.client.devtools.scriptinspector.ScriptInspector;
import dev.unethicalite.client.devtools.varinspector.VarInspector;
import dev.unethicalite.client.devtools.widgetinspector.WidgetInspector;
import dev.unethicalite.managers.FpsManager;
import dev.unethicalite.client.minimal.plugins.MinimalPluginManager;
import dev.unethicalite.managers.interaction.InteractionConfig;
import dev.unethicalite.client.minimal.plugins.MinimalPluginChanged;
import dev.unethicalite.client.minimal.plugins.MinimalPluginState;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;

@Singleton
public class MinimalToolbar extends JMenuBar
{
	private static final DisableRenderCallbacks DISABLE_RENDERING = new DisableRenderCallbacks();

	private final VarInspector varInspector;
	private final WidgetInspector widgetInspector;
	private final ScriptInspector scriptInspector;
	private final EntityRenderer entityRenderer;
	private final MinimalPluginManager minimalPluginManager;
	private final MinimalPluginsPanel minimalPluginsPanel;
	private final ConfigManager configManager;
	private final EventBus eventBus;
	private final MinimalConfig minimalConfig;
	private final InteractionConfig interactConfig;
	private final RuneLiteConfig runeLiteConfig;
	private final Client client;
	private final FpsManager fpsManager;

	private JMenuItem pluginConfig;
	private JMenuItem stopPlugin;
	private JMenuItem pauseScript;
	private JMenuItem restartPlugin;
	private JRadioButton rendering;

	private ConfigPanel botConfigPanel;
	private ConfigPanel interactConfigPanel;
	private ConfigPanel clientConfigPanel;

	@Inject
	public MinimalToolbar(VarInspector varInspector, WidgetInspector widgetInspector, ScriptInspector scriptInspector,
						  EntityRenderer entityRenderer, MinimalPluginManager minimalPluginManager, MinimalPluginsPanel minimalPluginsPanel,
						  ConfigManager configManager, EventBus eventBus, MinimalConfig minimalConfig, InteractionConfig interactConfig,
						  RuneLiteConfig runeLiteConfig, Client client, FpsManager fpsManager)
	{
		this.varInspector = varInspector;
		this.widgetInspector = widgetInspector;
		this.scriptInspector = scriptInspector;
		this.entityRenderer = entityRenderer;
		this.minimalPluginManager = minimalPluginManager;
		this.minimalPluginsPanel = minimalPluginsPanel;
		this.configManager = configManager;
		this.eventBus = eventBus;
		this.minimalConfig = minimalConfig;
		this.interactConfig = interactConfig;
		this.runeLiteConfig = runeLiteConfig;
		this.client = client;
		this.fpsManager = fpsManager;
	}

	public void init()
	{
		configManager.setDefaultConfiguration(minimalConfig, false);
		configManager.setDefaultConfiguration(interactConfig, false);
		configManager.setDefaultConfiguration(runeLiteConfig, false);

		ConfigurationDescriptor minimal = new ConfigurationDescriptor(
				"Unethicalite",
				"Unethicalite settings",
				configManager.getConfigDescriptor(minimalConfig)
		);
		botConfigPanel = new ConfigPanel(configManager, eventBus, minimal, client);
		botConfigPanel.init();

		ConfigurationDescriptor interact = new ConfigurationDescriptor(
				"Interact",
				"Interact settings",
				configManager.getConfigDescriptor(interactConfig)
		);
		interactConfigPanel = new ConfigPanel(configManager, eventBus, interact, client);
		interactConfigPanel.init();

		ConfigurationDescriptor cl = new ConfigurationDescriptor(
				"Client",
				"Client settings",
				configManager.getConfigDescriptor(runeLiteConfig)
		);
		clientConfigPanel = new ConfigPanel(configManager, eventBus, cl, client);
		clientConfigPanel.init();

		SwingUtilities.invokeLater(() ->
		{
			JMenuItem scripts = new JMenuItem("Plugins");
			scripts.addActionListener(e ->
			{
				minimalPluginsPanel.open();
			});
			scripts.setMaximumSize(scripts.getPreferredSize());
			add(scripts);

			JMenu settingsMenu = new JMenu("Settings");
			JMenuItem botSettings = new JMenuItem("Unethicalite Settings");
			botSettings.addActionListener(e ->
			{
				botConfigPanel.open();
			});
			settingsMenu.add(botSettings);

			JMenuItem interactSettings = new JMenuItem("Interact Settings");
			interactSettings.addActionListener(e ->
			{
				interactConfigPanel.open();
			});
			settingsMenu.add(interactSettings);

			JMenuItem clientSettings = new JMenuItem("Client Settings");
			clientSettings.addActionListener(e ->
			{
				clientConfigPanel.open();
			});
			settingsMenu.add(clientSettings);
			add(settingsMenu);

			rendering = new JRadioButton("Toggle rendering");
			rendering.addActionListener(e -> configManager.setConfiguration("unethicalite-minimal", "renderOff",
					rendering.isSelected()));
			JMenu debug = new JMenu("Debug");
			rendering.setSelected(configManager.getConfiguration("unethicalite-minimal", "renderOff", Boolean.class));
			add(rendering);

			JRadioButton mouseDebug = new JRadioButton("Mouse debug");
			mouseDebug.addActionListener(e -> configManager.setConfiguration("interaction", "drawMouse", mouseDebug.isSelected()));
			debug.add(mouseDebug);

			JRadioButton menuActionDebug = new JRadioButton("MenuAction debug");
			menuActionDebug.addActionListener(e -> configManager.setConfiguration("interaction", "debugInteractions", menuActionDebug.isSelected()));
			debug.add(menuActionDebug);

			JRadioButton dialogDebug = new JRadioButton("Dialog debug");
			dialogDebug.addActionListener(e -> configManager.setConfiguration("interaction", "debugDialogs", dialogDebug.isSelected()));
			debug.add(dialogDebug);

			JRadioButton collisionDebug = new JRadioButton("Collision map");
			collisionDebug.addActionListener(e -> entityRenderer.setCollisionMap(!entityRenderer.isCollisionMap()));
			debug.add(collisionDebug);

			JRadioButton pathDebug = new JRadioButton("Draw path");
			pathDebug.addActionListener(e -> entityRenderer.setPath(!entityRenderer.isPath()));
			debug.add(pathDebug);

			add(debug);

			JMenu developer = new JMenu("Developer");

			JMenuItem varInspectorItem = new JMenuItem("Var Inspector");
			varInspectorItem.addActionListener(e -> varInspector.open());
			developer.add(varInspectorItem);

			JMenuItem widgetInspectorItem = new JMenuItem("Widget Inspector");
			widgetInspectorItem.addActionListener(e -> widgetInspector.open());
			developer.add(widgetInspectorItem);

			JMenuItem scriptInspectorItem = new JMenuItem("Script Inspector");
			scriptInspectorItem.addActionListener(e -> scriptInspector.open());
			developer.add(scriptInspectorItem);

			JRadioButton gameObjectsBtn = new JRadioButton("Game Objects");
			gameObjectsBtn.addActionListener(e -> entityRenderer.setGameObjects(!entityRenderer.isGameObjects()));
			JRadioButton wallObjectsBtn = new JRadioButton("Wall Objects");
			wallObjectsBtn.addActionListener(e -> entityRenderer.setWallObjects(!entityRenderer.isWallObjects()));
			JRadioButton decorativeObjectsBtn = new JRadioButton("Decorative Objects");
			decorativeObjectsBtn.addActionListener(e -> entityRenderer.setDecorativeObjects(!entityRenderer.isDecorativeObjects()));
			JRadioButton groundObjectsBtn = new JRadioButton("Ground Objects");
			groundObjectsBtn.addActionListener(e -> entityRenderer.setGroundObjects(!entityRenderer.isGroundObjects()));
			JRadioButton npcsBtn = new JRadioButton("NPCs");
			npcsBtn.addActionListener(e -> entityRenderer.setNpcs(!entityRenderer.isNpcs()));
			JRadioButton playersBtn = new JRadioButton("Players");
			playersBtn.addActionListener(e -> entityRenderer.setPlayers(!entityRenderer.isPlayers()));
			JRadioButton tileItemsBtn = new JRadioButton("Tile Items");
			tileItemsBtn.addActionListener(e -> entityRenderer.setTileItems(!entityRenderer.isTileItems()));
			JRadioButton inventoryBtn = new JRadioButton("Inventory");
			inventoryBtn.addActionListener(e -> entityRenderer.setInventory(!entityRenderer.isInventory()));
			JRadioButton projectilesBtn = new JRadioButton("Projectiles");
			projectilesBtn.addActionListener(e -> entityRenderer.setProjectiles(!entityRenderer.isProjectiles()));
			JRadioButton tileLocationBtn = new JRadioButton("Tile Location");
			tileLocationBtn.addActionListener(e -> entityRenderer.setTileLocation(!entityRenderer.isTileLocation()));

			developer.add(gameObjectsBtn);
			developer.add(wallObjectsBtn);
			developer.add(decorativeObjectsBtn);
			developer.add(groundObjectsBtn);
			developer.add(npcsBtn);
			developer.add(playersBtn);
			developer.add(tileItemsBtn);
			developer.add(inventoryBtn);
			developer.add(projectilesBtn);
			developer.add(tileLocationBtn);
			add(developer);

			pluginConfig = new JMenuItem("Plugin config");
			pluginConfig.addActionListener(e ->
			{
				if (minimalPluginManager.getPlugin() != null && minimalPluginManager.getConfig() != null)
				{
					// todo: open/close an (external) config panel
				}
			});
			pluginConfig.setVisible(false);
			pluginConfig.setMaximumSize(pluginConfig.getPreferredSize());
			add(pluginConfig);

			pauseScript = new JMenuItem("Pause script");
			pauseScript.addActionListener(e ->
			{
				if (minimalPluginManager.getPlugin() != null)
				{
					minimalPluginManager.pauseScript();
				}
			});
			pauseScript.setVisible(false);
			pauseScript.setMaximumSize(pauseScript.getPreferredSize());
			add(pauseScript);

			stopPlugin = new JMenuItem("Stop script");
			stopPlugin.addActionListener(e ->
			{
				if (minimalPluginManager.getPlugin() != null)
				{
					minimalPluginManager.stopPlugin();
				}
			});
			stopPlugin.setVisible(false);
			stopPlugin.setMaximumSize(stopPlugin.getPreferredSize());
			add(stopPlugin);

			restartPlugin = new JMenuItem("Restart script");
			restartPlugin.addActionListener(e ->
			{
				if (minimalPluginManager.getPlugin() != null)
				{
					minimalPluginManager.restartPlugin();
				}
			});
			restartPlugin.setVisible(false);
			restartPlugin.setMaximumSize(restartPlugin.getPreferredSize());
			add(restartPlugin);
		});
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("unethicalite-minimal"))
		{
			return;
		}

		switch (event.getKey())
		{
			case "renderOff":
				boolean enabled = Boolean.parseBoolean(event.getNewValue());
				client.setIsHidingEntities(enabled);
				client.setLowCpu(enabled);

				if (enabled)
				{
					client.setDrawCallbacks(DISABLE_RENDERING);
				}
				else
				{
					client.setDrawCallbacks(null);
				}

				if (rendering != null)
				{
					SwingUtilities.invokeLater(() -> rendering.setSelected(Boolean.parseBoolean(event.getNewValue())));
				}

				break;

			case "fpsLimit":
				fpsManager.reloadConfig(minimalConfig.fpsLimit());
				break;
		}

	}

	@Subscribe
	private void onMinimalPluginChanged(MinimalPluginChanged event)
	{
		pluginConfig.setVisible(event.getState() == MinimalPluginState.STARTED || event.getState() == MinimalPluginState.PAUSED);
		stopPlugin.setVisible(event.getState() == MinimalPluginState.STARTED || event.getState() == MinimalPluginState.PAUSED);
		pauseScript.setVisible(event.getPlugin() instanceof Script &&
				(event.getState() == MinimalPluginState.STARTED || event.getState() == MinimalPluginState.PAUSED)
		);
		restartPlugin.setVisible(event.getState() == MinimalPluginState.STARTED || event.getState() == MinimalPluginState.PAUSED);

		if (event.getState() == MinimalPluginState.PAUSED)
		{
			pauseScript.setText("Resume script");
		}
		else
		{
			pauseScript.setText("Pause script");
		}
	}
}
