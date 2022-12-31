package objectinventory;

import me.opkarol.opc.api.gui.inventory.IInventoryObject;
import me.opkarol.opc.api.gui.events.chest.OnItemClicked;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TestObject implements IInventoryObject {
    private final String name;
    private int something1;
    private boolean something2;
    private final String someOtherString;

    public TestObject(String name, int something1, boolean something2, String someOtherString) {
        this.name = name;
        this.something1 = something1;
        this.something2 = something2;
        this.someOtherString = someOtherString;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("SOMETHING 1: " + something1, "SOMETHING 2: " + something2);
    }

    @Override
    public Material getMaterial() {
        return Material.ARROW;
    }

    @Override
    public Consumer<OnItemClicked> getAction() {
        return e -> e.setCancelled(true);
    }

    public int getSomething1() {
        return something1;
    }

    public boolean isSomething2() {
        return something2;
    }

    public void setSomething1(int something1) {
        this.something1 = something1;
    }

    public void setSomething2(boolean something2) {
        this.something2 = something2;
    }

    public String getSomeOtherString() {
        return someOtherString;
    }
}
