package com.osp.core.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class NzTreeData {
    private String title;
    private String key;
    private boolean checked = false;
    private boolean expanded = true;
    private boolean isLeaf = true;
    private List<NzTreeData> children = new ArrayList<>();

    public NzTreeData(String title, String key, boolean checked, boolean expanded, boolean isLeaf, List<NzTreeData> children) {
        this.title = title;
        this.key = key;
        this.checked = checked;
        this.expanded = expanded;
        this.isLeaf = isLeaf;
        this.children = children;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
