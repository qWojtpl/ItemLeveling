package pl.itemleveling.item;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemManager {

    private final List<CustomItem> items = new ArrayList<>();

}
