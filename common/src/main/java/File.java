import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (size != file.size) return false;
        if (hash != file.hash) return false;
        if (name != null ? !name.equals(file.name) : file.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(blocks, file.blocks);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + size;
        result = 31 * result + hash;
        result = 31 * result + Arrays.hashCode(blocks);
        return result;
    }
}
