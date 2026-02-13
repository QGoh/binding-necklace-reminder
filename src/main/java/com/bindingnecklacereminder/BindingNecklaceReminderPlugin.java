package com.bindingnecklacereminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Binding Necklace Reminder"
)
public class BindingNecklaceReminderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BindingNecklaceReminderOverlay overlay;

	@Inject
	private BindingNecklaceReminderConfig config;

	@Override
	protected void startUp() throws Exception
	{
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invokeLater(this::checkCharge);
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			clientThread.invokeLater(this::checkCharge);
		}
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.WORN && event.getContainerId() != InventoryID.INV)
		{
			return;
		}

		checkCharge();
	}

	@Subscribe
	private void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == VarPlayerID.NECKLACE_OF_BINDING)
		{
			setCharge(event.getValue());
			checkCharge();
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		String message = Text.removeTags(event.getMessage());
		if (message.contains("Your Binding necklace has disintegrated."))
		{
			setCharge(16);
		}
	}

	private void setCharge(int value)
	{
		configManager.setRSProfileConfiguration(BindingNecklaceReminderConfig.GROUP, "bindingNecklace", value);
	}

	public int getCharge()
	{
		Integer charge = configManager.getRSProfileConfiguration(BindingNecklaceReminderConfig.GROUP, "bindingNecklace", Integer.class);
		return charge == null ? 0 : charge;
	}

	private void checkCharge()
	{
		final ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		final ItemContainer inventory = client.getItemContainer(InventoryID.INV);
		if ((equipment != null && equipment.contains(ItemID.MAGIC_EMERALD_NECKLACE)) ||
			(inventory != null && inventory.contains(ItemID.MAGIC_EMERALD_NECKLACE)))
		{
			int charge = getCharge();

			if (charge <= config.chargeThreshold())
			{
				overlayManager.add(overlay);
			}
			else
			{
				overlayManager.remove(overlay);
			}
		}
		else
		{
			overlayManager.remove(overlay);
		}
	}

	@Provides
	BindingNecklaceReminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BindingNecklaceReminderConfig.class);
	}
}
