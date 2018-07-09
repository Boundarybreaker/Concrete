/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2018:
 * 	Una Thompson (unascribed),
 * 	Isaac Ellingson (Falkreon),
 * 	Jamie Mansfield (jamierocks),
 * 	Alex Ponebshek (capitalthree),
 * 	and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elytradev.concrete.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;

import java.util.UUID;

public class ConcreteHeldItemStorage extends ConcreteItemStorage implements IItemHandler {
	public ItemStack parentStack;
	public NBTTagCompound tag;

	public ConcreteHeldItemStorage(int slots, ItemStack stack) {
		super(slots);

		parentStack = stack;
	}

	public void onGuiSaved(EntityPlayer player) {
		parentStack = findParentStack(player);
		if (parentStack != null) {
			save();
		}
	}

	public ItemStack findParentStack(EntityPlayer player) {
		if (hasUUID(parentStack)) {
			UUID parentStackID = parentStack.getTagCompound().getUniqueId("ItemID");

			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);

				if (hasUUID(stack)) {
					if (stack.getTagCompound().getUniqueId("ItemID").equals(parentStackID)) {
						return stack;
					}
				}
			}
		}
		return null;
	}

	public void save() {
		NBTTagCompound compound = parentStack.getTagCompound();

		if (compound == null) {
			compound = new NBTTagCompound();

			compound.setUniqueId("ItemID", UUID.randomUUID());
		}

		parentStack.setTagCompound(compound);
	}

	public boolean hasUUID(ItemStack stack) {
		if (!stack.hasTagCompound()) return false;
		return stack.getTagCompound().hasKey("ItemIDMost") && stack.getTagCompound().hasKey("ItemIDLeast");
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

}