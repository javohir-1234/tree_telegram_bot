package telegram.bot.telegram_tree_bot.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreeDTO {
    private String name;
    private List<TreeDTO> children;

    @Override
    public String toString() {
        return toString(0, new StringBuilder()).toString();
    }

    private StringBuilder toString(int depth, StringBuilder stringBuilder) {
        appendIndentation(depth, stringBuilder);
        stringBuilder.append(name).append("\n");

        if (children != null) {
            for (TreeDTO child : children) {
                child.toString(depth + 1, stringBuilder);
            }
        }

        return stringBuilder;
    }

    private void appendIndentation(int depth, StringBuilder stringBuilder) {
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("  ");
        }
    }
}