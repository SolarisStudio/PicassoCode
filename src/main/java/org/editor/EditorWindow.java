package org.editor;

import org.editor.panels.DockablePanel;
import org.editor.panels.DashboardPanel;
import com.formdev.flatlaf.FlatLightLaf;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.DockingPreferences;
import com.vlsolutions.swing.docking.ui.DockingUISettings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.HashMap;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalIconFactory;
import org.editor.events.Actions;
import org.editor.icons.Icons;
import org.editor.menu.Menus;
import org.editor.panels.FileTreePanel;
import org.editor.panels.PluginsPanel;
import org.editor.panels.VCPanel;
import org.editor.theme.ThemeManager;

import org.fife.rsta.ui.CollapsibleSectionPanel;
//import org.fife.rsta.ui.DocumentMap;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

/**
 *
 * @author hexaredecimal
 */
public final class EditorWindow extends JFrame implements SearchListener {

	private static HashMap<Integer, CodeEditor> tabEditors;
	public static EditorWindow win = null;
	public static JLabel current_file = new JLabel();
	public static JLabel line_info = new JLabel();
	public static JLabel line_perc = new JLabel();
	public static JLabel charset = new JLabel();
	public static JProgressBar seekBar = new JProgressBar();
	private DockablePanel dashboard;

	private CollapsibleSectionPanel csp;
	public static FindDialog findDialog;
	public static ReplaceDialog replaceDialog;
	private DockingDesktop desk = new DockingDesktop();
	private static CodeEditor selected = null;
	private static boolean dark = true;

	public static EditorWindow the() {
		if (win == null) {
			win = new EditorWindow();
		}
		return win;
	}

	public static JRootPane root = null;

	public EditorWindow() {
		super("Piccode - DashBoard");

		DockingUISettings.getInstance().installUI();
		customizeDock();

		UIManager.put("Tree.collapsedIcon", UIManager.getIcon("Tree.collapsedIcon"));
		UIManager.put("Tree.expandedIcon", UIManager.getIcon("Tree.expandedIcon")); 
		
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize LaF");
		}

		new CodeEditor();
		root = getRootPane();
		Icons.loadIcons();
		tabEditors = new HashMap<>();
		CodeEditor.createTemplateManager();
		initSearchDialogs();

		int width = 900;
		int height = 600;

		desk.addDockableStateWillChangeListener(event -> {
			var current = event.getCurrentState();

			if (current == null) {
				return;
			}

			if (current.getDockable() instanceof CodeEditor ed) {
				if (event.getFutureState().isClosed()) {
					if (removeIfNotDirty(ed.tabIndex, ed) == false) {
						event.cancel();
					}
				}
			}
		});

		//main_panel.add(desk, BorderLayout.CENTER);

		JToolBar tool_bar = makeToolBar(
						Actions.newProjectAction,
						Actions.newFileAction,
						Actions.openFileAction,
						null,
						Actions.saveAction,
						null,
						Actions.undoAction,
						Actions.redoAction,
						Actions.compileAction,
						Actions.renderAction,
						null,
						Actions.exitAction
		);
		// main_panel.add(tool_bar, BorderLayout.PAGE_START);

		current_file = new JLabel("[NONE]");
		line_info = new JLabel();
		line_perc = new JLabel();
		charset = new JLabel();
		seekBar = new JProgressBar();
		seekBar.setValue(50);

		JToolBar bottom_bar = makeLRToolBar(
						new Component[]{current_file, null},
						new Component[]{line_info, line_perc, seekBar, null, charset}
		);

		Action[] app_actions = {
			Actions.showFileTreeAction,
			Actions.searchAction,
			Actions.commitAction,
			Actions.exportAction,
			Actions.AIAction,
			Actions.communityAction,
			Actions.pluginsAction
		};

		var bar = makeCoolbar(height, app_actions);

		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);

		Actions.loadActions();
		Menus.addMenus(menu_bar);
		this.setIconImage(Icons.getIcon("appicon").getImage());

		// Canvas and Access Panels
		var canvas_panel = CanvasFrame.the();
		var access_panel = new DockablePanel(new BorderLayout(), "Run");
		access_panel.add(new AccessFrame(width));

		var render_panel = new DockablePanel(new BorderLayout(), "Render");
		render_panel.add(canvas_panel, BorderLayout.CENTER);
		Action[] render_actions = {
			Actions.normalAction,
			Actions.gridAction,
			Actions.pointAction,
			Actions.rulerAction,
			Actions.snapAction,
			Actions.brushAction,
			Actions.thickBrushAction,
			Actions.paintBucketAction,
			Actions.effectsAction,};

		var short_cuts = makeCoolbar(canvas_panel.getHeight(), render_actions);
		short_cuts.setBorder(BorderFactory.createEmptyBorder());
		render_panel.add(short_cuts, BorderLayout.EAST);
		render_panel.add(new JScrollPane(canvas_panel), BorderLayout.CENTER);

		var file_tree = new FileTreePanel();
		var vc_panel = new VCPanel();
		var plugins = new PluginsPanel();

		DockingPreferences.setDottedDesktopStyle();
		getContentPane().add(desk, BorderLayout.CENTER);
		getContentPane().add(render_panel, BorderLayout.WEST);
		getContentPane().add(access_panel, BorderLayout.SOUTH);
		getContentPane().add(file_tree, BorderLayout.EAST);
		getContentPane().add(vc_panel, BorderLayout.EAST);
		getContentPane().add(plugins, BorderLayout.EAST);
		getContentPane().add(tool_bar, BorderLayout.PAGE_START);
		getContentPane().add(bottom_bar, BorderLayout.PAGE_END);

		dashboard = new DockablePanel(new BorderLayout(), "Piccasso DashBoard", "DashBoard", "Home page", "file");
		dashboard.add(new JScrollPane(new DashboardPanel()), BorderLayout.CENTER);
		getContentPane().add(access_panel, BorderLayout.EAST);

		desk.addDockable(dashboard);
		desk.addDockable(file_tree);
		desk.addDockable(vc_panel);
		desk.addDockable(plugins);
		
		desk.setAutoHide(file_tree, true);
		desk.setAutoHide(vc_panel, true);
		desk.setAutoHide(plugins, true);

		desk.split(dashboard, render_panel, DockingConstants.SPLIT_RIGHT, 0.7);
		desk.split(render_panel, access_panel, DockingConstants.SPLIT_BOTTOM);

		win = this;

		ThemeManager.updateThemes(dark);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void initSearchDialogs() {
		findDialog = new FindDialog(this, this);
		replaceDialog = new ReplaceDialog(this, this);

		SearchContext context = findDialog.getSearchContext();
		replaceDialog.setSearchContext(context);
	}

	public static void addTab(ActionEvent e) {
		int index = tabEditors.size();
		CodeEditor editor = new CodeEditor();
		editor.tabIndex = index;
		Path file = editor.file;
		editor.requestFocusInWindow();
		current_file.setText(file != null ? file.toString() : "[NONE]");
		tabEditors.put(index, editor);
		ThemeManager.registerEditor(editor);
		ThemeManager.updateThemes(dark);

		// Add first editor normally
		if (index == 0) {
			win.getContentPane().add(editor);
			win.desk.createTab(win.dashboard, editor, 2);
		} else {
			// Add to same container as first editor
			CodeEditor firstEditor = tabEditors.get(0);
			win.desk.createTab(firstEditor, editor, 2);
		}
	}

	public static void addTab(Path path, Void e) {
		var index = tabEditors.size();
		var editor = new CodeEditor(path);
		editor.tabIndex = index;
		editor.load(path.toFile());
		editor.requestFocusInWindow();
		tabEditors.put(index, editor);

		ThemeManager.registerEditor(editor);
		ThemeManager.updateThemes(dark);
		// Add first editor normally
		if (index == 0) {
			win.getContentPane().add(editor);
			win.desk.createTab(win.dashboard, editor, 2);
		} else {
			// Add to same container as first editor
			CodeEditor firstEditor = tabEditors.get(0);
			try {
				win.desk.createTab(firstEditor, editor, 1);
			} catch (NullPointerException npe) {
				win.desk.createTab(win.dashboard, editor, 2);
			}
		}
	}

	public static void setSelectedEditor(CodeEditor ed) {
		selected = ed;
	}

	public static CodeEditor getSelectedEditor() {
		if (selected != null) {
			return selected;
		}

		for (var editor : tabEditors.values()) {
			if (editor.textArea.isFocusOwner()) {
				return editor;
			}
		}
		var values = tabEditors.values();
		if (values.isEmpty()) return null;
		return values.toArray(CodeEditor[]::new)[0]; // fallback if nothing has focus
	}

	private static void addPlusTab(JTabbedPane tabs) {
		var dashb = new JPanel(new BorderLayout());
		dashb.add(new DashboardPanel(), BorderLayout.CENTER);
		tabs.addTab("", new JScrollPane(dashb));

		JButton plusBtn = new JButton();
		plusBtn.setIcon(Icons.getIcon("add"));
		plusBtn.setMargin(new Insets(0, 8, 0, 8));
		plusBtn.setBorder(BorderFactory.createEmptyBorder());
		plusBtn.setContentAreaFilled(false);
		plusBtn.setFocusPainted(false);
		plusBtn.addActionListener(EditorWindow::addTab);
		tabs.setTabComponentAt(tabs.getTabCount() - 1, plusBtn);
	}

	private static Component makeTabHeader(JTabbedPane tabs, String title) {
		JPanel tabHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tabHeader.setOpaque(false);

		JLabel titleLabel = new JLabel(title + " ");
		JButton closeButton = new JButton(Icons.getIcon("close"));

		closeButton.setMargin(new Insets(0, 5, 0, 5));
		closeButton.setFocusable(false);
		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
		closeButton.setOpaque(false);
		closeButton.setForeground(Color.RED);
		closeButton.setFont(closeButton.getFont().deriveFont(Font.BOLD, 12));

		closeButton.addActionListener(e -> {
			int index = tabs.indexOfTabComponent(tabHeader);
			if (index != -1) {
				var ed = tabEditors.get(index);
				removeIfNotDirty(index, ed);
			}
		});

		tabHeader.add(titleLabel);
		tabHeader.add(closeButton);

		return tabHeader;
	}

	public static void removeTab() {
		if (tabEditors.size() <= 1) {
			return;
		}

		var focused = getSelectedEditor();
		if (focused == null) {
			return;
		}

		Integer index = getEditorIndex(focused);
		if (index == null) {
			return;
		}

		removeIfNotDirty(index, focused);
	}

	public static void removeAllTabs() {
		var editors = new HashMap<>(tabEditors); // Copy to avoid ConcurrentModificationException
		for (var entry : editors.entrySet()) {
			removeIfNotDirty(entry.getKey(), entry.getValue());
		}
	}

	public static int tabsCount() {
		return tabEditors.size();
	}

	private static boolean removeIfNotDirty(Integer index, CodeEditor ed) {
		if (ed.textArea.isDirty()) {
			int result = JOptionPane.showConfirmDialog(win, "File " + ed.filePathTruncated() + " is modified. Save?");
			if (result == JOptionPane.OK_OPTION) {
				return ed.saveFile();
			}
		}
		win.desk.remove((Dockable) ed); // Actual removal from docking layout
		tabEditors.remove(index);
		ThemeManager.removeEditor(ed);
		migrateIndexes();

		return true;
	}

	private static boolean isDocked(Dockable d) {
		for (var state : win.desk.getDockables()) {
			var dockable = state.getDockable();
			if (dockable == d || dockable.equals(d)) {
				return true;
			}
		}
		return false;
	}

	private static Integer getEditorIndex(CodeEditor ed) {
		for (var entry : tabEditors.entrySet()) {
			if (entry.getValue() == ed) {
				return entry.getKey();
			}
		}
		return null;
	}

	private static void migrateIndexes() {
		HashMap<Integer, CodeEditor> newMap = new HashMap<>();
		int idx = 0;
		for (CodeEditor ed : tabEditors.values()) {
			newMap.put(idx++, ed);
		}
		tabEditors = newMap;
	}

	public static void saveAll() {
		for (var kv : tabEditors.entrySet()) {
			var editor = kv.getValue();
			editor.saveFile();
		}
	}

	public static void setSeletedTabTitle(String title) {
		var ed = getSelectedEditor();
		ed.setKey(title);
	}

	private JPanel makeCoolbar(int height, Action... actions) {
		var cool_bar = new JPanel(new BorderLayout());
		cool_bar.setPreferredSize(new Dimension(50, height));
		JPanel top_buttons = new JPanel(new GridLayout(10, 1));
		top_buttons.setOpaque(false); // transparent to inherit background

		for (var action : actions) {
			JButton btn = new JButton(action);
			btn.setText("");
			top_buttons.add(btn);
		}

		JPanel bottom_button = new JPanel(new BorderLayout());
		bottom_button.setOpaque(false);

		JButton settingsBtn = new JButton("", Icons.getIcon("settings"));
		settingsBtn.setToolTipText("Settings");

		bottom_button.add(settingsBtn, BorderLayout.SOUTH);
		cool_bar.add(top_buttons, BorderLayout.NORTH);
		cool_bar.add(bottom_button, BorderLayout.SOUTH);

		return cool_bar;
	}

	private JToolBar makeToolBar(Action... actions) {
		JToolBar cool_bar = new JToolBar();
		for (var action : actions) {
			if (action == null) {
				var sep = new JSeparator(JSeparator.VERTICAL);
				cool_bar.add(sep);
				continue;
			}
			JButton btn = new JButton(action);
			btn.setText("");
			cool_bar.add(btn);
		}
		return cool_bar;
	}

	private JToolBar makeLRToolBar(Component[] llbls, Component[] rlbls) {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setLayout(new BorderLayout());

		// LEFT panel
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		for (var lbl : llbls) {
			if (lbl == null) {
				leftPanel.add(new JSeparator(SwingConstants.VERTICAL));
			} else {
				leftPanel.add(lbl);
			}
		}

		// RIGHT panel
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		for (var lbl : rlbls) {
			if (lbl == null) {
				rightPanel.add(new JSeparator(SwingConstants.VERTICAL));
			} else {
				rightPanel.add(lbl);
			}
		}

		// Add them to the toolbar using BorderLayout
		toolBar.add(leftPanel, BorderLayout.WEST);
		toolBar.add(rightPanel, BorderLayout.EAST);

		return toolBar;
	}

	@Override
	public void searchEvent(SearchEvent se) {
		SearchEvent.Type type = se.getType();
		SearchContext context = se.getSearchContext();
		SearchResult result;
		var ed = getSelectedEditor();
		if (ed == null) {
			return;
		}
		
		var textArea = ed.textArea;

		switch (type) {
			default: // Prevent FindBugs warning later
			case MARK_ALL:
				result = SearchEngine.markAll(textArea, context);
				break;
			case FIND:
				result = SearchEngine.find(textArea, context);
				if (!result.wasFound() || result.isWrapped()) {
					UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				}
				break;
			case REPLACE:
				result = SearchEngine.replace(textArea, context);
				if (!result.wasFound() || result.isWrapped()) {
					UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				}
				break;
			case REPLACE_ALL:
				result = SearchEngine.replaceAll(textArea, context);
				JOptionPane.showMessageDialog(null, result.getCount()
								+ " occurrences replaced.");
				break;
		}
	}

	@Override
	public String getSelectedText() {
		var ed = getSelectedEditor();
		if (ed == null) {
			return "";
		}
		return ed.textArea.getSelectedText();
	}

	private void customizeDock() {
		UIManager.put("DockViewTitleBar.close", (Icon) Icons.getIcon("close"));
		UIManager.put("DockTabbedPane.close", (Icon) Icons.getIcon("close"));
		UIManager.put("DockViewTitleBar.isFloatButtonDisplayed", true);

		var font = (Font) UIManager.get("DockViewTitleBar.titleFont");
		UIManager.put("DockViewTitleBar.titleFont", new Font(font.getName(), font.getStyle(), 11));
	}

}
