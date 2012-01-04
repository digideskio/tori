package org.vaadin.tori.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vaadin.tori.widgetset.client.ui.VLazyLayout;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

/**
 * Forked from {@link com.vaadin.ui.CssLayout} in Vaadin 6.7.3
 */
@ClientWidget(VLazyLayout.class)
public class LazyLayout extends AbstractLayout {
    private static final long serialVersionUID = -2190849834160200478L;

    /**
     * Custom layout slots containing the components.
     */
    protected List<Component> components = new ArrayList<Component>();

    private final String placeholderWidth = "100%";
    private final String placeholderHeight = "400px";

    private List<Integer> componentIndexesToSend = null;

    /**
     * Add a component into this container. The component is added to the right
     * or under the previous component.
     * 
     * @param c
     *            the component to be added.
     */
    @Override
    public void addComponent(final Component c) {
        // Add to components before calling super.addComponent
        // so that it is available to AttachListeners
        components.add(c);
        try {
            super.addComponent(c);
            requestRepaint();
        } catch (final IllegalArgumentException e) {
            components.remove(c);
            throw e;
        }
    }

    @Override
    public void removeComponent(final Component c) {
        components.remove(c);
        super.removeComponent(c);
        requestRepaint();
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        return components.iterator();
    }

    /**
     * Gets the number of contained components. Consistent with the iterator
     * returned by {@link #getComponentIterator()}.
     * 
     * @return the number of contained components
     */
    public int getComponentCount() {
        return components.size();
    }

    /**
     * @deprecated Don't call this manually
     */
    @Deprecated
    @Override
    public void paintContent(final PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute(VLazyLayout.ATT_PLACEHOLDER_HEIGHT_STRING,
                placeholderHeight);
        target.addAttribute(VLazyLayout.ATT_PLACEHOLDER_WIDTH_STRING,
                placeholderWidth);
        target.addAttribute(VLazyLayout.ATT_TOTAL_COMPONENTS_INT,
                components.size());

        if (componentIndexesToSend != null) {
            Collections.sort(componentIndexesToSend);

            final Map<Component, Integer> componentOrder = new HashMap<Component, Integer>();
            for (final int sendIndex : componentIndexesToSend) {
                final Component component = components.get(sendIndex);
                component.paint(target);
                componentOrder.put(component, sendIndex);
            }

            target.addAttribute(VLazyLayout.ATT_PAINT_INDICES_MAP,
                    componentOrder);
            componentIndexesToSend = null;
        }
    }

    /**
     * @deprecated not supported
     * @throws UnsupportedOperationException
     */
    @Deprecated
    @Override
    public void replaceComponent(final Component oldComponent,
            final Component newComponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void changeVariables(final Object source,
            final Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey(VLazyLayout.VAR_LOAD_INDEXES_INTARR)) {
            componentIndexesToSend = new ArrayList<Integer>();
            for (final Object o : (Object[]) variables
                    .get(VLazyLayout.VAR_LOAD_INDEXES_INTARR)) {
                componentIndexesToSend.add((Integer) o);
            }
            requestRepaint();
        }
    }
}
