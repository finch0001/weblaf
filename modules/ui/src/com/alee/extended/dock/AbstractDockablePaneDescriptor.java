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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

/**
 * Abstract descriptor for {@link WebDockablePane} component.
 * Extend this class for creating custom {@link WebDockablePane} descriptors.
 *
 * @param <C> {@link WebDockablePane} type
 * @param <U> base {@link WDockablePaneUI} type
 * @param <P> {@link IDockablePanePainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractDockablePaneDescriptor<C extends WebDockablePane, U extends WDockablePaneUI, P extends IDockablePanePainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractDockablePaneDescriptor}.
     *
     * @param id                  {@link WebDockablePane} identifier
     * @param componentClass      {@link WebDockablePane} {@link Class}
     * @param uiClassId           {@link WDockablePaneUI} {@link Class} identifier
     * @param baseUIClass         base {@link WDockablePaneUI} {@link Class} applicable to {@link WebDockablePane}
     * @param uiClass             {@link WDockablePaneUI} {@link Class} used for {@link WebDockablePane} by default
     * @param painterInterface    {@link IDockablePanePainter} interface {@link Class}
     * @param painterClass        {@link IDockablePanePainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IDockablePanePainter}
     * @param defaultStyleId      {@link WebDockablePane} default {@link StyleId}
     */
    public AbstractDockablePaneDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                            @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                            @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                            @NotNull final Class<? extends P> painterClass,
                                            @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }

    @Override
    public void updateUI ( @NotNull final C component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Updating dockable pane glass layer
        final WDockablePaneUI ui = component.getUI ();
        component.setGlassLayer ( ui.createGlassLayer () );
    }
}