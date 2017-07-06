/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.painter;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.Bounds;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.WebBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract {@link Painter} implementation provides a few basic commonly used features.
 * You might want to extended this class instead of implementing {@link Painter} interface directly.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @author Alexandr Zernov
 * @see Painter
 */

public abstract class AbstractPainter<E extends JComponent, U extends ComponentUI> implements Painter<E, U>
{
    /**
     * Painter listeners.
     */
    protected transient final List<PainterListener> listeners;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;

    /**
     * Whether or not this painter is installed onto some component.
     */
    protected transient boolean installed;

    /**
     * Component reference.
     */
    protected transient E component;

    /**
     * Component UI reference.
     */
    protected transient U ui;

    /**
     * Whether or not painted component has LTR orientation.
     */
    protected transient boolean ltr;

    /**
     * Constructs new {@link AbstractPainter}.
     */
    public AbstractPainter ()
    {
        super ();
        listeners = new ArrayList<PainterListener> ( 1 );
    }

    @Override
    public void install ( final E c, final U ui )
    {
        // Updating marks
        this.installed = true;

        // Saving references
        this.component = c;
        this.ui = ui;

        // Installing section painters
        installSectionPainters ();

        // Installing properties and listeners
        installPropertiesAndListeners ();

        // Updating orientation
        updateOrientation ();
        saveOrientation ();

        // Updating border
        updateBorder ();
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Uninstalling properties and listeners
        uninstallPropertiesAndListeners ();

        // Uninstalling section painters
        uninstallSectionPainters ();

        // Cleaning up references
        this.component = null;
        this.ui = null;

        // Updating marks
        this.installed = false;
    }

    @Override
    public boolean isInstalled ()
    {
        return installed;
    }

    @Override
    public Boolean isOpaque ()
    {
        return null;
    }

    /**
     * Returns whether or not this painter is allowed to update component settings and visual state.
     * By default it is determined by the painter type, for example any SectionPainter should avoid updating settings.
     *
     * @return true if this painter is allowed to update component settings and visual state, false otherwise
     */
    protected boolean isSettingsUpdateAllowed ()
    {
        return isInstalled () && !isSectionPainter ();
    }

    /**
     * Returns whether or not this is a section painter.
     * Some internal behaviors might vary depending on what this method returns.
     *
     * @return true if this is a section painter, false otherwise
     */
    protected boolean isSectionPainter ()
    {
        // todo Optimize this in future to return true/false
        // todo Also this can be completely moved to higher level implementations of section painters
        return this instanceof SectionPainter;
    }

    /**
     * Installs {@link SectionPainter}s used by this {@link Painter}.
     * Override this method instead of {@link #install(JComponent, ComponentUI)} to install additional {@link SectionPainter}s.
     */
    protected void installSectionPainters ()
    {
        // No section painters by default
    }

    /**
     * Uninstalls {@link SectionPainter}s used by this {@link Painter}.
     * Override this method instead of {@link #install(JComponent, ComponentUI)} to uninstall additional {@link SectionPainter}s.
     */
    protected void uninstallSectionPainters ()
    {
        // No section painters by default
    }

    /**
     * Installs properties and listeners used by this {@link Painter} implementation.
     * Override this method instead of {@link #install(JComponent, ComponentUI)} to install additional properties and listeners.
     */
    protected void installPropertiesAndListeners ()
    {
        installPropertyChangeListener ();
    }

    /**
     * Uninstalls properties and listeners used by this {@link Painter} implementation.
     * Override this method instead of {@link #uninstall(JComponent, ComponentUI)} to uninstall additional properties and listeners.
     */
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallPropertyChangeListener ();
    }

    /**
     * Installs listener that will inform about component property changes.
     */
    protected void installPropertyChangeListener ()
    {
        // Property change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                // Event Dispatch Thread check
                WebLookAndFeel.checkEventDispatchThread ();

                // Ensure component is still available
                // This might happen if painter is replaced from another PropertyChangeListener
                if ( component != null )
                {
                    // Inform about property change event
                    AbstractPainter.this.propertyChanged ( evt.getPropertyName (), evt.getOldValue (), evt.getNewValue () );
                }
            }
        };
        component.addPropertyChangeListener ( propertyChangeListener );
    }

    /**
     * Informs about {@link #component} property change.
     *
     * @param property modified property
     * @param oldValue old property value
     * @param newValue new property value
     */
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Forcing orientation visual updates
        if ( CompareUtils.equals ( property, WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
        {
            orientationChange ();
        }

        // Tracking component border changes
        if ( CompareUtils.equals ( property, WebLookAndFeel.BORDER_PROPERTY ) )
        {
            borderChange ( ( Border ) newValue );
        }

        // Tracking component margin and padding changes
        if ( CompareUtils.equals ( property, WebLookAndFeel.LAF_MARGIN_PROPERTY, WebLookAndFeel.LAF_PADDING_PROPERTY ) )
        {
            updateBorder ();
        }
    }

    /**
     * Uninstalls listener that is informing about component property changes.
     */
    protected void uninstallPropertyChangeListener ()
    {
        component.removePropertyChangeListener ( propertyChangeListener );
        propertyChangeListener = null;
    }

    /**
     * Performs various updates on orientation change.
     */
    protected void orientationChange ()
    {
        // Saving new orientation
        saveOrientation ();

        // Updating component view
        // Revalidate includes border update so we don't need to call it separately
        revalidate ();
        repaint ();
    }

    /**
     * Saves current component orientation state.
     */
    protected void saveOrientation ()
    {
        ltr = component.getComponentOrientation ().isLeftToRight ();
    }

    /**
     * Updates component orientation based on global orientation.
     */
    protected void updateOrientation ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            SwingUtils.setOrientation ( component );
        }
    }

    /**
     * Performs various border-related operations.
     *
     * @param border new border
     */
    protected void borderChange ( final Border border )
    {
        // First of all checking that it is not a UI resource
        // If it is not that means new component border was set from outside
        // We might want to keep that border and avoid automated WebLaF border to be set in future until old border is removed
        if ( !SwingUtils.isUIResource ( border ) )
        {
            SwingUtils.setHonorUserBorders ( component, true );
        }
    }

    /**
     * Returns {@link Painter} border according to component's margin, padding and {@link Painter}'s borders.
     * It is used to update component's border within {@link #updateBorder()} and to calculated default preferred size.
     *
     * @return {@link Painter} border according to component's margin, padding and {@link Painter}'s borders
     */
    protected Insets getCompleteBorder ()
    {
        final Insets border;
        if ( component != null && !SwingUtils.isPreserveBorders ( component ) )
        {
            // Initializing empty border
            border = new Insets ( 0, 0, 0, 0 );

            // Adding margin size
            if ( !isSectionPainter () )
            {
                final Insets margin = PainterSupport.getMargin ( component );
                if ( margin != null )
                {
                    border.top += margin.top;
                    border.left += ltr ? margin.left : margin.right;
                    border.bottom += margin.bottom;
                    border.right += ltr ? margin.right : margin.left;
                }
            }

            // Adding painter border size
            final Insets borders = getBorder ();
            if ( borders != null )
            {
                border.top += borders.top;
                border.left += ltr ? borders.left : borders.right;
                border.bottom += borders.bottom;
                border.right += ltr ? borders.right : borders.left;
            }

            // Adding padding size
            if ( !isSectionPainter () )
            {
                final Insets padding = PainterSupport.getPadding ( component );
                if ( padding != null )
                {
                    border.top += padding.top;
                    border.left += ltr ? padding.left : padding.right;
                    border.bottom += padding.bottom;
                    border.right += ltr ? padding.right : padding.left;
                }
            }
        }
        else
        {
            // Null border to prevent updates
            border = null;
        }
        return border;
    }

    /**
     * Returns border required for the view provided by this {@link Painter} or {@code null} in case it is not needed.     *
     * This border should not include possible component margin and padding, but only border provided by painter.
     * This border is added to component's margin and padding in {@link #getCompleteBorder()} calculations.
     * This border should not take component orientation into account, painter will take care of it later.
     *
     * @return border required for the view provided by this {@link Painter} or {@code null} in case it is not needed
     */
    protected Insets getBorder ()
    {
        return null;
    }

    @Override
    public int getBaseline ( final E c, final U ui, final Bounds bounds )
    {
        return -1;
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final E c, final U ui )
    {
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        return SwingUtils.increase ( new Dimension ( 0, 0 ), getCompleteBorder () );
    }

    @Override
    public void addPainterListener ( final PainterListener listener )
    {
        synchronized ( listeners )
        {
            listeners.add ( listener );
        }
    }

    @Override
    public void removePainterListener ( final PainterListener listener )
    {
        synchronized ( listeners )
        {
            listeners.remove ( listener );
        }
    }

    /**
     * Updates component with complete border.
     * This border takes painter borders and component margin and padding into account.
     */
    protected void updateBorder ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            final Insets border = getCompleteBorder ();
            if ( border != null )
            {
                final Border old = component.getBorder ();
                if ( !( old instanceof WebBorder ) || !CompareUtils.equals ( ( ( WebBorder ) old ).getBorderInsets (), border ) )
                {
                    component.setBorder ( new WebBorder ( border ) );
                }
            }
        }
    }

    /**
     * Should be called when whole painter visual representation changes.
     */
    protected void repaint ()
    {
        repaint ( 0, 0, component.getWidth (), component.getHeight () );
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param bounds part bounds
     */
    protected void repaint ( final Rectangle bounds )
    {
        repaint ( bounds.x, bounds.y, bounds.width, bounds.height );
    }

    /**
     * Should be called when part of painter visual representation changes.
     *
     * @param x      part bounds X coordinate
     * @param y      part bounds Y coordinate
     * @param width  part bounds width
     * @param height part bounds height
     */
    protected void repaint ( final int x, final int y, final int width, final int height )
    {
        if ( isSettingsUpdateAllowed () && component.isShowing () )
        {
            synchronized ( listeners )
            {
                for ( final PainterListener listener : listeners )
                {
                    listener.repaint ( x, y, width, height );
                }
            }
        }
    }

    /**
     * Should be called when painter size or border changes.
     */
    protected void revalidate ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            // Updating border to have correct size
            updateBorder ();

            // Revalidating layout
            synchronized ( listeners )
            {
                for ( final PainterListener listener : listeners )
                {
                    listener.revalidate ();
                }
            }
        }
    }

    /**
     * Should be called when painter opacity changes.
     * todo Use this instead of the outer border updates?
     */
    protected void updateOpacity ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            synchronized ( listeners )
            {
                for ( final PainterListener listener : listeners )
                {
                    listener.updateOpacity ();
                }
            }
        }
    }

    /**
     * Should be called when painter size, border and visual representation changes.
     * Makes sure that everything in the component view is up to date.
     */
    protected void updateAll ()
    {
        if ( isSettingsUpdateAllowed () )
        {
            updateBorder ();
            synchronized ( listeners )
            {
                for ( final PainterListener listener : listeners )
                {
                    listener.updateOpacity ();
                    listener.revalidate ();
                    if ( component.isShowing () )
                    {
                        listener.repaint ();
                    }
                }
            }
        }
    }
}