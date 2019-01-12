SELECT * FROM rezepttool.tblrecipes r
left join rezepttool.tblrecipeingredients ri on r.id = ri.recipe_id
left join rezepttool.tblingredients i on i.id = ri.ingredient_id
left join rezepttool.tblalternativenames ain on ain.ingredient_id = i.id
left join rezepttool.tblrecipecategories rc on rc.recipe_entity_id = r.id;