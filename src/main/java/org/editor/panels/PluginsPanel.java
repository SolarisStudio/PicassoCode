package org.editor.panels;

import com.piccode.piccodeplugin.PiccodePluginPanel;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.jar.JarFile;
import org.editor.EditorWindow;
import org.editor.theme.ThemeManager;

/**
 *
 * @author hexaredecimal
 */
public class PluginsPanel extends DockablePanel {

	private final HashMap<File, URLClassLoader> loaders = new HashMap<>();

	public PluginsPanel() {
		super(new BorderLayout(), "Plugins", "Plugins", "Browse community plugins", "plugin");
		loadPlugins();
	}

	private void loadPlugins() {
		var pluginsDir = Paths.get("./etc/plugins/");
		try {
			Class<?> baseClass = Class.forName("com.piccode.piccodeplugin.PiccodePluginPanel");
			Files.walk(pluginsDir)
							.filter(p -> p.toString().endsWith(".jar"))
							.forEach(jarPath -> {
								try {
									scanJar(jarPath.toFile(), baseClass);
								} catch (Exception e) {
									e.printStackTrace();
								}
							});
		} catch (IOException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void loadPlugin(Class<?> clazz) {
		System.out.println("Loading plugin: " + clazz.getName());
		try {
			var instance = (PiccodePluginPanel) clazz.getDeclaredConstructor().newInstance();
			instance.init();
			instance = instance.getMainPanel();
			var name = instance.getPluginName();
			var title = "Plugin: " + name;
			var dockable = new DockablePanel(new BorderLayout(), name, title, instance.getDescription(), "plugin");
			dockable.add(instance, BorderLayout.CENTER);
			ThemeManager.registerPlugin(instance);
			var desk = EditorWindow.desk;
			desk.addDockable(dockable);
			desk.setAutoHide(dockable, true);
			ThemeManager.updateThemes(EditorWindow.dark);

		} catch (Exception ex) {
			System.out.println("" + ex.getMessage());
		}
	}

	private void scanJar(File jarFile, Class<?> baseClass) throws IOException {
		URL[] urls = {jarFile.toURI().toURL()};
		// Use system classloader as parent to share core and app classes
		URLClassLoader cl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
		loaders.put(jarFile, cl);  // keep the loader alive

		try (JarFile jar = new JarFile(jarFile)) {
			jar.stream()
							.filter(e -> e.getName().endsWith(".class")
							&& !e.getName().equals("module-info.class")
							&& !e.getName().contains("$")
							&& !e.getName().contains("META-INF/")
							&& !e.getName().endsWith("package-info.class"))
							.forEach(e -> {
								String className = e.getName().replace('/', '.').replaceAll("\\.class$", "");
								try {
									// Try loading from app classloader first (fallback pattern)
									Class<?> clazz;
									try {
										clazz = Class.forName(className);
									} catch (ClassNotFoundException ex) {
										clazz = cl.loadClass(className);
									}
									if (baseClass.isAssignableFrom(clazz) && !clazz.equals(baseClass)) {
										loadPlugin(clazz);
									}
								} catch (Exception ignore) {
									System.out.println("Failed to load class " + className + ": " + ignore.getMessage());
								}
							});
		}
	}

}
