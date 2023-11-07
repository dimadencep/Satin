/*
 * Satin
 * Copyright (C) 2019-2022 Ladysnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package ladysnake.satintestcore.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DebugItem extends Item {
    private int debugMode;
    private static final List<DebugCallback> callbacks = new ArrayList<>();

    public DebugItem(Settings settings) {
        super(settings);
    }

    public static void registerDebugCallback(DebugCallback callback) {
        DebugItem.callbacks.add(callback);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking() && !world.isClient) {
            if (DebugItem.callbacks.size() > 1) {
                debugMode = (debugMode + 1) % DebugItem.callbacks.size();
                player.sendMessage(new TranslatableText("Switched mode to %s", debugMode), true);
            }
        } else {
            DebugItem.callbacks.get(debugMode).use(world, player, hand);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack itemStack_1) {
        return true;
    }
}