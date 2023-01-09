package me.opkarol.opc.api.schematic;

import me.opkarol.opc.api.map.OpMap;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Schematic {
    private final OpMap<String, Block> blocks = new OpMap<>();
    private final OpMap<Vector, String> positionBlocks = new OpMap<>();

    public List<Vector> getPositions() {
        return positionBlocks.keySet().stream().toList();
    }

    public void clearPositions() {
        positionBlocks.getMap().clear();
    }

    public void add(Vector vector, String block) {
        positionBlocks.set(center(vector), block);
    }

    public boolean contains(Vector vector) {
        return getPositions().stream().anyMatch(vector1 -> vector1.equals(center(vector)));
    }

    public Optional<Block> getBlock(Vector vector) {
        Optional<String> optional = positionBlocks.getByKey(center(vector));
        if (optional.isPresent()) {
            return blocks.getByKey(optional.get());
        }
        return Optional.empty();
    }

    public List<Block> getBlocksAtX(double x) {
        return getBlocksAtLevel(vector -> vector.getBlockX() == x);
    }

    public List<Block> getBlocksAtY(double y) {
        return getBlocksAtLevel(vector -> vector.getBlockY() == y);
    }

    public List<Block> getBlocksAtZ(double z) {
        return getBlocksAtLevel(vector -> vector.getBlockZ() == z);
    }

    public List<Block> getBlocksAtLevel(Predicate<? super Vector> predicate) {
        return getPositions().stream()
                .filter(predicate)
                .map(this::getBlock)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Vector center(@NotNull Vector vector) {
        vector.setX(vector.getBlockX());
        vector.setY(vector.getBlockY());
        vector.setZ(vector.getBlockZ());
        return vector;
    }
}
