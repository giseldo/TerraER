/*
 * @(#)UndoAction.java  2.0  2006-06-15
 *
 * Copyright (c) 1996-2007 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */

package org.jhotdraw.app.action;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.Project;
import org.jhotdraw.util.ResourceBundleUtil;
/**
 * Undoes the last user action.
 * In order to work, this action requires that the Project returns a project
 * specific undo action when invoking getAction("undo") on the Project.
 *
 *
 * @author Werner Randelshofer
 * @version 2.0 2006-06-15 Reworked.
 * <br>1.0 October 9, 2005 Created.
 */
public class UndoAction extends AbstractProjectAction {
    public final static String ID = "undo";
    private ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.app.Labels");
    
    private PropertyChangeListener redoActionPropertyListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if (name == AbstractAction.NAME) {
                putValue(AbstractAction.NAME, evt.getNewValue());
            } else if (name == "enabled") {
                updateEnabledState();
            }
        }
    };
    
    /** Creates a new instance. */
    public UndoAction(Application app) {
        super(app);
        labels.configureAction(this, ID);
    }
    
    protected void updateEnabledState() {
        boolean isEnabled = false;
        Action realRedoAction = getRealRedoAction();
        if (realRedoAction != null) {
            isEnabled = realRedoAction.isEnabled();
        }
        setEnabled(isEnabled);
    }
    
    @Override protected void updateProject(Project oldValue, Project newValue) {
        super.updateProject(oldValue, newValue);
        if (newValue != null && newValue.getAction("undo") != null) {
            putValue(AbstractAction.NAME, newValue.getAction("undo").
                    getValue(AbstractAction.NAME));
            updateEnabledState();
        }
    }
    /**
     * Installs listeners on the project object.
     */
    @Override protected void installProjectListeners(Project p) {
        super.installProjectListeners(p);
        if (p.getAction("undo") != null) {
        p.getAction("undo").addPropertyChangeListener(redoActionPropertyListener);
        }
    }
    /**
     * Installs listeners on the project object.
     */
    @Override protected void uninstallProjectListeners(Project p) {
        super.uninstallProjectListeners(p);
        if (p.getAction("undo") != null) {
        p.getAction("undo").removePropertyChangeListener(redoActionPropertyListener);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Action realRedoAction = getRealRedoAction();
        if (realRedoAction != null) {
            realRedoAction.actionPerformed(e);
        }
    }
    
    private Action getRealRedoAction() {
        return (getCurrentProject() == null) ? null : getCurrentProject().getAction("undo");
    }
    
}
