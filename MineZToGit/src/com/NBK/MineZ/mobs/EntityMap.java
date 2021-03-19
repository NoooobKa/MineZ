package com.NBK.MineZ.mobs;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public enum EntityMap {

	ZOMBIE("Zombie", 54, CustomZombie.class) {
		@Override
		public Entity spawn(Location location) {
			World mcworld = (World) ((CraftWorld) location.getWorld()).getHandle();
			final CustomZombie customEntity = new CustomZombie(mcworld);
			customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());		
			mcworld.addEntity(customEntity);
			return customEntity;
		}
	},
	PIG_ZOMBIE("PigZombie", 57, CustomPigZombie.class){
		@Override
		public Entity spawn(Location location) {
			World mcworld = (World) ((CraftWorld) location.getWorld()).getHandle();
			final CustomPigZombie customEntity = new CustomPigZombie(mcworld);
			customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());		
			mcworld.addEntity(customEntity);
			return customEntity;
		}
	},
	GIANT_ZOMBIE("Giant", 53, CustomGiantZombie.class){
		@Override
		public Entity spawn(Location location) {
			World mcworld = (World) ((CraftWorld) location.getWorld()).getHandle();
			final CustomGiantZombie customEntity = new CustomGiantZombie(mcworld);
			customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());		
			mcworld.addEntity(customEntity);
			return customEntity;
		}
	};
	
	private String name;
	private int id;
	private Class<? extends Entity> clazz;
	
	private EntityMap(final String name, final int id, final Class<? extends Entity> clazz) {
		this.name = name;
		this.id = id;
		this.clazz = clazz;
	}
	
	public static void registerAll() {
		for (EntityMap entity : values()) {
			register(entity.clazz, entity.name, entity.id);
		}
	}
	
	public static void register(Class<? extends Entity> entityClass, String name, int id) {
		addToMaps(entityClass, name, id);
	}
	
	@SuppressWarnings("unchecked")
	public static void addToMaps(Class<? extends Entity> entityClass, String name, int id) {
		((Map<String, Class<?>>)getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, entityClass);
        ((Map<Class<? extends Entity>, String>)getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(entityClass, name);
        ((Map<Class<? extends Entity>, Integer>)getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(entityClass, Integer.valueOf(id));
    }
	
	public abstract Entity spawn(Location location);
	
    private static Object getPrivateField( String fieldName,  Class<?> clazz,  PathfinderGoalSelector object) {
        Object o = null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        return o;
    }
	
}
