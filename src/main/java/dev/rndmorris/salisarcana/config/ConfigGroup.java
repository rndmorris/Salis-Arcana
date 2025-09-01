package dev.rndmorris.salisarcana.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraftforge.common.config.Configuration;

import dev.rndmorris.salisarcana.config.settings.Setting;

/**
 * A group of related settings that can be collectively enabled or disabled.
 */
public abstract class ConfigGroup implements IEnabler, IHaveSettings {

    private boolean enabled = true;
    protected final List<Setting> settings = new ArrayList<>();

    public ConfigGroup() {
        SalisConfig.groups.add(this);
    }

    /**
     * Load the group's settings. Only called if the group is enabled.
     *
     * @param configuration The configuration from which to load the group's settings.
     */
    public void loadFromConfig(@Nonnull Configuration configuration) {
        for (var comment : getCategoryComments()) {
            configuration.addCustomCategoryComment(comment.categoryId(), comment.comment());
        }
        for (var setting : settings) {
            setting.loadFromConfiguration(configuration);

        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable this config group.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * The unique id string of the group. Used as part of the group's "Enable" config option name.
     * This should also be the name, or the prefix of the name, of any configuration categories this group reads.
     */
    @Nonnull
    public abstract String getGroupName();

    /**
     * The comment string that will be displayed above the group's "Enable" config option.
     */
    @Nonnull
    public abstract String getGroupComment();

    @Override
    public Collection<Setting> getSettings() {
        return settings;
    }

    protected Collection<CategoryComment> getCategoryComments() {
        return Collections.emptyList();
    }

    protected static class CategoryComment {

        /**
         * The category to which the comment should be attached.
         */
        private final String categoryId;
        /**
         * The text of the comment to put on the category.
         */
        private final String comment;

        /**
         * A POJO containing a comment to put on a category, and the Id of the category on which the comment should be
         * placed.
         * 
         * @param categoryId The category to which the comment should be attached.
         * @param comment    The text of the comment to put on the category.
         */
        public CategoryComment(String categoryId, String comment) {
            this.categoryId = categoryId;
            this.comment = comment;
        }

        public String categoryId() {
            return categoryId;
        }

        public String comment() {
            return comment;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof CategoryComment that)) return false;
            return Objects.equals(categoryId, that.categoryId) && Objects.equals(comment, that.comment);
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryId, comment);
        }
    }
}
