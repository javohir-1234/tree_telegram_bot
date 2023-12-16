package telegram.bot.telegram_tree_bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import telegram.bot.telegram_tree_bot.entity.Tree;
import telegram.bot.telegram_tree_bot.repository.TreeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTreeServiceImplTest {

    @Mock
    private TreeRepository treeRepository;

    private CategoryTreeServiceImpl categoryTreeServiceImpl;

    private Tree tree;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        tree = new Tree(5, "parent", null, List.of());
        categoryTreeServiceImpl = new CategoryTreeServiceImpl(treeRepository);
    }

    @Test
    void createCategoryTreeParent() {
        Mockito.when(categoryTreeServiceImpl.createCategoryTreeParent("parent"))
                .thenReturn(tree);
        Tree parent = categoryTreeServiceImpl.createCategoryTreeParent("parent");
        assertNotNull(parent);
    }

    @Test
    void deleteCategoryTree() {
        Mockito.when(treeRepository.findByName("parent"))
                .thenReturn(Optional.of(tree));
        boolean parent = categoryTreeServiceImpl.deleteCategoryTree("parent");
        assertTrue(parent);
    }
}