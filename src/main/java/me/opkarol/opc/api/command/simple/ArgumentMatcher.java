package me.opkarol.opc.api.command.simple;

import java.util.List;

public interface ArgumentMatcher {
    List<String> filter(List<String> tabCompletions, String argument);
}
