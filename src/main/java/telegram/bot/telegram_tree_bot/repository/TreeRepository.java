package telegram.bot.telegram_tree_bot.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import telegram.bot.telegram_tree_bot.entity.Tree;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Integer> {

    Optional<Tree> findByParentName(String parentName);
    Optional<Tree> findByName(String parent);

    @Transactional
    @Modifying
    @Query("DELETE FROM Tree t WHERE t.name = :treeName OR t.parent.name = :treeName")
    void deleteTreeAndChildrenByName(String treeName);
}
