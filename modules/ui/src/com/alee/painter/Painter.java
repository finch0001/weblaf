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

import com.alee.managers.style.Bounds;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * This interface provides basic methods for drawing components or sections of components.
 * Using painters you can easily change Swing and WebLaF components visual representation.
 * <p/>
 * Whether or not single painter can be used for multiply components exclusively depends on its implementation.
 * In most cases painters which does some animation won't work well with multiply components unless stated otherwise in JavaDoc.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see SpecificPainter
 * @see SectionPainter
 * @see AbstractPainter
 * @see com.alee.painter.decoration.AbstractDecorationPainter
 * @see com.alee.painter.decoration.AbstractSectionDecorationPainter
 */

public interface Painter<E extends JComponent, U extends ComponentUI>
{
    /**
     * todo 1. Add "E c, U ui" parameters to methods missing them OR
     * todo    Remove "E c, U ui" from all methods except {@link #install} and {@link #uninstall}
     * todo    Probably second option is better to clarify painters usage
     */

    /**
     * Called when painter is installed onto some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c  component this painter is being installed onto
     * @param ui component UI
     */
    public void install ( E c, U ui );

    /**
     * Called when painter is installed into some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c  component this painter is being uninstalled from
     * @param ui component UI
     */
    public void uninstall ( E c, U ui );

    /**
     * Returns whether or not this painter is installed onto some component.
     *
     * @return {@code true} if this painter is installed onto some component, {@code false} otherwise
     */
    public boolean isInstalled ();

    /**
     * Returns whether the view  provided by this painter is opaque or not.
     * Returned value might affect component opacity depending on painter support inside that component UI.
     * Simply return null if you don't want to change default component opacity.
     *
     * @return {@code true} if the view provided by this painter is opaque, {@code false} otherwise
     */
    public Boolean isOpaque ();

    /**
     * Returns component baseline within the specified bounds, measured from the top of the bounds.
     * A return value less than {@code 0} indicates this component does not have a reasonable baseline.
     * This method is primarily meant for {@code java.awt.LayoutManager}s to align components along their baseline.
     *
     * @param c      aligned component
     * @param ui     aligned component UI
     * @param bounds bounds to get the baseline for
     * @return component baseline within the specified bounds, measured from the top of the bounds
     */
    public int getBaseline ( E c, U ui, Bounds bounds );

    /**
     * Returns enum indicating how the baseline of the component changes as the size changes.
     *
     * @param c  aligned component
     * @param ui aligned component UI
     * @return enum indicating how the baseline of the component changes as the size changes
     */
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( E c, U ui );

    /**
     * Paints the view using component graphics context.
     * Provided graphics context and component are taken directly from component UI paint method.
     * <p>
     * It is highly recommended to honor provided painting bounds to avoid painting issues.
     * These bounds might be representing full component size or contain just a small portion of the component.
     *
     * @param g2d    graphics context
     * @param c      painted component
     * @param ui     painted component UI
     * @param bounds painting bounds
     */
    public void paint ( Graphics2D g2d, E c, U ui, Bounds bounds );

    /**
     * Returns preferred size required for proper painting of the view provided by this painter.
     * This should not take into account any sizes not related to the painter itself.
     *
     * @return preferred size required for proper painting of the view provided by this painter
     */
    public Dimension getPreferredSize ();

    /**
     * Adds specified listener to this painter.
     *
     * @param listener painter listener to add
     * @see PainterListener
     */
    public void addPainterListener ( PainterListener listener );

    /**
     * Removes specified listener from this painter.
     *
     * @param listener painter listener to remove
     * @see PainterListener
     */
    public void removePainterListener ( PainterListener listener );
}