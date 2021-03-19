package com.NBK.MineZ.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class UTFConfig extends YamlConfiguration {

    public UTFConfig(File file) {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load Configuration " + file.getName(), e);
        }
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File can't be null");
        Files.createParentDirs(file);
        String data = this.saveToString();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);

        try {
            writer.write(data);
        } finally {writer.close();}
    }

    @Override
    public String saveToString() {
        try {
            Field optionField = Reflections.getField(getClass(), "yamlOptions");
            Field representerField = Reflections.getField(getClass(), "yamlRepresenter");
            Field yamlField = Reflections.getField(getClass(), "yaml");

            if(optionField != null && representerField != null && yamlField != null) {
                optionField.setAccessible(true);
                representerField.setAccessible(true);
                yamlField.setAccessible(true);

                DumperOptions yamlOptions = (DumperOptions) optionField.get(this);
                Representer yamlRepresenter = (Representer) representerField.get(this);
                Yaml yaml = (Yaml) yamlField.get(this);
                DumperOptions.FlowStyle flow = DumperOptions.FlowStyle.BLOCK;

                yamlOptions.setIndent(this.options().indent());
                yamlOptions.setDefaultFlowStyle(flow);
                yamlOptions.setAllowUnicode(true);
                yamlRepresenter.setDefaultFlowStyle(flow);

                String header = this.buildHeader();
                String dump = yaml.dump(this.getValues(false));

                if(dump.equals("{}\n"))dump = "";
                return header + dump;
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error in converting Configuration to String", e);
        }
        return "Error: Cannot be saved to String";
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File can't be null");
        this.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
    }

    public static class Reflections {

        public static final Field modifiers = getField( Field.class, "modifiers" );

        public Reflections() {
            setAccessible( true, modifiers );
        }

        public Class< ? > getNMSClass( String name ) {
            String version = Bukkit.getServer().getClass().getPackage().getName().split( "\\." )[ 3 ];
            try {
                return Class.forName( "net.minecraft.server." + version + "." + name );
            } catch ( Exception e ) {
                return null;
            }
        }

        public Class< ? > getBukkitClass( String name ) {
            try {
                String version = Bukkit.getServer().getClass().getPackage().getName().split( "\\." )[ 3 ];
                return Class.forName( "org.bukkit.craftbukkit." + version + "." + name );
            } catch( Exception ex ) {
                return null;
            }
        }

        public void sendPacket( Player to, Object packet ) {
            try {
                Object playerHandle = to.getClass().getMethod( "getHandle" ).invoke( to );
                Object playerConnection = playerHandle.getClass().getField( "playerConnection" ).get( playerHandle );
                playerConnection.getClass().getMethod( "sendPacket", getNMSClass( "Packet" ) ).invoke( playerConnection, packet );
            } catch ( Exception e ) {
                Bukkit.getLogger().log(Level.INFO, "Could not send Packet to Player " + to.getName(), e);
            }
        }

        public void setField( Object change, String name, Object to ) {
            try {
                Field field = getField( change.getClass(), name );
                if(field != null) {
                    setAccessible( true, field );
                    field.set( change, to );
                    setAccessible( false, field);
                }
            } catch( Exception ex ) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not set Value " + to.getClass().getName() + " in Field " + name + " in Class " + change.getClass().getName(), ex);
            }
        }

        public static void setAccessible( boolean state, Field... fields ) {
            try {
                for( Field field : fields ) {
                    field.setAccessible( state );
                    if( Modifier.isFinal( field.getModifiers() ) ) {
                        field.setAccessible(true);
                        modifiers.set( field, field.getModifiers() & ~Modifier.FINAL );
                    }
                }
            } catch( Exception ex ) {
                Bukkit.getLogger().log(Level.WARNING, "Could not set Fields accessible", ex);
            }
        }

        public static Field getField( Class< ? > clazz, String name ) {
            Field field = null;
            for( Field f : getFields( clazz ) ) {
                if(f.getName().equals( name )) field = f;
            }
            return field;
        }

        public static List< Field > getFields( Class< ? > clazz ) {
            List< Field > buf = new ArrayList<>();

            do {
                try {
                    for( Field f : clazz.getDeclaredFields() )
                      buf.add( f );
                } catch( Exception ex ) {}
            } while( ( clazz = clazz.getSuperclass() ) != null );

            return buf;
        }

        public String getVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().split( "\\." )[ 3 ];
        }
    }
}
