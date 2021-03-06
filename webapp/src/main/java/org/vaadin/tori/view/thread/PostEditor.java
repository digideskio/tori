/*
 * Copyright 2014 Vaadin Ltd.
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

package org.vaadin.tori.view.thread;

import org.vaadin.tori.component.BBCodeWysiwygEditor;
import org.vaadin.tori.util.ComponentUtil;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class PostEditor extends VerticalLayout {

    private AbstractField<String> editor;
    private final PostEditorListener listener;

    public PostEditor(final String rawBody, final boolean bbcode,
            final PostEditorListener listener) {
        this.listener = listener;
        addStyleName("posteditor");
        addStyleName("authoring");
        setSizeFull();
        editor = buildEditor(bbcode);
        editor.setValue(rawBody);
        addComponent(editor);
        setExpandRatio(editor, 1.0f);
        addComponent(buildButtons());
    }

    private Component buildButtons() {
        HorizontalLayout result = new HorizontalLayout();
        result.addStyleName("buttonslayout");
        result.setWidth(100.0f, Unit.PERCENTAGE);
        result.setSpacing(true);
        result.setMargin(true);
        final Button cancelButton = ComponentUtil.getSecondaryButton(
                "Discard Changes", new Button.ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        event.getButton().removeClickShortcut();
                        listener.cancelEdit();
                    }
                });
        result.addComponent(new Button("Confirm Edit",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(final ClickEvent event) {
                        cancelButton.removeClickShortcut();
                        listener.submitEdit(editor.getValue());
                    }
                }));

        cancelButton.setClickShortcut(KeyCode.ESCAPE);
        result.addComponent(cancelButton);
        result.setExpandRatio(cancelButton, 1.0f);
        return result;
    }

    private AbstractField<String> buildEditor(final boolean bbcode) {
        if (Page.getCurrent().getWebBrowser().isAndroid()) {
            editor = new TextArea();
        } else {
            editor = new BBCodeWysiwygEditor(false, bbcode);
        }
        editor.setSizeFull();
        return editor;
    }

    public interface PostEditorListener {
        void submitEdit(String rawBody);

        void cancelEdit();
    }

}
