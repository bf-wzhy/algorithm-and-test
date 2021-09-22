package org.bf.alg.tree;

import org.jetbrains.annotations.NotNull;

public interface Tree<K> {
    void insert(@NotNull K key);
    void delete(@NotNull K key);
}
