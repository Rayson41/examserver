package com.exam.service;

import com.exam.model.exam.Category;

import java.util.Set;

public interface CategoryService {

    public Category addCategory(Category category);

    public Category updatecategory(Category category);

    public Set<Category> getcategories();

    public Category getCategory(Long categoryId);

    public void deleteCategory(Long categoryid);
}
