 package com.NBK.MineZ.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.NBK.MineZ.util.CustomStack;
import com.NBK.MineZ.util.Util;

public class StackSizeListener implements Listener{

	private Plugin p;
	
	public StackSizeListener(Plugin p) {
		this.p = p;
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInvClick(InventoryClickEvent e) {
		if (clickValidate(e))return;
		Player p = (Player) e.getWhoClicked();
		if (p.getGameMode() == GameMode.SURVIVAL && (e.getCurrentItem().getType() != Material.AIR || e.getCursor().getType() != Material.AIR)) {
			ItemStack currentItem = e.getCurrentItem();
			ItemStack cursorItem = e.getCursor();
			ClickType c = e.getClick();
			if (CustomizedStackItems.nullableValueOf(currentItem.getType() != Material.AIR ? currentItem.getType().toString() : cursorItem.getType().toString()) != null) {
				InventoryAction action = e.getAction();
				if (action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_SOME || action == InventoryAction.PLACE_ONE) {
					if (action == InventoryAction.PLACE_ONE) {
						if (currentItem.getType() != Material.AIR) {
							CustomizedStackItems csi = CustomizedStackItems.valueOf(currentItem.getType().toString());
							if (currentItem.getAmount() + 1 > csi.getStackSize()) {
								e.setCancelled(true);
							}
						}
						return;
					}
					e.setCancelled(true);
					if (currentItem.getType() != Material.AIR) {
						CustomizedStackItems csi = CustomizedStackItems.valueOf(currentItem.getType().toString());
						if (currentItem.getAmount() < csi.getStackSize()) {
							if (currentItem.getAmount() + cursorItem.getAmount() <= csi.getStackSize()) {
								currentItem.setAmount(currentItem.getAmount() + cursorItem.getAmount());
								e.setCurrentItem(currentItem);
								e.setCursor(null);
							}else {
								cursorItem.setAmount(cursorItem.getAmount() - csi.getStackSize() + currentItem.getAmount());
								currentItem.setAmount(csi.getStackSize());
								e.setCurrentItem(currentItem);
								e.setCursor(cursorItem);
							}
						}
					}else {
						CustomizedStackItems csi = CustomizedStackItems.valueOf(cursorItem.getType().toString());
						if (cursorItem.getAmount() < csi.getStackSize()) {
							e.setCurrentItem(cursorItem);
							e.setCursor(null);
						}else {
							int newCursorAmount = cursorItem.getAmount() - csi.getStackSize();
							cursorItem.setAmount(csi.getStackSize());
							e.setCurrentItem(cursorItem);
							cursorItem.setAmount(newCursorAmount);
							e.setCursor(cursorItem);
						}
					}
				}
				if (c == ClickType.SHIFT_RIGHT || c == ClickType.SHIFT_LEFT) {
					Inventory tInv = e.getView().getTopInventory();
					Inventory bInv = e.getView().getBottomInventory();
					CustomizedStackItems csi = CustomizedStackItems.valueOf(currentItem.getType().toString());
					e.setCancelled(true);
					if (tInv.getType() == InventoryType.CRAFTING && bInv.getType() == InventoryType.PLAYER) {
						Inventory pInv = e.getWhoClicked().getInventory();
						if (e.getSlotType() == SlotType.QUICKBAR || e.getSlotType() == SlotType.CONTAINER) {
							HashMap<Integer, ? extends ItemStack> all = e.getSlotType() == SlotType.QUICKBAR ? Util.allSimilarWithStartIndex(currentItem, pInv, 9) : Util.allSimilarWithEndIndex(currentItem, pInv, 9);
							if (all.size() > 0) {
								for (int index : all.keySet()) {
									ItemStack item = all.get(index);
									if (item.getAmount() < csi.getStackSize()) {
										if (item.getAmount() + currentItem.getAmount() > csi.getStackSize()) {
											currentItem.setAmount(currentItem.getAmount() - csi.getStackSize() + item.getAmount());
											item.setAmount(csi.getStackSize());
											pInv.setItem(index, item);
										}else {
											item.setAmount(item.getAmount() + currentItem.getAmount());
											pInv.setItem(index, item);
											e.setCurrentItem(null);
											break;
										}
									}
								}
							}
							if (e.getCurrentItem().getType() != Material.AIR && currentItem.getAmount() > 0) {
								do {
									if ((e.getSlotType() == SlotType.QUICKBAR ? Util.firstEmptyWithStartIndex(pInv, 9) : Util.firstEmptyWithEndIndex(pInv, 9)) != -1) {
										if (currentItem.getAmount() <= csi.getStackSize()) {
											pInv.setItem((e.getSlotType() == SlotType.QUICKBAR ? Util.firstEmptyWithStartIndex(pInv, 9) : Util.firstEmptyWithEndIndex(pInv, 9)), currentItem);
											e.setCurrentItem(null);
											break;
										}else {
											pInv.setItem((e.getSlotType() == SlotType.QUICKBAR ? Util.firstEmptyWithStartIndex(pInv, 9) : Util.firstEmptyWithEndIndex(pInv, 9)), csi.getCustomStack().setAmount(csi.getStackSize()).getItemStack());
											currentItem.setAmount(currentItem.getAmount() - csi.getStackSize());
											e.setCurrentItem(currentItem);
										}
									}
								} while ((e.getSlotType() == SlotType.QUICKBAR ? Util.firstEmptyWithStartIndex(pInv, 9) : Util.firstEmptyWithEndIndex(pInv, 9)) != -1);
							}
						}
					}
					if (bInv.getType() == InventoryType.PLAYER && tInv.getType() == InventoryType.CHEST) {
						InventoryType cType = e.getClickedInventory().getType();
						HashMap<Integer, ? extends ItemStack> all = cType == InventoryType.PLAYER ? Util.allSimilar(e.getCurrentItem(), tInv) : Util.allSimilar(e.getCurrentItem(), bInv);
						if (all.size() > 0) {
							for (int index : all.keySet()) {
								ItemStack item = all.get(index);
								if (item.getAmount() < csi.getStackSize()) {
									if (item.getAmount() + currentItem.getAmount() > csi.getStackSize()) {
										currentItem.setAmount(currentItem.getAmount() - csi.getStackSize() + item.getAmount());
										item.setAmount(csi.getStackSize());
										(cType == InventoryType.PLAYER ? tInv : bInv).setItem(index, item);
									}else {
										item.setAmount(item.getAmount() + currentItem.getAmount());
										(cType == InventoryType.PLAYER ? tInv : bInv).setItem(index, item);
										e.setCurrentItem(null);
										break;
									}
								}
							}
						}
						if (e.getCurrentItem().getType() != Material.AIR && currentItem.getAmount() > 0) {
							do {
								if ((cType == InventoryType.PLAYER ? tInv.firstEmpty() : bInv.firstEmpty()) != -1) {
									if (currentItem.getAmount() <= csi.getStackSize()) {
										(cType == InventoryType.PLAYER ? tInv : bInv).setItem((cType == InventoryType.PLAYER ? tInv.firstEmpty() : bInv.firstEmpty()), currentItem);
										e.setCurrentItem(null);
										break;
									}else {
										(cType == InventoryType.PLAYER ? tInv : bInv).setItem((cType == InventoryType.PLAYER ? tInv.firstEmpty() : bInv.firstEmpty()), csi.getCustomStack().setAmount(csi.getStackSize()).getItemStack());
										currentItem.setAmount(currentItem.getAmount() - csi.getStackSize());
										e.setCurrentItem(currentItem);
									}
								}
							} while ((cType == InventoryType.PLAYER ? tInv.firstEmpty() : bInv.firstEmpty()) != -1);
						}
					}
					if (bInv.getType() == InventoryType.PLAYER && tInv.getType() == InventoryType.WORKBENCH) {
						if (e.getSlotType() == SlotType.RESULT) {
	                        final HashMap<Integer, ? extends ItemStack> all = Util.allSimilar(e.getCurrentItem(), bInv);
	                        int free = 0;
	                        for (final ItemStack item : all.values()) {
	                            if (item.getAmount() < csi.getStackSize()) {
	                                free += csi.getStackSize() - item.getAmount();
	                            }
	                        }
	                        if (free > csi.getStackSize()) {
	                            for (final int index : all.keySet()) {
	                                final ItemStack item = (ItemStack)all.get(index);
	                                if (item.getAmount() < csi.getStackSize()) {
	                                    if (item.getAmount() + currentItem.getAmount() <= csi.getStackSize()) {
	                                        item.setAmount(item.getAmount() + currentItem.getAmount());
	                                        bInv.setItem(index, item);
	                                        e.setCurrentItem(null);
	                                        for (int i = 1; i < 9; ++i) {
	                                            final ItemStack is = tInv.getItem(i);
	                                            if (is != null) {
	                                                tInv.setItem(i, (is.getAmount() > 1) ? new CustomStack(is).setAmount(is.getAmount() - 1).getItemStack() : null);
	                                            }
	                                        }
	                                        break;
	                                    }
	                                    currentItem.setAmount(currentItem.getAmount() - csi.getStackSize() + item.getAmount());
	                                    item.setAmount(csi.getStackSize());
	                                    bInv.setItem(index, item);
	                                }
	                            }
	                        }
	                        if (bInv.firstEmpty() != -1) {
	                            bInv.setItem(bInv.firstEmpty(), csi.getCustomStack().setAmount(csi.getStackSize()).getItemStack());
	                            e.setCurrentItem(null);
	                            for (int j = 1; j < 9; ++j) {
	                                final ItemStack is = tInv.getItem(j);
	                                if (is != null) {
	                                    tInv.setItem(j, (is.getAmount() > 1) ? new CustomStack(is).setAmount(is.getAmount() - 1).getItemStack() : null);
	                                }
	                            }
	                        }
						}
					}
					if (bInv.getType() == InventoryType.PLAYER && tInv.getType() == InventoryType.BREWING) {
						if (e.getSlotType() == SlotType.FUEL) {
							HashMap<Integer, ? extends ItemStack> all = Util.allSimilar(e.getCurrentItem(), bInv);
							if (all.size() > 0) {
								for (int index : all.keySet()) {
									ItemStack item = all.get(index);
									if (item.getAmount() < csi.getStackSize()) {
										if (item.getAmount() + currentItem.getAmount() > csi.getStackSize()) {
											currentItem.setAmount(currentItem.getAmount() - csi.getStackSize() + item.getAmount());
											item.setAmount(csi.getStackSize());
											bInv.setItem(index, item);
										}else {
											item.setAmount(item.getAmount() + currentItem.getAmount());
											bInv.setItem(index, item);
											e.setCurrentItem(null);
											break;
										}
									}
								}
							}
							if (e.getCurrentItem().getType() != Material.AIR && currentItem.getAmount() > 0) {
								do {
									if (bInv.firstEmpty() != -1) {
										if (currentItem.getAmount() <= csi.getStackSize()) {
											bInv.setItem(bInv.firstEmpty(), currentItem);
											e.setCurrentItem(null);
											break;
										}else {
											bInv.setItem(bInv.firstEmpty(), csi.getCustomStack().setAmount(csi.getStackSize()).getItemStack());
											currentItem.setAmount(currentItem.getAmount() - csi.getStackSize());
											e.setCurrentItem(currentItem);
										}
									}
								} while (bInv.firstEmpty() != -1);
							}
						}
						if (e.getClickedInventory().getType() == InventoryType.PLAYER && tInv.getItem(3) == null) {
							e.setCancelled(false);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (dragValidate(e))return;
		CustomizedStackItems csi = CustomizedStackItems.nullableValueOf(e.getOldCursor().getType().toString());
		if (csi != null) {
			ItemStack newCursor = new CustomStack(e.getOldCursor()).setAmount(0).getItemStack();
			Map<Integer, ItemStack> newItems = new HashMap<>();
			for (int slot : e.getNewItems().keySet()) {
				ItemStack item = e.getNewItems().get(slot);
				if (item.getAmount() > csi.getStackSize()) {
					newCursor.setAmount(newCursor.getAmount() + item.getAmount() - csi.getStackSize());
					item.setAmount(csi.getStackSize());
					newItems.put(slot, item);
				}
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					if (e.getCursor() != null)newCursor.setAmount(newCursor.getAmount() + e.getCursor().getAmount());
					e.getWhoClicked().setItemOnCursor(newCursor);
					for (int key : newItems.keySet()) {
						e.getView().setItem(key, newItems.get(key));
					}
				}
			}.runTask(p);
		}
	}
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItem().getItemStack();
		CustomizedStackItems csi = CustomizedStackItems.nullableValueOf(item.getType().toString());
		if (csi != null) {
			e.setCancelled(true);
			HashMap<Integer, ItemStack> all = Util.allSimilar(item, p.getInventory());
			if (all.size() > 0) {
				for (int index : all.keySet()) {
					ItemStack is = all.get(index);
					if (is.getAmount() < csi.getStackSize()) {
						if (is.getAmount() + item.getAmount() > csi.getStackSize()) {
							item.setAmount(item.getAmount() - csi.getStackSize() + is.getAmount());
							is.setAmount(csi.getStackSize());
							p.getInventory().setItem(index, is);
						}else {
							is.setAmount(is.getAmount() + item.getAmount());
							p.getInventory().setItem(index, item);
							e.getItem().remove();
							p.playSound(p.getLocation(), "random.pop", 0.2f, ((new Random().nextFloat() - new Random().nextFloat()) * 0.7f + 1.0f) * 2.0f);
							break;
						}
					}
				}
			}
			if (item.getType() != Material.AIR && item.getAmount() > 0) {
				do {
					if (p.getInventory().firstEmpty() != -1) {
						if (item.getAmount() <= csi.getStackSize()) {
							p.getInventory().setItem(p.getInventory().firstEmpty(), item);
							e.getItem().remove();
							p.playSound(p.getLocation(), "random.pop", 0.2f, ((new Random().nextFloat() - new Random().nextFloat()) * 0.7f + 1.0f) * 2.0f);
							break;
						}else {
							p.getInventory().setItem(p.getInventory().firstEmpty(), new CustomStack(item).setAmount(csi.getStackSize()).getItemStack());
							item.setAmount(item.getAmount() - csi.getStackSize());
							e.getItem().setItemStack(item);
						}
					}
				} while (p.getInventory().firstEmpty() != -1);
			}
		}
	}
	
	@EventHandler
	public void onMerge(ItemMergeEvent e) {
		ItemStack whom = e.getEntity().getItemStack();
		ItemStack who = e.getTarget().getItemStack();
		CustomizedStackItems csi = CustomizedStackItems.nullableValueOf(who.getType().toString());
		if (csi != null) {
			if (whom.getAmount() + who.getAmount() > csi.getStackSize()) {
				e.setCancelled(true);
				if (whom.getAmount() < csi.getStackSize()) {
					who.setAmount(who.getAmount() - csi.getStackSize() + whom.getAmount());
					whom.setAmount(csi.getStackSize());
					e.getEntity().setItemStack(who);
					e.getTarget().setItemStack(whom);
				}
			}
		}
	}
	
	private boolean clickValidate(InventoryClickEvent e) {
		return e.getSlot() == -999 || e.getClickedInventory() == null || e.getSlotType() == null || e.getSlotType() == SlotType.ARMOR || (e.getSlotType() == SlotType.RESULT && e.getCurrentItem().getType() == Material.AIR) || e.getClick() == null || e.isCancelled();
	}
	
	private boolean dragValidate(InventoryDragEvent e) {
		return e.getInventorySlots() == null || e.getNewItems() == null || e.getWhoClicked() == null || e.getType() == null;
	}
	
}
