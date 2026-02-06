package com.ecom.Shopping_cart.service;

import java.util.List;

import com.ecom.Shopping_cart.model.Category;
import org.springframework.data.domain.Page;


public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean existCategory(String name);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategory();

    public Page<Category> getAllCategorPagination(Integer pageNo,Integer pageSize);

}