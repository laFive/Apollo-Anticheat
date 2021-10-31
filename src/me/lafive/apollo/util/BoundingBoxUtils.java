package me.lafive.apollo.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BoundingBoxUtils {
	
	public static boolean isUnderBlockBB(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.maxY; y <= bb.maxY + 0.6; y += 0.1) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType() != Material.AIR) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isOnGround(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.minY - 1; y <= bb.minY; y += 0.1) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType() != Material.AIR) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isOnIce(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.minY - 1; y <= bb.minY; y += 0.1) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType() == Material.ICE) {
						return true;
					}
					if (loc.getBlock().getType() == Material.PACKED_ICE) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isInWeb(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.minY; y <= bb.maxY + 1; y += 0.5) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType() == Material.WEB) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isOnClimbable(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.minY; y <= bb.maxY; y += 0.5) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType() == Material.LADDER) {
						return true;
					}
					if (loc.getBlock().getType() == Material.VINE) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isInLiquid(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.minY - 1; y <= bb.maxY + 1; y += 0.5) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType() == Material.WATER) {
						return true;
					}
					if (loc.getBlock().getType() == Material.LAVA) {
						return true;
					}
					if (loc.getBlock().getType() == Material.STATIONARY_WATER) {
						return true;
					}
					if (loc.getBlock().getType() == Material.STATIONARY_LAVA) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isOnStairs(AxisAlignedBB bb, World world) {
		
		for (double x = bb.minX; x <= bb.maxX; x += 0.1) {
			for (double z = bb.minZ; z <= bb.maxZ; z += 0.1) {
				for (double y = bb.minY - 1; y <= bb.minY; y += 0.1) {
					Location loc = new Location(world, x, y, z);
					if (loc.getBlock().getType().toString().contains("STAIR")) {
						return true;
					}
					if (loc.getBlock().getType().toString().contains("SLAB")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static AxisAlignedBB getBoundingBox(PacketLocation location, double width, double height) {
		
		return new AxisAlignedBB(location.getPosX() - width, location.getPosY(), location.getPosZ() - width, location.getPosX() + width, location.getPosY() + height, location.getPosZ() + width);
		
	}

}
