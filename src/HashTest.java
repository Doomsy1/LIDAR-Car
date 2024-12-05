class HashTest {
    public static void main(String[] args) {
        HashTable<Integer, String> ht = new HashTable<>();
        ht.add(1, "A");
        ht.add(11, "B");
        ht.add(3, "C");
        ht.add(4, "D");
        ht.add(5, "E");
        ht.add(6, "F");
        ht.add(7, "G");
        ht.add(8, "H");
        System.out.println(ht);
    }
}