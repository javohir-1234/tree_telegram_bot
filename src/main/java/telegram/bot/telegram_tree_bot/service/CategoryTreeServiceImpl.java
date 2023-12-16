package telegram.bot.telegram_tree_bot.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import telegram.bot.telegram_tree_bot.entity.Tree;
import telegram.bot.telegram_tree_bot.entity.dto.TreeDTO;
import telegram.bot.telegram_tree_bot.repository.TreeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryTreeServiceImpl implements CategoryTreeService{


    @Autowired
    private TreeRepository treeRepository;



    @Override
    public Tree createCategoryTreeParent(String name) {
        Tree tree = new Tree();
        tree.setName(name);
        tree.setParent(null);
        tree.setChildren(List.of());
        treeRepository.save(tree);
        return tree;
    }

    @Override
    public Tree createCategoryTreeChildren(String parent, String children) {
        Optional<Tree> byParentName = treeRepository.findByName(parent);
        if (byParentName.isPresent()){
            Tree tree = new Tree();
            tree.setName(children);
            tree.setParent(byParentName.get());
            treeRepository.save(tree);
            return tree;
        }
        return null;
    }

    @Override
    public Tree getCategoryTree(Integer id) {
        return null;
    }

    @Override
    public boolean deleteCategoryTree(String name) {
        Optional<Tree> treeRepositoryByName = treeRepository.findByName(name);
        treeRepositoryByName.ifPresent(this::deleteTreeAndChildren);
        if (treeRepositoryByName.isPresent() && treeRepositoryByName.get().getParent() == null){
            treeRepository.delete(treeRepositoryByName.get());
        }
        return true;
    }

    private void deleteTreeAndChildren(Tree parent) {
        if (parent.getChildren() != null) {
            for (Tree child : new ArrayList<>(parent.getChildren())) {
                deleteTreeAndChildren(child);
            }
        }
        treeRepository.deleteTreeAndChildrenByName(parent.getName());
    }

    private TreeDTO convertToDTO(Tree tree) {
        TreeDTO treeDTO = new TreeDTO();
        treeDTO.setName(tree.getName());

        if (tree.getChildren() != null) {
            List<TreeDTO> childrenDTOs = new ArrayList<>();
            for (Tree child : tree.getChildren()) {
                childrenDTOs.add(convertToDTO(child));
            }
            treeDTO.setChildren(childrenDTOs);
        }

        return treeDTO;
    }

    public List<TreeDTO> getAll() {
        List<Tree> trees = treeRepository.findAll();
        List<TreeDTO> treeDTOs = new ArrayList<>();

        for (Tree tree : trees) {
            if (tree.getParent() == null) {
                treeDTOs.add(convertToDTO(tree));
            }
        }

        return treeDTOs;
    }
}
