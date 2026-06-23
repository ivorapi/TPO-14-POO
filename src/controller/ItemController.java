package controller;

import modelo.Item;

import java.util.ArrayList;

public class ItemController {
    private ArrayList<Item> items;

    public ItemController() {
        this.items = new ArrayList<>();
    }

    public void agregarItem(Item item) {
        items.add(item);
    }

    public Item buscarItemPorCodigo(int codigo) {
        for (Item item : items) {
            if (item.getCodigo() == codigo) {
                return item;
            }
        }

        return null;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}