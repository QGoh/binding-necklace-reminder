package com.bindingnecklacereminder;

import java.awt.Color;
import java.awt.Font;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.ui.FontManager;

@ConfigGroup(BindingNecklaceReminderConfig.GROUP)
public interface BindingNecklaceReminderConfig extends Config
{
	String GROUP = "bindingNecklaceReminder";

	@Range(
		max = 16
	)
	@ConfigItem(
		keyName = "chargeThreshold",
		name = "Charge Threshold",
		description = "Draw overlay when binding necklace charges are at or below this value (0 to disable)"
	)
	default int chargeThreshold()
	{
		return 4;
	}

	@ConfigItem(
		keyName = "overlayFont",
		name = "Overlay Font",
		description = "Set the font for the text drawn in the overlay"
	)
	default Fonts overlayFont()
	{
		return Fonts.REGULAR;
	}

	@Alpha
	@ConfigItem(
		keyName = "overlayColour",
		name = "Overlay Colour",
		description = "Set the colour for the overlay background"
	)
	default Color overlayColour()
	{
		return new Color(255, 0, 0, 150);
	}

	@Getter
	@RequiredArgsConstructor
	enum Fonts
	{
		SMALL(FontManager.getRunescapeSmallFont()),
		REGULAR(FontManager.getRunescapeFont()),
		BOLD(FontManager.getRunescapeBoldFont());

		private final Font font;
	}
}
