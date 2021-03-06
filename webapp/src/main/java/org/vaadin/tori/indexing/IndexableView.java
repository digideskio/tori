/*
 * Copyright 2012 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.vaadin.tori.indexing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class IndexableView {
    protected final List<String> arguments;
    protected final ToriIndexableApplication application;

    public IndexableView(final List<String> arguments,
            final ToriIndexableApplication application) {
        this.arguments = arguments;
        this.application = application;
    }

    abstract public String getHtml();

    public static <T extends IndexableView> T newInstance(
            final Class<T> viewClass, final List<String> arguments,
            final ToriIndexableApplication application) {
        try {
            final Constructor<T> constructor = viewClass.getConstructor(
                    List.class, ToriIndexableApplication.class);
            return constructor.newInstance(arguments, application);
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        }

        Logger.getLogger(IndexableView.class).error(
                "Improperly constructed " + IndexableView.class.getSimpleName()
                        + ": " + viewClass.getClass().getName());
        return null;
    }

    protected Logger getLogger() {
        return Logger.getLogger(getClass());
    }
}
