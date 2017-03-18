/**
 * Created by walter on 2017-03-18.
 */
public class File {
    String name;
    int size; //File size in bytes
    int hash;
    String[] blocks;

    public File(String name, int size, int hash, String[] blocks) {
        this.name = name;
        this.size = size;
        this.hash = hash;
        this.blocks = blocks;
    }
}
