SELECT 
--  r.url,
    r.name,
--    r.additional_information,
--    r.instructions,
--    r.portions,
--    r.callories,
    r.difficulty,
--    r.work_time,
--    r.cook_time,
--    r.rest_time,
    ri.amount,
    i.name,
    i.department
--    ain.alternative_name
FROM rezepttool.tblrecipes r
left join rezepttool.tblrecipeingredients ri on r.id = ri.recipe_id
left join rezepttool.tblingredients i on i.id = ri.ingredient_id
-- left join rezepttool.tblalternativenames ain on ain.ingredient_id = i.id

;

left join rezepttool.tblrecipecategories rc on rc.recipe_entity_id = r.id