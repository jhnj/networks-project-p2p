package wj.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by walter on 2017-03-22.
 */
public class BlockListResponse {
    private int[] blocks;

    @JsonCreator
    public BlockListResponse(@JsonProperty("blocks") int[] blocks) {
        this.blocks = blocks;
    }

    public int[] getBlocks() {
        return blocks;
    }

    public void setBlocks(int[] blocks) {
        this.blocks = blocks;
    }
}
