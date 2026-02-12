package com.bindingnecklacereminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BindingNecklaceReminderTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BindingNecklaceReminderPlugin.class);
		RuneLite.main(args);
	}
}