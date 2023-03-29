package com.example.rallyapp.database.entities.entity_model_adapters

import com.example.rallyapp.api.dataModel.response_models.Category
import com.example.rallyapp.api.dataModel.response_models.Menu
import com.example.rallyapp.database.entities.MenuWithCategory

class MenuWithCategoryEntityAdapter {

     fun menuWithCategoryToModel(menuWithCategory: MenuWithCategory): Menu{
        val category = Category(
            id = menuWithCategory.category.categoryId,
            category = menuWithCategory.category.categoryDisplayName
        )
        return Menu(
            id = menuWithCategory.menu.menuId,
            name = menuWithCategory.menu.name,
            price = menuWithCategory.menu.price,
            category = category,
            image = menuWithCategory.menu.image,
            description = menuWithCategory.menu.description
        )
    }
}