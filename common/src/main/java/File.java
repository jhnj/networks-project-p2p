/**
 * Created by walter on 2017-03-18.
 */
public class File {
    private String name;
    private int size; //File size in bytes
    private int hash;
    private String[] blocks;

    public File(String name, int size, int hash, String[] blocks) {
        this.name = name;
        this.size = size;
        this.hash = hash;
        this.blocks = blocks;
    }

    public String[] getBlocks() {
        return blocks;
    }

    public int getHash() {
        return hash;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
}
