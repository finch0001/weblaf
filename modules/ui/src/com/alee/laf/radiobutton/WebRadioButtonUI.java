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

package com.alee.laf.radiobutton;

import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JRadioButton} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */

public class WebRadioButtonUI<C extends JRadioButton> extends WRadioButtonUI<C> implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( RadioButtonPainter.class )
    protected IRadioButtonPainter painter;

    /**
     * Returns an instance of the {@link WebRadioButtonUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebRadioButtonUI}
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebRadioButtonUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( button );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( button );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( button, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( button );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( button, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( button );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( button, padding );
    }

    /**
     * Returns radiobutton painter.
     *
     * @return radiobutton painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets radiobutton painter.
     * Pass null to remove radiobutton painter.
     *
     * @param painter new radiobutton painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( button, new DataRunnable<IRadioButtonPainter> ()
        {
            @Override
            public void run ( final IRadioButtonPainter newPainter )
            {
                WebRadioButtonUI.this.painter = newPainter;
            }
        }, this.painter, painter, IRadioButtonPainter.class, AdaptiveRadioButtonPainter.class );
    }

    @Override
    public Rectangle getIconBounds ()
    {
        if ( painter != null )
        {
            return painter.getIconBounds ();
        }
        return null;
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}