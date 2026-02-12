package com.bindingnecklacereminder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class BindingNecklaceReminderOverlay extends OverlayPanel
{
	private final BindingNecklaceReminderPlugin plugin;
	private final BindingNecklaceReminderConfig config;

	@Inject
	private BindingNecklaceReminderOverlay(Client client, BindingNecklaceReminderPlugin plugin, BindingNecklaceReminderConfig config)
	{
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics2D)
	{
		panelComponent.getChildren().clear();
		panelComponent.getChildren().add(
			TitleComponent.builder()
			.text("Binding Necklace")
			.color(Color.WHITE)
			.build()
		);
		panelComponent.getChildren().add(
			TitleComponent.builder()
			.text("Charges: " + plugin.getCharge())
			.color(Color.WHITE)
			.build()
		);
		panelComponent.setBackgroundColor(config.overlayColour());
		graphics2D.setFont(config.overlayFont().getFont());
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);

		return panelComponent.render(graphics2D);
	}
}
