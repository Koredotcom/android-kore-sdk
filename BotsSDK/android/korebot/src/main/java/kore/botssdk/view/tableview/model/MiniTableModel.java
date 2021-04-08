package kore.botssdk.view.tableview.model;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 13-Apr-18.
 */

public class MiniTableModel {
    public List<Object> getElements() {
        return elements;
    }

    public void setElements(List<Object> elements) {
        this.elements = elements;
    }

    private List<Object> elements;
}
