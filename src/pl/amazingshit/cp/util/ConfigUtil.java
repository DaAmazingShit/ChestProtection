package pl.amazingshit.cp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.util.config.ConfigurationException;
import org.bukkit.util.config.ConfigurationNode;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;
/**
 * Helps with configuration; Actually, it is a configuration
 */
public class ConfigUtil extends ConfigurationNode {

	private String header = null;
	private File file;
	private Yaml yaml;

	public ConfigUtil(File config) {
		super((new HashMap<String, Object>()));
		this.file = config;
		// Code borrowed from Bukkit (https://github.com/Bukkit/Bukkit/blob/46d4e2009340044aca64683bb9a376288e8fabb8/src/main/java/org/bukkit/util/config/Configuration.java)
		DumperOptions options = new DumperOptions();
		
		options.setIndent(4);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		
		yaml = new Yaml(new SafeConstructor(), new EmptyNullRepresenter(), options);
		// End of borrowed code
	}

	// Code borrowed from Bukkit (https://github.com/Bukkit/Bukkit/blob/914da8e887deaadb57be339aa2dbb78eca2742a4/src/main/java/org/bukkit/util/config/ConfigurationNode.java)
    /**
     * Gets all of the cofiguration values within the Node as
     * a key value pair, with the key being the full path and the
     * value being the Object that is at the path.
     *
     * @return A map of key value pairs with the path as the key and the object as the value
     */
    public Map<String, Object> getAll() {
        return recursiveBuilder(root);
    }

    /**
     * A helper method for the getAll method that deals with the recursion
     * involved in traversing the tree
     *
     * @param node The map for that node of the tree
     * @return The fully pathed map for that point in the tree, with the path as the key
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> recursiveBuilder(Map<String, Object> node) {
        Map<String, Object> map = new TreeMap<String, Object>();

        Set<String> keys = node.keySet();
        for( String k : keys ) {
            Object tmp = node.get(k);
            if( tmp instanceof Map<?,?> ) {
                Map<String, Object> rec = recursiveBuilder((Map <String,Object>) tmp);

                Set<String> subkeys = rec.keySet();
                for( String sk : subkeys ) {
                    map.put(k + "." + sk, rec.get(sk));
                }
            }
            else {
                map.put(k, tmp);
            }
        }
        return map;
    }

    /**
     * Returns a list of all keys at the root path
     *
     * @return List of keys
     */
    public List<String> getKeys() {
        return new ArrayList<String>(root.keySet());
    }
    // End of borrowed code

    // Code borrowed from Bukkit (https://github.com/Bukkit/Bukkit/blob/46d4e2009340044aca64683bb9a376288e8fabb8/src/main/java/org/bukkit/util/config/Configuration.java)
    /**
     * Loads the configuration file. All errors are thrown away.
     */
    public void load() {
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(file);
            read(yaml.load(new UnicodeReader(stream)));
        } catch (IOException e) {
            root = new HashMap<String, Object>();
        } catch (ConfigurationException e) {
            root = new HashMap<String, Object>();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {}
        }
    }

    @SuppressWarnings("unchecked")
    private void read(Object input) throws ConfigurationException {
        try {
            if (null == input) {
                root = new HashMap<String, Object>();
            } else {
                root = (Map<String, Object>) input;
            }
        } catch (ClassCastException e) {
            throw new ConfigurationException("Root document must be an key-value structure");
        }
    }

    /**
     * Set the header for the file as a series of lines that are terminated
     * by a new line sequence.
     * 
     * @param headerLines header lines to prepend
     */
    public void setHeader(String ... headerLines)  {
        StringBuilder header = new StringBuilder();
        
        for (String line : headerLines) {
            if (header.length() > 0) {
                header.append("\r\n");
            }
            header.append(line);
        }
        setHeader(header.toString());
    }

    /**
     * Set the header for the file. A header can be provided to prepend the
     * YAML data output on configuration save. The header is 
     * printed raw and so must be manually commented if used. A new line will
     * be appended after the header, however, if a header is provided.
     * 
     * @param header header to prepend
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Return the set header.
     * 
     * @return
     */
    public String getHeader() {
        return header;
    }

    public boolean save() {
        FileOutputStream stream = null;
        
        File parent = file.getParentFile();
        
        if (parent != null) {
            parent.mkdirs();
        }
        
        try {
            stream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
            if (header != null) {
                writer.append(header);
                writer.append("\r\n");
            }
            yaml.dump(root, writer);
            return true;
        } catch (IOException e) {} finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {}
        }
        return false;
    }

    class EmptyNullRepresenter extends Representer {

        public EmptyNullRepresenter() {
            super();
            this.nullRepresenter = new EmptyRepresentNull();
        }

        protected class EmptyRepresentNull implements Represent {
            public Node representData(Object data) {
                return representScalar(Tag.NULL, ""); // Changed "null" to "" so as to avoid writing nulls
            }
        }
    }
    // End of borrowed code
}
