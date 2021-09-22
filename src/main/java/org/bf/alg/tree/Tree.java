package org.bf.alg.tree;

import org.jetbrains.annotations.NotNull;

public interface Tree<K> {
    /**
     * Insert a key into tree.
     * @param key needs inserting
     */
    void insert(@NotNull K key);

    /**
     * Delete a key from tree.
     * @param key needs deleting
     */
    void delete(@NotNull K key);
}
