package com.grasswort.picker.blog.vo;

import com.grasswort.picker.blog.dto.QueryBlogCategoryResponse;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname CategoryTreeNode
 * @Description 种类树状图(依据前端 vue-ant-design 要求格式返回)
 * @Date 2019/11/5 15:15
 * @blame Java Team
 */
@Getter
public class CategoryTreeNodeVO {

    private List<CategoryTreeNode> nodes;

    /**
     * 设置节点
     * @param categories
     */
    public void setNodes(List<QueryBlogCategoryResponse.Category> categories) {
        if (categories == null) {
            return;
        }
        this.nodes = categories.stream().map(CategoryTreeNodeVO::categoryConvertTreeNode).collect(Collectors.toList());
    }


    @Data
    public static class CategoryTreeNode {
        private Long key;

        private String title;

        private Long parentId;

        private List<CategoryTreeNode> children;
    }

    /**
     * 对象转换
     * @param category
     * @return
     */
    private static CategoryTreeNode categoryConvertTreeNode(QueryBlogCategoryResponse.Category category) {
        CategoryTreeNode treeNode = new CategoryTreeNode();
        treeNode.setKey(category.getCategoryId());
        treeNode.setTitle(category.getCategory());
        treeNode.setParentId(category.getParentId());
        treeNode.setChildren(category.getSubCategorys().stream().map(CategoryTreeNodeVO::categoryConvertTreeNode).collect(Collectors.toList()));
        return treeNode;
    }
}
