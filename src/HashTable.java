/*
 * HashTable.java
 * Ario Barin Ostovary
 * A hash table using an array list of linked lists (hashmap).
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class HashTable<K, V> {
    private int size; // Number of elements in the hash table
    private ArrayList<LinkedList<Entry<K, V>>> table;
    private double maxLoad = 0.7;

    // Valid load factors
    private static final double MIN_VALID_LOAD = 0.1;
    private static final double MAX_VALID_LOAD = 0.8;

    private static final int INITIAL_SIZE = 10;
    private static final int SIZE_MULTIPLIER = 10; // How much to multiply the size by when resizing

    private static class Entry<K, V> {
        private final K key;
        private final V value;  

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public HashTable() {
        this.size = 0; // Start with 0 elements
        fill(INITIAL_SIZE);
    }

    private int getHash(K key) {
        // Get the hash of the key
        return Math.abs(key.hashCode());
    }

    private int getIndex(int hash) {
        // Get the index of the hash in the table
        return hash % table.size();
    }

    private void fill(int newSize) {
        // Fill the table with nulls
        this.size = 0;
        this.table = new ArrayList<>(newSize);
        for (int i = 0; i < newSize; i++) {
            table.add(null);
        }
    }

    public double getLoad() {
        // Get the load factor of the table
        return (double) size / table.size();
    }

    private void resize() {
        ArrayList<LinkedList<Entry<K, V>>> oldTable = table;
        fill(table.size() * SIZE_MULTIPLIER);

        // Rehash all elements
        for (LinkedList<Entry<K, V>> lst : oldTable) {
            if (lst != null) {
                for (Entry<K, V> entry : lst) {
                    add(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void add(K key, V value) {
        int hash = getHash(key);
        int index = getIndex(hash);

        // Get the linked list at the index given by the hash
        LinkedList<Entry<K, V>> lst = table.get(index);

        // If the linked list at the index is null, create a new linked list and set it at the index
        if (lst == null) {
            lst = new LinkedList<>();
            table.set(index, lst);
        }

        lst.add(new Entry<>(key, value));
        size++;

        // If the load factor is greater than the max load factor, resize the table
        if (getLoad() >= maxLoad) {
            resize();
        }
    }

    public V get(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);

        // Get the linked list at the index given by the hash
        LinkedList<Entry<K, V>> lst = table.get(index);

        // If the index is empty, the key is not in the table
        if (lst == null) {
            return null;
        }

        for (Entry<K, V> entry : lst) {
            // Check if the key is equal to the entry's key
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null; // If the key is not found in the list, it is not in the table
    }

    public void remove(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);

        // Get the linked list at the index given by the hash
        LinkedList<Entry<K, V>> lst = table.get(index);

        // If the index is empty, the key is not in the table
        if (lst == null) {
            return;
        }

        for (Entry<K, V> entry : lst) {
            // Check if the key is equal to the entry's key
            if (entry.getKey().equals(key)) {
                lst.remove(entry);
                size--;
                return;
            }
        }
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public void setMaxLoad(double newMaxLoad) {
        if (newMaxLoad < MIN_VALID_LOAD || newMaxLoad > MAX_VALID_LOAD) {
            // If the new max load factor is not valid, throw an exception
            throw new IllegalArgumentException("Max load must be between " + MIN_VALID_LOAD + " and " + MAX_VALID_LOAD);
        }

        this.maxLoad = newMaxLoad;

        // If the load factor is greater than the new max load factor, resize the table
        if (getLoad() > maxLoad) {
            resize();
        }
    }

    public void setLoad(double newLoad) {
        if (newLoad < MIN_VALID_LOAD || newLoad > MAX_VALID_LOAD) {
            // If the new load factor is not valid, throw an exception
            throw new IllegalArgumentException("Load must be between " + MIN_VALID_LOAD + " and " + MAX_VALID_LOAD);
        }

        // If the new load factor is greater than the max load factor, return
        if (newLoad > maxLoad) {
            return;
        }

        // Round up so that the load factor is less than the new load factor
        int newSize = (int) Math.ceil(size / newLoad); 

        ArrayList<LinkedList<Entry<K, V>>> oldTable = table;
        fill(newSize);

        // Rehash all elements
        for (LinkedList<Entry<K, V>> lst : oldTable) {
            if (lst != null) {
                for (Entry<K, V> entry : lst) {
                    add(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public ArrayList<K> getKeys() {
        // Get all the keys in the table as an array list
        ArrayList<K> ans = new ArrayList<>();

        for (LinkedList<Entry<K, V>> lst : table) {
            if (lst != null) {
                for (Entry<K, V> entry : lst) {
                    // Add each key in the linked list to the array list
                    ans.add(entry.getKey());
                }
            }
        }
        return ans;
    }

    public ArrayList<V> getValues() {
        // Get all the values in the table as an array list
        ArrayList<V> ans = new ArrayList<>();

        for (LinkedList<Entry<K, V>> lst : table) {
            if (lst != null) {
                for (Entry<K, V> entry : lst) {
                    // Add each value in the linked list to the array list
                    ans.add(entry.getValue());
                }
            }
        }
        return ans;
    }

    public ArrayList<V> toArray() {
        // Get all the values in the table as an array list
        return getValues();
    }


    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();

        // Add each linked list to the string
        for (LinkedList<Entry<K, V>> lst : table) {
            if (lst != null) {
                ans.append("||");
                for (Entry<K, V> entry : lst) {
                    ans.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append(", ");
                }
            }
        }

        // If there are elements in the table, remove the last comma and space
        if (size > 0 && ans.length() >= 2) {
            ans.delete(ans.length() - 2, ans.length());
        }

        // Add pointer brackets to the string
        return "<" + ans.toString() + ">";
    }
}
