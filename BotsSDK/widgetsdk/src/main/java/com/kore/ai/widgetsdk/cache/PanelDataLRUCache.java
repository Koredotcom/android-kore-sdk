package com.kore.ai.widgetsdk.cache;

import com.kore.ai.widgetsdk.models.WidgetBaseDataModel;

import java.util.HashMap;

public class PanelDataLRUCache {

    private static PanelDataLRUCache mInstance = null;
    HashMap<String, Node> hashMap;
    Node start;
    Node end;

    static final int CACHE_SIZE = 15;

    private PanelDataLRUCache(){
        hashMap = new HashMap<>();
    }

    public synchronized static PanelDataLRUCache getInstance(){
        if(mInstance == null)
            mInstance = new PanelDataLRUCache();
        return mInstance;
    }
    public WidgetBaseDataModel getEntry(String key) {
        if (hashMap.containsKey(key)) // Key Already Exist, just update the
        {
            Node entry = hashMap.get(key);
            removeNode(entry);
            addAtTop(entry);
            return entry.value;
        }
        return null;
    }

    public void putEntry(String key, WidgetBaseDataModel value) {
        if (hashMap.containsKey(key)) // Key Already Exist, just update the value and move it to top
        {
            Node entry = hashMap.get(key);
            entry.value = value;
            removeNode(entry);
            addAtTop(entry);
        } else {
            Node newNode = new Node();
            newNode.left = null;
            newNode.right = null;
            newNode.value = value;
            newNode.key = key;
            if (hashMap.size() > CACHE_SIZE) // We have reached maxium size so need to make room for new element.
            {
                hashMap.remove(end.key);
                removeNode(end);
                addAtTop(newNode);

            } else {
                addAtTop(newNode);
            }

            hashMap.put(key, newNode);
        }
    }

    public void addAtTop(Node node) {
        node.right = start;
        node.left = null;
        if (start != null)
            start.left = node;
        start = node;
        if (end == null)
            end = start;
    }
    public void removeNode(Node node) {

        if (node.left != null) {
            node.left.right = node.right;
        } else {
            start = node.right;
        }

        if (node.right != null) {
            node.right.left = node.left;
        } else {
            end = node.left;
        }
    }

    public void clearAll() {
        if(hashMap != null)
            hashMap.clear();
    }
}