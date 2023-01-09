package me.opkarol.opc.api.schematic;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Optional;

public class SchematicQueue {
    private Thread thread;

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void build(Schematic schematic) {
        setThread(new Thread(() -> {
            for (Vector vector : schematic.getPositions()) {
                Optional<Block> optional = schematic.getBlock(vector);
                if (optional.isEmpty()) {
                    continue;
                }

                Block block = optional.get();
                
            }
        }));
    }
}
