package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Account} class represents an account which may contain multiple posts.
 * The framework analyzes the posts provided by the data plugin and creates an
 * account that will be passed in to visualization plugin for display purpose.
 *
 * @author Team6
 */
public final class Account {
    private final String accountName;
    private final List<AnalyzedPost> posts;

    /**
     * Initializes a new {@code Account} object from an account name and a list
     * of analyzed posts.
     *
     * @param accountName   The name of the account
     * @param posts         A list of analyzed posts that belongs to the account
     */
    public Account(String accountName, List<AnalyzedPost> posts) {
        this.accountName = accountName;
        this.posts = posts;
    }

    /**
     * @return the name of the account
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @return the list of analyzed posts that belongs to the account
     */
    public List<AnalyzedPost> getPosts() {
        return new ArrayList<>(posts);
    }
}
