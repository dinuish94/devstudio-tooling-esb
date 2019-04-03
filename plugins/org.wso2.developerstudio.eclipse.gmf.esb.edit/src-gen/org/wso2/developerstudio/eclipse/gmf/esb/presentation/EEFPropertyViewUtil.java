package org.wso2.developerstudio.eclipse.gmf.esb.presentation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.wso2.developerstudio.eclipse.esb.project.artifact.ESBArtifact;
import org.wso2.developerstudio.eclipse.esb.project.artifact.ESBProjectArtifact;
import org.wso2.developerstudio.eclipse.gmf.esb.persistence.Activator;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.eclipse.swt.layout.GridLayout;

public class EEFPropertyViewUtil {

    Composite view;
    static Properties properties = null;
    private static final String TYPE_TEMPLATE_SEQ = "synapse/sequenceTemplate";
    private static final String TYPE_TEMPLATE_EPT = "synapse/endpointTemplate";
    private static final String AVAILABLE_TEMPLATE_LIST_DEFAULT_VALUE = "Select From Templates";
    private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

    static {
        URL url;
        try {
            url = new URL("platform:/plugin/org.wso2.developerstudio.eclipse.gmf.esb.edit/src-gen/org/wso2/developerstudio/eclipse/gmf/esb/presentation/helpcontent.properties");
            InputStream inputStream = url.openConnection().getInputStream();
            properties = new Properties();
            properties.load(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EEFPropertyViewUtil(Composite view) {
        this.view = view;
    }

    public void clearElements(Composite[] propertiesGroups) {
        for (Composite propertiesGroup : propertiesGroups) {
            hideEntry(propertiesGroup.getChildren(), false);
        }
    }

    public void clearTableButtons(Control[] controls) {
        for (Control control : ((Composite)controls[0]).getChildren()) {
          if (control instanceof org.eclipse.swt.widgets.Button) {
              control.setVisible(false);
          }
        }
    }

    public void clearElement(Control control) {
        if (control.getLayoutData() != null && control.getLayoutData() instanceof GridData) {
            ((GridData) control.getLayoutData()).exclude = true;
            control.setVisible(false);
        }
    }

    public void hideEntry(Control controls[], boolean layout) {
        // view.getChildren();
        for (Control control : controls) {
            // null check and type check
            // control.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
            control.setEnabled(true);
            if (control.getLayoutData() != null && control.getLayoutData() instanceof GridData) {
                ((GridData) control.getLayoutData()).exclude = true;
                control.setVisible(false);
            } else if (control.getLayoutData() != null && control.getLayoutData() instanceof FormData) {
                // ((FormData) control.getLayoutData()). = true;
                control.setVisible(false);
            } else {
                control.setLayoutData(new GridData());
                ((GridData) control.getLayoutData()).exclude = true;
                control.setVisible(false);
            }
        }
        if (layout) {
            view.layout(true, true);
        }
    }

    public void showEntry(Control controls[], boolean layout) {
        for (Control control : controls) {
            // null check and type check
            // control.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
            control.setEnabled(true);
            if (control.getLayoutData() != null && control.getLayoutData() instanceof GridData) {
                ((GridData) control.getLayoutData()).exclude = false;
                control.setVisible(true);
            } else if (control.getLayoutData() != null && control.getLayoutData() instanceof FormData) {
                control.setVisible(true);
            } else {
                control.setLayoutData(new GridData());
                ((GridData) control.getLayoutData()).exclude = false;
                control.setVisible(true);
            }
        }
        if (layout) {
            view.layout(true, true);
        }
    }
    
    public void showEntry(ArrayList<Control> controls, boolean layout) {
        for (Control control : controls) {
            // null check and type check
            // control.setLayoutData( new GridData(GridData.FILL_HORIZONTAL));
            control.setEnabled(true);
            if (control.getLayoutData() != null && control.getLayoutData() instanceof GridData) {
                ((GridData) control.getLayoutData()).exclude = false;
                control.setVisible(true);
            } else if (control.getLayoutData() != null && control.getLayoutData() instanceof FormData) {
                control.setVisible(true);
            } else {
                control.setLayoutData(new GridData());
                ((GridData) control.getLayoutData()).exclude = false;
                control.setVisible(true);
            }
        }
        if (layout) {
            view.layout(true, true);
        }
    }

    public static Control[] getTableElements(Control[] previousControls, Control[] newControls) {
        Control[] tableElements = new Control[newControls.length - previousControls.length];
        for (int i = previousControls.length; i < newControls.length; i++) {
            tableElements[i - previousControls.length] = newControls[i];
        }
        return tableElements;
    }
    
    public static void addTableElementsAsList(ArrayList<Control> arrayList, Control[] previousControls, Control[] newControls) {
/*        Control [] controlArray = getTableElements(previousControls, newControls);
        for(Control control : controlArray) {
            arrayList.add(control);
        }*/
        
        //Control[] tableElements = new Control[newControls.length - previousControls.length];
        for (int i = previousControls.length; i < newControls.length; i++) {
            //tableElements[i - previousControls.length] = newControls[i];
            arrayList.add(newControls[i]);
        }
    }
    
    // This method will return a subSection group that can be use to group section inside a eef form
    public static Composite createSubsectionGroup(FormToolkit widgetFactory, final Composite parent, String name, boolean expanded) {
        int style = Section.TITLE_BAR | Section.TWISTIE;
        if(expanded) {
            style = Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED;
        }
        Section propertiesSection = widgetFactory.createSection(parent, style);
        propertiesSection.setText(name);
        GridData propertiesSectionData = new GridData(GridData.FILL_HORIZONTAL);
        propertiesSectionData.horizontalSpan = 3;
        propertiesSection.setLayoutData(propertiesSectionData);
        Composite subsectionGroup = widgetFactory.createComposite(propertiesSection);
        GridLayout propertiesGroupLayout = new GridLayout();
        propertiesGroupLayout.numColumns = 3;
        subsectionGroup.setLayout(propertiesGroupLayout);
        propertiesSection.setClient(subsectionGroup);
        return subsectionGroup;
    }
    
    /**
     * This method will check if a specific key combination matches a defined
     * set of key combinations.
     * 
     * @param e KeyEvent instance.
     * @return 'True' if matches, 'False' otherwise.
     */
    public static boolean isReservedKeyCombination(KeyEvent e) {
        
        if (e.keyCode == SWT.CR) {
            return true;
        } else if (e.keyCode == SWT.COMMAND) {
            return true;
        } else if (e.keyCode == SWT.CTRL) {
            return true;
        } else if (e.keyCode == SWT.ALT) {
            return true;
        } else if (e.keyCode == SWT.SHIFT) {
            return true;
        } else if (e.keyCode == SWT.ESC) {
            return true;
        } else if (e.stateMask == SWT.COMMAND) {
            return true;
        } else if (e.stateMask == SWT.CTRL && e.keyCode != 118) {//not returning true for Ctrl+V
            return true;
        }
        
        return false;
    }

    /**
     * This returns help content assigned for each property (in helpContent.properties file)
     * @param key property key
     * @return property help content
     */
    public static String getHelpContent(Object key) {
        String helpContent = "";
        if(key instanceof String) {
            //replacing :: to - since colon is a reserved char
            helpContent = ((String)key).replaceAll("::", "-");
        }
        return properties.getProperty(helpContent);
    }

    public static ArrayList<String> getAvailableTemplateList() {
        ArrayList<String> definedTemplates = new ArrayList<String>();
        definedTemplates.add(AVAILABLE_TEMPLATE_LIST_DEFAULT_VALUE);
        File projectPath = null;
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for (IProject activeProject : projects) {
            if (activeProject != null) {
                try {
                    if (activeProject.hasNature("org.wso2.developerstudio.eclipse.esb.project.nature")) {
                        ESBProjectArtifact esbProjectArtifact = new ESBProjectArtifact();
                        projectPath = activeProject.getLocation().toFile();
                        try {
                            esbProjectArtifact.fromFile(activeProject.getFile("artifact.xml").getLocation().toFile());
                            List<ESBArtifact> allESBArtifacts = esbProjectArtifact.getAllESBArtifacts();
                            for (ESBArtifact esbArtifact : allESBArtifacts) {
                                if (TYPE_TEMPLATE_SEQ.equals(esbArtifact.getType())) {
                                    File artifact = new File(projectPath, esbArtifact.getFile());
                                    definedTemplates.add(artifact.getName().replaceAll("[.]xml$", ""));
                                } else if (TYPE_TEMPLATE_EPT.equals(esbArtifact.getType())) {
                                    File artifact = new File(projectPath, esbArtifact.getFile());
                                    definedTemplates.add(artifact.getName().replaceAll("[.]xml$", ""));
                                }
                            }
                        } catch (Exception e) {
                            log.error("Error occured while scanning the project for artifacts", e);
                        }
                    }
                } catch (CoreException e) {
                    log.error("Error occured while scanning the project", e);
                }
            }
        }
        return definedTemplates;
    }
}
