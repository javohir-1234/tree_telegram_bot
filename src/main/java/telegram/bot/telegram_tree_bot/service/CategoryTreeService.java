package telegram.bot.telegram_tree_bot.service;

import org.springframework.stereotype.Service;
import telegram.bot.telegram_tree_bot.entity.Tree;
import telegram.bot.telegram_tree_bot.entity.dto.TreeDTO;

import java.util.List;


public interface CategoryTreeService {

    Tree createCategoryTreeParent(String name);
    Tree createCategoryTreeChildren(String parent, String children);
    Tree getCategoryTree(Integer id);
    boolean deleteCategoryTree(String name);
    List<TreeDTO> getAll();
}
