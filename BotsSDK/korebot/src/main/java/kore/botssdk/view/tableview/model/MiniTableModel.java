package kore.botssdk.view.tableview.model;

import java.util.List;

/**
 * Created by Ramachandra Pradeep on 13-Apr-18.
 */

public class MiniTableModel {
    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    private List<String> elements;
}
