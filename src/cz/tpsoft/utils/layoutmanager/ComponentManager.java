/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.layoutmanager;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author trptom
 */
public class ComponentManager {
//    public static final Color DEFAULT_PANEL_BACKGROUND = UIManager.getColor("Panel.background");
    
    public static enum Alignment {
        CORNER_LEFTTOP {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                JPanel tmp = new JPanel(new BorderLayout(spacing, spacing));
                tmp.add(content, BorderLayout.NORTH);
                ret.add(tmp, BorderLayout.WEST);
                tmp.setBackground(background);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        CORNER_LEFTBOTTOM {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                JPanel tmp = new JPanel(new BorderLayout(spacing, spacing));
                tmp.add(content, BorderLayout.SOUTH);
                ret.add(tmp, BorderLayout.WEST);
                tmp.setBackground(background);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        CORNER_RIGHTTOP {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                JPanel tmp = new JPanel(new BorderLayout(spacing, spacing));
                tmp.add(content, BorderLayout.NORTH);
                ret.add(tmp, BorderLayout.EAST);
                tmp.setBackground(background);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        CORNER_RIGHTBOTTOM {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                JPanel tmp = new JPanel(new BorderLayout(spacing, spacing));
                tmp.add(content, BorderLayout.SOUTH);
                ret.add(tmp, BorderLayout.EAST);
                tmp.setBackground(background);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        LEFT {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                ret.add(content, BorderLayout.WEST);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        RIGHT {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                ret.add(content, BorderLayout.EAST);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        TOP {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                ret.add(content, BorderLayout.NORTH);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        BOTOM {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                ret.add(content, BorderLayout.SOUTH);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        CENTER {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel();
                GridBagLayout gridbag = new GridBagLayout();
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.CENTER;
                gridbag.setConstraints(ret, constraints);
                ret.setLayout(gridbag);
                ret.add(content);
                setPanelBackground(ret, background);
                return ret;
            }
        },
        CENTER_STRETCHED {
            @Override
            protected JPanel getPanel(Component content, int spacing, Color background) {
                JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
                ret.add(content, BorderLayout.CENTER);
                setPanelBackground(ret, background);
                return ret;
            }
        };
        protected void setPanelBackground(JPanel panel, Color background) {
            if (background == null) {
                panel.setOpaque(false);
            } else {
                panel.setOpaque(true);
                panel.setBackground(background);
            }
        }
        protected JPanel getPanel(Component content, Color background) {
            return getPanel(content, 0, background);
        };
        protected abstract JPanel getPanel(Component content, int spacing, Color background);
    }
    
    public static JPanel getComponentsGrid(Component[][] cells,
            Alignment[][] cellAlignments, Alignment alignment, int rowSpacing,
            int colSpacing) {
        return getComponentsGrid(cells, cellAlignments, alignment, rowSpacing,
                colSpacing, null);
    }
    
    public static JPanel getComponentsGrid(Component[][] cells,
            Alignment[][] cellAlignments, Alignment alignment, int rowSpacing,
            int colSpacing, Color background) {
        JPanel tmp = new JPanel(new GridLayout(0, 1, colSpacing, rowSpacing));
        if (background != null) {
            tmp.setOpaque(true);
            tmp.setBackground(background);
        } else {
            tmp.setOpaque(false);
        }
        for (int a=0; a<cells.length; a++) {
            if (cellAlignments != null) {
                tmp.add(getGridLine(
                        cells[a],
                        cellAlignments[a % cellAlignments.length], colSpacing,
                        background));
            } else {
                tmp.add(getGridLine(
                        cells[a],
                        null,
                        colSpacing,
                        background));
            }
        }
        JPanel ret = alignment.getPanel(tmp, background);
        return ret;
    }

    public static JPanel getComponentInPanel(Component component,
            Alignment alignment) {
        return getComponentInPanel(component, alignment, null);
    }
    
    public static JPanel getComponentInPanel(Component component, 
            Alignment alignment, Color background) {
        return alignment.getPanel(component, 0, background);
    }
    
    public static JPanel getComponentInPanel(Component component,
            Alignment alignment, int spacing) {
        return getComponentInPanel(component, alignment, spacing, null);
    }
    
    public static JPanel getComponentInPanel(Component component,
            Alignment alignment, int spacing, Color background) {
        JPanel ret = alignment.getPanel(component, spacing, background);
        return ret;
    }
    
    public static JPanel getGridLine(Component[] components, Alignment[] alignments,
            int spacing) {
        return getGridLine(components, alignments, spacing, null);
    }
    
    public static JPanel getGridLine(Component[] components, Alignment[] alignments,
            int spacing, Color background) {
        JPanel ret = new JPanel(new GridLayout(1, components.length, spacing, spacing));
        for (int a=0; a<components.length; a++) {
            if (alignments != null) {
                ret.add(getComponentInPanel(
                        components[a],
                        alignments[a % alignments.length],
                        background));
            } else {
                ret.add(getComponentInPanel(
                        components[a],
                        Alignment.CENTER_STRETCHED,
                        background), background);
            }
        }
        if (background != null) {
            ret.setOpaque(true);
            ret.setBackground(background);
        } else {
            ret.setOpaque(false);
        }
        return ret;
    }
    
    public static JPanel getMergedComponents(Component[] components,
            Alignment alignment, int spacing) {
        return getMergedComponents(components, alignment, spacing, null);
    }
    
    public static JPanel getMergedComponents(Component[] components,
            Alignment alignment, int spacing, boolean lastSpace) {
        return getMergedComponents(components, alignment, spacing, null, lastSpace);
    }
    
    public static JPanel getMergedComponents(Component[] components,
            Alignment alignment, int spacing, Color background) {
        return getMergedComponents(components, alignment, spacing, background, true);
    }
    
    public static JPanel getMergedComponents(Component[] components,
            Alignment alignment, int spacing, Color background,
            boolean lastSpace) {
        if (alignment == Alignment.CENTER_STRETCHED) throw new IllegalArgumentException(
                "alignment nesmi byt center");
        JPanel ret = new JPanel(new BorderLayout(spacing, spacing));
        JPanel tmp = ret;
        for (Component component : components) {
            if (!lastSpace && component == components[components.length-1]) {
                spacing = 0;
            }
            JPanel newPanel = alignment.getPanel(component, spacing, background);
            tmp.add(newPanel, BorderLayout.CENTER);
            tmp = newPanel;
        }
        if (background != null) {
            ret.setOpaque(true);
            ret.setBackground(background);
        } else {
            ret.setOpaque(false);
        }
        return ret;
    }
}
