select 
	an.alternative_name,
	i.name,
	i.department
from rezepttool.tblingredients i
left join rezepttool.tblalternativenames an on i.id = an.ingredient_id;