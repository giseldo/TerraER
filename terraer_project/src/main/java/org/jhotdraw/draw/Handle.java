/*
 * @(#)Handle.java  2.0  2006-01-14
 *
 * Copyright (c) 1996-2006 by the original authors of JHotDraw
 * and all its contributors ("JHotDraw.org")
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JHotDraw.org ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * JHotDraw.org.
 */


package org.jhotdraw.draw;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.util.Collection;
/**
 * Handles are used to change a figure by direct manipulation.
 * Handles know their owning figure and they provide methods to
 * locate the handle on the figure and to track changes.
 * <p>
 * Handles are used for user interaction. Unlike figures, a handle works with
 * the user interface coordinates of the DrawingView. The user interface
 * coordinates are expressed in integer pixels.
 * <p>
 * A Handle forwards UndoableEdit events to the Drawing object onto which it
 * is performing changes.
 *
 * @author Werner Randelshofer
 * @version 2.0 2006-01-14 Changed to support double precision coordinates.
 * <br>1.0 2003-12-01 Derived from JHotDraw 5.4b1.
 */
public interface Handle extends KeyListener {
    /**
     * Returns the owner of this handle.
     */
    public Figure getOwner();
    /**
     * Sets the view of the handle.
     */
    public void setView(DrawingView view);
    /**
     * Adds a listener for this handle.
     */
    public void addHandleListener(HandleListener l);
    
    /**
     * Removes a listener for this handle.
     */
    void removeHandleListener(HandleListener l);
    /**
     * Returns the bounding box of the handle.
     * Note: The bounding box must be returned in the coordinate 
     * system of the DrawingView.
     */
    Rectangle getBounds();
    /**
     * Returns the drawing area of the handle.
     * Note: The drawing area must be returned in the coordinate 
     * system of the DrawingView.
     */
    Rectangle getDrawingArea();
    
    /**
     * Tests if a point is contained in the handle.
     */
    public boolean contains(Point p);
    
    /**
     * Draws this handle. 
     * Note: The handle is drawn with the coordinate system of
     * the DrawingView.
     */
    public void draw(Graphics2D g);
    /**
     * Invalidates the handle. This method informs its listeners
     * that its current display box is invalid and should be
     * refreshed.
     */
    public void invalidate();
    
    /**
     * Disposes the resources aquired by the handler.
     */
    public void dispose();
    
    /**
     * Returns a cursor for the handle.
     */
    public Cursor getCursor();
    
    /**
     * Returns true, if this handle is combinable with the specified handle.
     * This method is used to determine, if multiple handles need to be tracked,
     * when more than one figure is selected.
     */
    public boolean isCombinableWith(Handle handle);
    
    /**
     * Tracks the start of the interaction. The default implementation
     * does nothing.
     *  @param anchor the position where the interaction started
     */
    public void trackStart(Point anchor, int modifiersEx);
    
    /**
     * Tracks a step of the interaction.
     *  @param anchor the position where the interaction started
     * @param lead the current position
     */
    public void trackStep(Point anchor, Point lead, int modifiersEx);
    
    /**
     * Tracks the end of the interaction.
     *  @param anchor the position where the interaction started
     * @param lead the current position
     */
    public void trackEnd(Point anchor, Point lead, int modifiersEx);
    
    /**
     * Tracks a double click.
     */
    public void trackDoubleClick(Point p, int modifiersEx);
    
    /**
     * This method is invoked by the drawing view, when its transform
     * has changed. This means, that DrawingView.viewToDrawing and
     * DrawingView.drawingToView will return different values than they
     * did before.
     */
    public void viewTransformChanged();
    
    /**
     * Creates secondary handles.
     */
    public Collection<Handle> createSecondaryHandles();
    /**
     * Returns a tooltip for the specified location.
     */
    public String getToolTipText(Point p);
}
